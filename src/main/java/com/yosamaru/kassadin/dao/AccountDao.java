package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.AccountPO;

public interface AccountDao extends BaseDao<AccountPO, Long> {

	AccountPO getAccountByPhone(final Integer telephone);

	AccountPO getAccountByAccountName(final String accountName);
}
