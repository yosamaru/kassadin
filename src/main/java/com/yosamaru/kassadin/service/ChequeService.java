package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.ChequePO;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.math.BigDecimal;
import java.util.List;

public interface ChequeService extends BaseService<ChequePO, Long> {
	/**
	 * 查询详情信息
	 *
	 * @param id
	 * @return
	 */
	public ChequePO findInfo(Long id);

	/**
	 * 账户充值
	 *
	 * @param userId
	 * @param amount
	 * @return
	 */
	public CommonResponseParams recharge(Long userId, BigDecimal amount);

	/**
	 * 根据用户Id查询
	 *
	 * @return
	 */
	List<ChequePO> findAll();

	ChequePO getChequeByAccountId(Long userId);
}
