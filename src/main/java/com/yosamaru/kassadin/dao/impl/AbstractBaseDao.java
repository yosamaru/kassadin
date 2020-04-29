package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.dao.query.SimpleQuery;
import com.yosamaru.kassadin.dao.query.SimpleResult;
import com.yosamaru.kassadin.util.ClassUtil;
import com.yosamaru.kassadin.util.DateUtil;
import com.yosamaru.kassadin.util.Util;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.NativeQuery;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional(Transactional.TxType.MANDATORY)
public abstract class AbstractBaseDao<T, ID extends Serializable> implements BaseDao<T, ID> {
	private static interface TypeConverter {
		Object convert(final Class<?> clazz, final String value) throws Exception;
	}

	/**
	 * Apart from Date, all boxed primitives have a String constructor
	 **/
	private static class PrimitiveTypeConverter<Z> implements TypeConverter {
		@Override
		public Object convert(final Class<?> clazz, final String value) throws Exception {
			return clazz.getConstructor(String.class).newInstance(value);
		}
	}

	class TotalRows {
		private final int totalFilteredRows;

		private TotalRows(final int totalFilteredRows) {
			this.totalFilteredRows = totalFilteredRows;
		}

		int getTotalFilteredRows() {
			return totalFilteredRows;
		}
	}

	private final Logger logger = LoggerFactory.getLogger(AbstractBaseDao.class);

	private static final Map<Class<?>, TypeConverter> coverterMap;
	private final int DEFAULT_PADDING_LENGTH = 9;

	private static final int MAX_IN_SIZE = 1000;

	static {
		coverterMap = new HashMap<>();
		coverterMap.put(Boolean.class, new PrimitiveTypeConverter<Boolean>());
		coverterMap.put(Byte.class, new PrimitiveTypeConverter<Byte>());
		coverterMap.put(Integer.class, new PrimitiveTypeConverter<Integer>());
		coverterMap.put(Long.class, new PrimitiveTypeConverter<Long>());
		coverterMap.put(Float.class, new PrimitiveTypeConverter<Float>());
		coverterMap.put(Double.class, new PrimitiveTypeConverter<Double>());
		coverterMap.put(BigInteger.class, new PrimitiveTypeConverter<BigInteger>());
		coverterMap.put(BigDecimal.class, new PrimitiveTypeConverter<BigDecimal>());
	}

	@PersistenceContext
	private EntityManager entityManager;

	private final Class<T> bean;

	private final SimpleDateFormat simpleDateFormat;

	@SuppressWarnings("unchecked")
	protected AbstractBaseDao() {
		bean = (Class<T>) ClassUtil.getTypeArguments(AbstractBaseDao.class, this.getClass()).get(0);
		simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
	}

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}


	private void applyJunction(final Criteria criteria, final CriteriaQueryTranslator translator, final Junction junction, final String point, final String query) {
		final Class<?> clazz = translator.getType(criteria, point).getReturnedClass();
		if (clazz == String.class) {
			final Criterion c = Restrictions.like(point, query, MatchMode.ANYWHERE);
			junction.add(c);
		} else if (clazz == Date.class) {

			if (query.matches("\\d{4}-\\d{2}-\\d{2}")) {
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				try {
					final Date date = sdf.parse(query);
					final String column = translator.getColumn(criteria, point);
					final Criterion c = Restrictions.sqlRestriction("trunc(" + column + ") = ?", DateUtil.setZeroTime(date), StandardBasicTypes.DATE);
					junction.add(c);

				} catch (ParseException ex) {
					logger.warn(ex.getMessage());
					logger.debug(Util.getStackTrace(ex));
				}
			}
		} else {
			final TypeConverter tc = coverterMap.get(clazz);
			if (tc != null) {
				try {
					final Object value = tc.convert(clazz, query);
					if (value != null) {

						final Criterion c = Restrictions.eq(point, value);
						junction.add(c);
					}

				} catch (final Exception ex) {
					/** Ignore **/
				}

			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("Requested converter ({}) is not found", clazz.getSimpleName());
				}
			}
		}
	}

	protected TotalRows applySimpleSearch(final Criteria criteria, final SimpleQuery simpleQuery, final Junction junction, final boolean join, final boolean distinctSearch) {
		final CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
		final Map<String, String> aliasMap = new HashMap<>();
		final Map<String, String> map = new HashMap<>();

		final Iterator<Subcriteria> iter = criteriaImpl.iterateSubcriteria();
		while (iter.hasNext()) {
			final Subcriteria subcriteria = iter.next();
			if (subcriteria.getAlias() != null) {
				aliasMap.put(subcriteria.getPath(), subcriteria.getAlias());
			}
		}

		/** Build a mapping between alias and field **/
		for (final SimpleQuery.Column c : simpleQuery.getColumns()) {
			final String[] fields = c.getName().split("\\.|\\?");
			if (fields.length > 1) {
				String associationPath = fields[0];
				String alias = '$' + fields[0];

				/** check if need join **/
				if (join) {
					/** Support 1st level left outer join **/
					if (aliasMap.containsKey(associationPath)) {
						alias = aliasMap.get(associationPath);
					} else {
						criteria.createAlias(associationPath, alias, c.isInner() ? JoinType.INNER_JOIN : JoinType.LEFT_OUTER_JOIN);
						aliasMap.put(associationPath, alias);
					}
				}

				/** The last component is a field, so we do not need to build an alias for it **/
				for (int i = 1; i < fields.length - 1; i++) {
					associationPath = alias + '.' + fields[i];
					alias = '$' + fields[i];

					if (!aliasMap.containsKey(associationPath)) {
						criteria.createAlias(associationPath, alias);
						aliasMap.put(associationPath, alias);
					}
				}

				if (logger.isDebugEnabled()) {
					logger.debug("Alias: {}->{}", associationPath, alias);
				}

				map.put(c.getName(), alias + '.' + fields[fields.length - 1]);
			} else {
				map.put(c.getName(), fields[0]);
			}
		}

		final org.hibernate.engine.spi.SharedSessionContractImplementor session = criteriaImpl.getSession();
		final SessionFactoryImplementor factory = session.getFactory();
		final CriteriaQueryTranslator translator = new CriteriaQueryTranslator(factory, criteriaImpl, criteriaImpl.getEntityOrClassName(), CriteriaQueryTranslator.ROOT_SQL_ALIAS);

		/** Apply Condition **/
		final Junction globalJ = Restrictions.disjunction();
		final String query = simpleQuery.getQuery();
		if (StringUtils.isNotBlank(query)) {
			for (final String point : map.values()) {
				applyJunction(criteria, translator, globalJ, point, query);
			}
		}

		/** Apply Specific Condition **/
		final Junction localJ = Restrictions.disjunction();
		for (final SimpleQuery.Column c : simpleQuery.getColumns()) {
			final String point = map.get(c.getName());
			final String value = c.getValue();
			if (StringUtils.isNotBlank(value)) {
				applyJunction(criteria, translator, localJ, point, c.getValue());
			}
		}
		if (globalJ.conditions().iterator().hasNext()) {
			if (localJ.conditions().iterator().hasNext()) {
				criteria.add(Restrictions.and(globalJ, localJ));
			} else {
				criteria.add(globalJ);
			}
		}
		if (localJ.conditions().iterator().hasNext()) {
			criteria.add(localJ);
		}

		final Projection p = criteriaImpl.getProjection();
		final ResultTransformer rt = criteriaImpl.getResultTransformer();

		/** Get Total Rows **/
		final int totalFiltersRows = getTotalRows(criteria, distinctSearch);

		/** Reset Criteria **/
		criteria.setProjection(p);
		criteria.setResultTransformer(rt);

		/** Apply Order **/
		for (final SimpleQuery.Column c : simpleQuery.getColumns()) {
			final String alias = map.get(c.getName());
			if (c.isAscending() != null) {
				if (c.isAscending()) {
					criteria.addOrder(Order.asc(alias).nulls(NullPrecedence.LAST));
				} else {
					criteria.addOrder(Order.desc(alias).nulls(NullPrecedence.LAST));
				}
			}
		}

		/** Apply Paging **/
		criteria.setFirstResult((int) simpleQuery.getStartRow());
		criteria.setMaxResults((int) simpleQuery.getPageSize());

		if (!criteriaImpl.iterateOrderings().hasNext()) {
			criteria.addOrder(Order.desc(getIdentifierPropertyName(bean)));
		}
		return new TotalRows(totalFiltersRows);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		final GenericCriteria genericCriteria = this.getGenericCriteria(bean);
		return genericCriteria.createQuery().getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getById(final ID id) {
		return getSession().get(bean, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SimpleResult<T> find(final SimpleQuery simpleQuery) {
		final Session s = this.getSession();
		final Criteria c = s.createCriteria(bean);

		final TotalRows tw = applySimpleSearch(c, simpleQuery, Restrictions.disjunction(), true, true);
		return new SimpleResult<>(c.list(), tw.getTotalFilteredRows());
	}

	protected SimpleResult<T> find(final Criteria criteria, final SimpleQuery simpleQuery) {
		return find(criteria, simpleQuery, Restrictions.disjunction(), true, true);
	}

	protected SimpleResult<T> find(final Criteria criteria, final SimpleQuery simpleQuery, final boolean join) {
		return find(criteria, simpleQuery, Restrictions.disjunction(), join, true);
	}

	protected SimpleResult<T> find(final Criteria criteria, final SimpleQuery simpleQuery, final boolean join, final boolean distinctSearch) {
		return find(criteria, simpleQuery, Restrictions.disjunction(), join, distinctSearch);
	}

	@SuppressWarnings("unchecked")
	protected SimpleResult<T> find(final Criteria criteria, final SimpleQuery simpleQuery, final Junction junction, final boolean join, final boolean distinctSearch) {
		final TotalRows tw = applySimpleSearch(criteria, simpleQuery, junction, join, distinctSearch);
		return new SimpleResult<>(criteria.list(), tw.getTotalFilteredRows());
	}

	@SuppressWarnings("unchecked")
	@Override
	public T loadById(final ID id) {
		return (T) getSession().load(bean, id);
	}

	@Override
	public void add(final T entity) {
		getSession().save(entity);
	}

	@Override
	public void delete(final T entity) {
		getSession().delete(entity);
	}

	@Override
	public void update(final T entity) {
		getSession().update(entity);
	}

	@Override
	public void saveOrUpdate(final T entity) {
		getSession().saveOrUpdate(entity);
	}

	@Override
	public void deleteById(final ID id) {
		final Session s = this.getSession();
		s.delete(s.load(bean, id));
	}

	@Override
	public void deleteById(final ID[] id) {
		final Session s = this.getSession();
		for (final ID p : id) {
			s.delete(s.load(bean, p));
		}
	}

	@Override
	public boolean exists(final ID id) {
		return loadById(id) != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getOne() {
		final GenericCriteria genericQuery = this.getGenericCriteria(bean);
		TypedQuery typedQuery = genericQuery.createQuery();
		typedQuery.setMaxResults(1);
		return (T) typedQuery.getSingleResult();
	}

	protected int getTotalRows(final Criteria criteria, final boolean distinctSearch) {
		final String identifier = getIdentifierPropertyName(bean);
		if (identifier != null && distinctSearch) {
			criteria.setProjection(Projections.projectionList().add(Projections.countDistinct(identifier)));
		} else {
			criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		}

		criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
		return ((Long) criteria.uniqueResult()).intValue();
	}

	protected int getTotalRows() {
		final Session s = this.getSession();
		return getTotalRows(s.createCriteria(bean), true);
	}

	protected static Disjunction splitInClause(final String alias, final Object[] values) {
		final Disjunction disjunction = Restrictions.disjunction();

		if (values.length > MAX_IN_SIZE) {
			for (final Object[] o : Util.split(values, MAX_IN_SIZE, new Object[][]{})) {
				disjunction.add(Restrictions.in(alias, o));
			}
		} else if (values.length > 0) {
			disjunction.add(Restrictions.in(alias, values));
		} else {
			/** force to FALSE is values are empty **/
			disjunction.add(Restrictions.sqlRestriction("(0=1)"));
		}
		return disjunction;
	}


	protected static Object[][] splitInSqlCalsue(final StringBuilder outerSql, final String column, final String paramterNamePrefix, final Object[] values) {
		final Object[][] ret;
		if (values.length > MAX_IN_SIZE) {
			final StringBuilder sb = new StringBuilder();
			sb.append(" and (1=0 ");

			final Object[][] parts = Util.split(values, MAX_IN_SIZE, new Object[][]{});
			for (int i = 0; i < parts.length; i++) {
				sb.append(String.format("or %s in (:%s%d)", column, paramterNamePrefix, i));
			}

			sb.append(')');
			outerSql.append(sb.toString());
			ret = parts;

		} else {
			outerSql.append(String.format(" and %s in (:%s%d)", column, paramterNamePrefix, 0));
			ret = new Object[][]{values};
		}
		return ret;
	}

	protected BigInteger getSequence(final String sequence) {
		final String sql = String.format("select %s.nextval as ret from dual", sequence);
		final NativeQuery q = this.getSession().createNativeQuery(sql);
		q.addScalar("ret", new BigIntegerType());
		return (BigInteger) q.uniqueResult();
	}

	protected String getSequence(final String sequence, final String prefix, final int paddingSize) {
		final BigInteger bi = getSequence(sequence);
		return prefix + StringUtils.leftPad(bi.toString(), paddingSize, '0');
	}

	protected String getSequence(final String sequence, final String prefix) {
		return getSequence(sequence, prefix, DEFAULT_PADDING_LENGTH);
	}

	protected String getSequenceWithNow(final String sequence, final String prefix) {
		final Date now = new Date();
		return getSequence(sequence, prefix + simpleDateFormat.format(now));
	}

	protected String getSequenceWithNowAndLength(final String sequence, final String prefix, final Integer length) {
		final Date now = new Date();
		return getSequence(sequence, prefix + simpleDateFormat.format(now), length);
	}

	protected String getSequenceWithLength(final String sequence, final String prefix, final Integer length) {
		return getSequence(sequence, prefix, length);
	}


	@Override
	public void flush() {
		getSession().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(final T entity) {
		return (T) getSession().merge(entity);
	}

	private String getIdentifierPropertyName(final Class<?> clazz) {

		final ClassMetadata meta = getSession().getSessionFactory().getClassMetadata(clazz);
		return meta.getIdentifierPropertyName();
	}

	@Override
	public void executeUpdateSql(final String sql) {
		getSession().createNativeQuery(sql).executeUpdate();
	}

	/**
	 * @param fromClazz 查询模型对象
	 * @return
	 */
	protected GenericCriteria getGenericCriteria(Class fromClazz) {
		return GenericCriteria.forClass(fromClazz, fromClazz, entityManager);
	}

	/**
	 * @param fromClazz   查询模型对象
	 * @param returnClazz 返回模型对象
	 * @return
	 */
	protected GenericCriteria getGenericCriteria(Class fromClazz, Class returnClazz) {
		return GenericCriteria.forClass(fromClazz, returnClazz, entityManager);
	}

	/**
	 * @param simpleQuery
	 * @return
	 */
	protected SimpleResult<T> findPage(final SimpleQuery simpleQuery) {
		final GenericCriteria c = getGenericCriteria(bean);
		return findPage(simpleQuery, c);
	}

	/**
	 * @param simpleQuery
	 * @param genericCriteria
	 * @return
	 */
	protected SimpleResult<T> findPage(final SimpleQuery simpleQuery, final GenericCriteria genericCriteria) {
		TypedQuery typedQuery = genericCriteria.createQuery();
		typedQuery.setFirstResult((int) simpleQuery.getStartRow());
		typedQuery.setMaxResults((int) simpleQuery.getPageSize());
		return new SimpleResult<>(typedQuery.getResultList(), getTotalRowsByGenericCriteria(genericCriteria));
	}

	/**
	 * @param genericCriteria
	 * @return
	 */
	protected int getTotalRowsByGenericCriteria(final GenericCriteria genericCriteria) {
		genericCriteria.select(genericCriteria.getCriteriaBuilder().count(genericCriteria.getRoot()));
		return ((Long) genericCriteria.createQuery().getSingleResult()).intValue();
	}

	/**
	 * @param genericCriteria
	 * @return
	 */
	protected T getSingleResultOrNull(final GenericCriteria genericCriteria) {
		final List<T> list = genericCriteria.createQuery().setMaxResults(1).getResultList();
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

}
