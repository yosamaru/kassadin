package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.ContractDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.ContractPO;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ContractDaoImpl extends AbstractBaseDao<ContractPO, Long> implements ContractDao {

	@Override
	public List<ContractPO> listByAccountId(Long accountId) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(ContractPO.class);
		genericCriteria.where(genericCriteria.eq("accountId", accountId));
		return genericCriteria.createQuery().getResultList();
	}

	@Override
	public Long countAll() {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ContractPO.class);
		criteria.setProjection(Projections.count("id"));
		return (Long) criteria.uniqueResult();
	}

	@Override
	public Long countByTransactionStatus(String transactionStatus) {
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ContractPO.class);
		criteria.setProjection(Projections.count("transactionStatus"));
		criteria.add(Restrictions.eq("transactionStatus", transactionStatus));
		return (Long) criteria.uniqueResult();
	}
}
