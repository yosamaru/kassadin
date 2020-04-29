package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.ContractPO;
import java.util.List;

public interface ContractDao extends BaseDao<ContractPO, Long> {
	List<ContractPO> listByAccountId(final Long accountId);

	Long countAll();

	Long countByTransactionStatus(final String transactionStatus);
}
