package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import java.math.BigDecimal;
import java.util.List;

public interface CashAccountTransactionDao extends BaseDao<CashAccountTransactionPO, Long> {

	List<CashAccountTransactionPO> listByAccountId(final Long userId);

	BigDecimal sumAmountByType(final String transactionType);

	Long countAll();
}
