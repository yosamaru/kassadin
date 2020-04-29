package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.ChequePO;

public interface ChequeDao extends BaseDao<ChequePO, Long> {
	ChequePO getChequeByAccountId(final Long accountId);
}
