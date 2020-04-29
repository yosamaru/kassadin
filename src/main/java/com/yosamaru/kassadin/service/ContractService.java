package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.BookPO;
import com.yosamaru.kassadin.entity.PO.ContractPO;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;

public interface ContractService extends BaseService<ContractPO, Long> {
	/**
	 * 查询详情信息
	 *
	 * @param id
	 * @return
	 */
	public ContractPO findInfo(Long id);

	/**
	 * 生成快照订单
	 *
	 * @param accountPO
	 * @param bookPO
	 * @return
	 */
	CommonResponseParams buy(AccountPO accountPO, BookPO bookPO);

	/**
	 * 订单审核
	 *
	 * @param id
	 * @param approvedId
	 * @return
	 */
	public CommonResponseParams examine(Long id, Long approvedId);

	/**
	 * 根据用户Id查询
	 *
	 * @param accountId
	 * @return
	 */
	List<ContractPO> findAll(Long accountId);

	Long count();

	Long countByStatus(String code);

}
