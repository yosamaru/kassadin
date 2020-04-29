package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.ChequeDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.ChequePO;
import org.springframework.stereotype.Repository;

@Repository
public class ChequeDaoImpl extends AbstractBaseDao<ChequePO, Long> implements ChequeDao {

	@Override
	public ChequePO getChequeByAccountId(Long userId) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(ChequePO.class);
		genericCriteria.where(genericCriteria.eq("userId", userId));
		return (ChequePO) genericCriteria.createQuery().getSingleResult();
	}


}
