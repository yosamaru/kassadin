package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.CashAccountTransactionDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class CashAccountTransactionDaoImpl extends AbstractBaseDao<CashAccountTransactionPO, Long> implements CashAccountTransactionDao {
	@Override
	public List<CashAccountTransactionPO> listByAccountId(Long userId) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(CashAccountTransactionPO.class);
		genericCriteria.where(genericCriteria.eq("userId", userId));
		return genericCriteria.createQuery().getResultList();
	}

	@Override
	public BigDecimal sumAmountByType(String transactionType) {
		final Session session = this.getSession();
		Criteria criteria = session.createCriteria(CashAccountTransactionPO.class);
		// setProjection 设置聚合函数，显示的字段别名
		criteria.setProjection(Projections.sum("transactionAmount"));
		// Restrictions where 查询条件
		criteria.add(Restrictions.eq("transactionType", transactionType));
		return (BigDecimal) criteria.uniqueResult();
	}

	@Override
	public Long countAll() {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(CashAccountTransactionPO.class);
		criteria.setProjection(Projections.count("id"));
		return (Long) criteria.uniqueResult();
	}
}
