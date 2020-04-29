package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.AccountDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDaoImpl extends AbstractBaseDao<AccountPO, Long> implements AccountDao {
	@Override
	public AccountPO getAccountByPhone(Integer telephone) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(AccountPO.class);
		genericCriteria.where(genericCriteria.eq("telephone", telephone));
		return (AccountPO) genericCriteria.createQuery().getSingleResult();
	}

	@Override
	public AccountPO getAccountByAccountName(String accountName) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(AccountPO.class);
		genericCriteria.where(genericCriteria.eq("accountName", accountName));
		return (AccountPO) genericCriteria.createQuery().getSingleResult();
	}
}
