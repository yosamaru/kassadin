package com.yosamaru.kassadin.dao.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.internal.util.StringHelper;

public class GenericCriteria implements Serializable {

	private static final long serialVersionUID = -262432975885358940L;

	private EntityManager entityManager;

	/**
	 * 要查询的模型对象
	 */
	private Class fromClazz;

	/**
	 * 要返回的模型对象
	 */
	private Class returnClazz;

	/**
	 * 查询条件列表
	 */
	private Root root;

	private List<Predicate> predicates;

	private CriteriaQuery criteriaQuery;

	private CriteriaBuilder criteriaBuilder;

	/**
	 * 排序方式列表
	 */
	private List<Order> orders;

	private String projection;

	private String selectProperty;

	private static final int MAX_IN_SIZE = 1000;

	private Map<String, From> fromMap;

	public enum OrderEnum {
		ASC("asc"), DESC("desc");
		private final String value;

		OrderEnum(final String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}
	}

	private GenericCriteria() {

	}

	private GenericCriteria(Class fromClazz, Class returnClazz, EntityManager entityManager) {
		this.fromClazz = fromClazz;
		this.returnClazz = returnClazz;
		this.entityManager = entityManager;
		this.criteriaQuery = criteriaBuilder.createQuery(this.returnClazz);
		this.root = criteriaQuery.from(this.fromClazz);
		this.predicates = new ArrayList();
		this.orders = new ArrayList();
		this.fromMap = new HashMap<>();
		criteriaQuery.select(root);
	}


	/**
	 * 通过类创建查询条件
	 */
	public static GenericCriteria forClass(Class fromClazz, Class returnClazz, EntityManager entityManager) {
		return new GenericCriteria(fromClazz, returnClazz, entityManager);
	}

	private boolean isNullOrEmpty(Object value) {
		if (value instanceof String) {
			return value == null || "".equals(value);
		}
		return value == null;
	}

	public Predicate or(Predicate... restrictions) {
		if (restrictions == null) {
			return null;
		}
		Predicate predicate = criteriaBuilder.or(restrictions);
		return predicate;
	}

	public Predicate and(Predicate... restrictions) {
		if (restrictions == null) {
			return null;
		}
		return criteriaBuilder.and(restrictions);
	}


	/**
	 * 相等
	 */
	public Predicate eq(String propertyName, Object value, From propertyFrom) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return criteriaBuilder.equal(getPropertyPath(propertyName, propertyFrom), value);
	}

	/**
	 * 相等
	 *
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Predicate eq(String propertyName, Object value) {
		return eq(propertyName, value, root);
	}


	public Predicate orLike(List<String> propertyName, String value) {
		if (isNullOrEmpty(value) || (propertyName.size() == 0)) {
			return null;
		}

		if (value.indexOf("%") < 0) {
			value = "%" + value + "%";
		}

		Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(getPropertyPath(propertyName.get(0), root), value.toString()));
		for (int i = 1; i < propertyName.size(); ++i) {
			predicate = criteriaBuilder.or(predicate, criteriaBuilder.like(getPropertyPath(propertyName.get(i), root), value));
		}
		return predicate;
	}

	/**
	 * 空
	 */
	public Predicate isNull(String propertyName, From propertyFrom) {
		return criteriaBuilder.isNull(getPropertyPath(propertyName, propertyFrom));
	}

	public Predicate isNull(String propertyName) {
		return isNull(propertyName, root);
	}

	/**
	 * 非空
	 */
	public Predicate isNotNull(String propertyName, From propertyFrom) {
		return criteriaBuilder.isNotNull(getPropertyPath(propertyName, propertyFrom));
	}

	public Predicate isNotNull(String propertyName) {
		return isNotNull(propertyName, root);
	}

	/**
	 * 不相等
	 */
	public Predicate notEq(String propertyName, Object value, From propertyFrom) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return criteriaBuilder.notEqual(getPropertyPath(propertyName, propertyFrom), value);
	}

	public Predicate notEq(String propertyName, Object value) {
		return notEq(propertyName, value, root);
	}

	/**
	 * 模糊匹配
	 *
	 * @param propertyName 属性名称
	 * @param value        属性值
	 */
	public Predicate like(String propertyName, String value, From propertyFrom) {
		if (isNullOrEmpty(value)) {
			return null;
		}

		if (value.indexOf("%") < 0) {
			value = "%" + value + "%";
		}
		return criteriaBuilder.like(getPropertyPath(propertyName, propertyFrom), value);
	}

	public Predicate like(String propertyName, String value) {
		return like(propertyName, value, root);
	}

	/**
	 * 时间区间查询
	 *
	 * @param propertyName 属性名称
	 * @param lo           属性起始值
	 * @param go           属性结束值
	 */
	public Predicate between(String propertyName, Date lo, Date go, From propertyFrom) {
		if (!isNullOrEmpty(lo) && !isNullOrEmpty(go)) {
			return criteriaBuilder.between(getPropertyPath(propertyName, propertyFrom), lo, go);
		}
		return null;
	}

	public Predicate between(String propertyName, Date lo, Date go) {
		return between(propertyName, lo, go, root);
	}

	public void between(String propertyName, Number lo, Number go) {
		if (!(isNullOrEmpty(lo))) {
			ge(propertyName, lo);
		}
		if (!(isNullOrEmpty(go))) {
			le(propertyName, go);
		}

	}

	/**
	 * 小于等于
	 *
	 * @param propertyName 属性名称
	 * @param value        属性值
	 */
	public Predicate le(String propertyName, Number value, From propertyFrom) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return criteriaBuilder.le(getPropertyPath(propertyName, propertyFrom), value);
	}

	public Predicate le(String propertyName, Number value) {
		return le(propertyName, value, root);
	}


	/**
	 * 小于
	 *
	 * @param propertyName 属性名称
	 * @param value        属性值
	 */
	public Predicate lt(String propertyName, Number value, From propertyFrom) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return criteriaBuilder.lt(getPropertyPath(propertyName, propertyFrom), value);
	}

	public Predicate lt(String propertyName, Number value) {
		return lt(propertyName, value, root);
	}

	/**
	 * 大于等于
	 *
	 * @param propertyName 属性名称
	 * @param value        属性值
	 */
	public Predicate ge(String propertyName, Number value, From propertyFrom) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return criteriaBuilder.ge(getPropertyPath(propertyName, propertyFrom), value);
	}

	public Predicate ge(String propertyName, Number value) {
		return ge(propertyName, value, root);
	}

	/**
	 * 大于
	 *
	 * @param propertyName 属性名称
	 * @param value        属性值
	 */
	public Predicate gt(String propertyName, Number value, From propertyFrom) {
		if (isNullOrEmpty(value)) {
			return null;
		}
		return criteriaBuilder.gt(getPropertyPath(propertyName, propertyFrom), value);
	}

	public Predicate gt(String propertyName, Number value) {
		return gt(propertyName, value, root);
	}

	/**
	 * in
	 *
	 * @param propertyName 属性名称
	 * @param value        值集合
	 */
	public Predicate in(String propertyName, Collection value, From propertyFrom) {
		if ((value == null) || (value.size() == 0)) {
			return null;
		}
		Iterator iterator = value.iterator();
		CriteriaBuilder.In in = criteriaBuilder.in(getPropertyPath(propertyName, propertyFrom));

		int i = 1;
		Predicate predicate = null;
		while (iterator.hasNext()) {
			in.value(iterator.next());
			i++;
			if (i >= MAX_IN_SIZE) {
				if (predicate == null) {
					predicate = in;
				} else {
					predicate = criteriaBuilder.or(predicate, in);
				}
				in = criteriaBuilder.in(getPropertyPath(propertyName, propertyFrom));
				i = 1;
			}
		}
		if (predicate == null) {
			predicate = in;
		} else {
			predicate = criteriaBuilder.or(predicate, in);
		}
		return predicate;
	}

	public Predicate in(String propertyName, Collection value) {
		return in(propertyName, value, root);
	}

	/**
	 * not in
	 *
	 * @param propertyName 属性名称
	 * @param value        值集合
	 */
	public Predicate notIn(String propertyName, Collection value, From propertyFrom) {
		return criteriaBuilder.not(this.in(propertyName, value, propertyFrom));
	}

	public Predicate notIn(String propertyName, Collection value) {
		return notIn(propertyName, value, root);
	}

	/**
	 * where条件
	 *
	 * @param restrictions
	 */
	public void where(Predicate... restrictions) {
		this.predicates.addAll(Arrays.asList(restrictions));
	}

	/**
	 * group by
	 *
	 * @param propertyNames
	 */
	public void groupBy(String... propertyNames) {
		if (propertyNames == null) {
			return;
		}
		List<String> propertyNameList = Arrays.asList(propertyNames);
		List<Expression> groupByExpressions = new ArrayList<>();
		for (String propertyName : propertyNameList) {
			groupByExpressions.add(getPropertyPath(propertyName, root));
		}
		criteriaQuery.groupBy(groupByExpressions);
	}


	/**
	 * 获取property路径
	 *
	 * @param propertyName
	 * @param propertyFrom
	 * @return
	 */
	public Path getPropertyPath(String propertyName, From propertyFrom) {
		if (isNullOrEmpty(propertyName)) {
			return null;
		}
		From joinFrom = propertyFrom;
		while (propertyName.indexOf(".") > 0) {
			String rootName = StringHelper.root(propertyName);
			if (fromMap.get(rootName) == null) {
				fromMap.put(rootName, joinFrom.join(rootName));
			}
			joinFrom = fromMap.get(rootName);
			propertyName = StringHelper.unroot(propertyName);
		}
		return joinFrom.get(propertyName);
	}

	/**
	 * 创建查询条件
	 *
	 * @return JPA离线查询
	 */
	public CriteriaQuery newCriteriaQuery() {

		if (this.orders != null) {
			criteriaQuery.orderBy(orders);
		}

		if (!isNullOrEmpty(selectProperty)) {
			criteriaQuery.select(getPropertyPath(selectProperty, root));
		}

		if (CollectionUtils.isNotEmpty(this.predicates)) {
			criteriaQuery.where(this.predicates.toArray(new Predicate[0]));
		}

		return criteriaQuery;
	}

	public TypedQuery createQuery() {
		return this.entityManager.createQuery(this.newCriteriaQuery());
	}

	public CriteriaQuery select(Selection selection) {
		return criteriaQuery.select(selection);
	}

	public void addOrder(String propertyName, String order) {
		if (order == null || propertyName == null) {
			return;
		}
		if (this.orders == null) {
			this.orders = new ArrayList();
		}
		if (order.equalsIgnoreCase(OrderEnum.ASC.toString())) {
			this.orders.add(criteriaBuilder.asc(getPropertyPath(propertyName, root)));
		} else if (order.equalsIgnoreCase(OrderEnum.DESC.toString())) {
			this.orders.add(criteriaBuilder.desc(getPropertyPath(propertyName, root)));
		}
	}

	public void setOrder(String propertyName, String order) {
		this.orders = null;
		addOrder(propertyName, order);
	}

	public String getProjection() {
		return this.projection;
	}

	public void setProjection(String projection) {
		this.projection = projection;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}


	public CriteriaQuery getCriteriaQuery() {
		return criteriaQuery;
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	public void setFetchModes(List<String> fetchField, List<String> fetchMode) {
	}

	public String getSelectProperty() {
		return selectProperty;
	}

	public void setSelectProperty(String selectProperty) {
		this.selectProperty = selectProperty;
	}

	public Root getRoot() {
		return root;
	}

	public List<Predicate> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<Predicate> predicates) {
		this.predicates = predicates;
	}
}
