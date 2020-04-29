package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.service.CashAccountTransactionService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(value = "/transaction")
public class CashAccountTransactionController {
	@Resource
	private CashAccountTransactionService cashAccountTransactionService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(@RequestBody CashAccountTransactionPO cashAccountTransactionPO) {
		cashAccountTransactionService.save(cashAccountTransactionPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public CommonResponseParams edit(@RequestBody CashAccountTransactionPO cashAccountTransactionPO) {
		cashAccountTransactionService.save(cashAccountTransactionPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		CashAccountTransactionPO cashAccountTransactionPO = new CashAccountTransactionPO();
		cashAccountTransactionPO.setId(id);
		cashAccountTransactionService.delete(cashAccountTransactionPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
			AccountPO concurrentUser = (AccountPO) request.getSession().getAttribute("concurrentUser");
			Long userId = null;
			List<CashAccountTransactionPO> cashAccountTransactionPOS = null;
			if (moderatorUser != null) {
				cashAccountTransactionPOS = cashAccountTransactionService.findAll();
			} else if (concurrentUser != null) {
				userId = concurrentUser.getId();
				cashAccountTransactionPOS = cashAccountTransactionService.findAll(userId);
			} else {
				throw new Exception("无用户信息");
			}
			return CommonResponseParams.ofSuccessful(cashAccountTransactionPOS);
		} catch (Exception e) {
			return CommonResponseParams.ofFailure(e.getMessage());
		}
	}
}
