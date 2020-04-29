package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.util.CommonResponseParams;

public interface AccountService extends BaseService<AccountPO, Long> {

	/**
	 * 注册
	 */
	CommonResponseParams register(AccountPO accountPO);

	/**
	 * 登陆
	 *
	 * @param accountPO
	 */
	CommonResponseParams login(AccountPO accountPO);


	/**
	 * 退出
	 *
	 * @param accountPO
	 */
	CommonResponseParams loginOut(AccountPO accountPO);


	/**
	 * 解冻用户（审核用户）
	 *
	 * @param accountPO
	 * @param moderatorId
	 * @return
	 */
	CommonResponseParams frozen(AccountPO accountPO, Long moderatorId);

	/**
	 * 通过姓名查询
	 * @param accountName
	 * @return
	 */
	AccountPO findByAccountName(String accountName);
}
