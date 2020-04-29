package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import java.math.BigDecimal;
import java.util.List;

public interface CashAccountTransactionService extends BaseService<CashAccountTransactionPO, Long> {
	/**
	 * 查询交易信息
	 *
	 * @param accountId
	 * @return
	 */
	public CashAccountTransactionPO findInfo(Long accountId);

	/**
	 * 根据用户Id查询
	 *
	 * @param userId
	 * @return
	 */
	List<CashAccountTransactionPO> findAll(Long userId);

	BigDecimal sumAmoutByStatus(String code);

	Long count();
}
