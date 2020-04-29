package com.yosamaru.kassadin.web.controller;

import com.google.common.collect.Lists;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.BasePO.Status;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO.TransactionType;
import com.yosamaru.kassadin.entity.PO.ChequePO;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.service.CashAccountTransactionService;
import com.yosamaru.kassadin.service.ChequeService;
import com.yosamaru.kassadin.service.ModeratorService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(value = "/cheque")
public class ChequeController {
	@Resource
	private ChequeService chequeService;

	@Resource
	private AccountService accountService;

	@Resource
	private ModeratorService moderatorService;

	@Resource
	private CashAccountTransactionService cashAccountTransactionService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(HttpServletRequest request, @RequestBody ChequePO chequePO) {
		if (StringUtils.isBlank(chequePO.getAccountName())) {
			return CommonResponseParams.ofFailure("用户名不能为空");
		}

		ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
		if (moderatorUser == null) {
			return CommonResponseParams.ofFailure("请登录管理员账户进行充值");
		}

		chequePO.setStatus(Status.FREEZE.toString());
		AccountPO accountPO = accountService.findByAccountName(chequePO.getAccountName());
		chequePO.setAccountId(accountPO.getId());

		ChequePO oldChequePO = chequeService.getChequeByAccountId(accountPO.getId());
		if (oldChequePO != null) {
			BigDecimal countMoney = oldChequePO.getBalance().add(chequePO.getBalance());
			oldChequePO.setBalance(countMoney);
			oldChequePO.setApprovedId(moderatorUser.getId());
			oldChequePO.setApprovedDate(new Date());
			oldChequePO.setStatus(Status.THAW.toString());
			chequeService.save(oldChequePO);
		} else {
			chequePO.setApprovedId(moderatorUser.getId());
			chequePO.setApprovedDate(new Date());
			chequePO.setStatus(Status.THAW.toString());
			chequeService.save(chequePO);
		}
		addTraction(accountPO.getId(), chequePO.getBalance());
		return CommonResponseParams.ofSuccessful();
	}


	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public CommonResponseParams edit(@RequestBody ChequePO chequePO) {
		chequeService.save(chequePO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		ChequePO chequePO = new ChequePO();
		chequePO.setId(id);
		chequeService.delete(chequePO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
			AccountPO concurrentUser = (AccountPO) request.getSession().getAttribute("concurrentUser");
			Long userId = null;
			if (moderatorUser != null) {
				List<ChequePO> chequePOS = chequeService.findAll();
				return CommonResponseParams.ofSuccessful(chequePOS);
			} else if (concurrentUser != null) {
				userId = concurrentUser.getId();
				ChequePO chequePO = chequeService.getChequeByAccountId(userId);
				ArrayList<Object> chequePOs = Lists.newArrayList();
				chequePOs.add(chequePO);
				return CommonResponseParams.ofSuccessful(chequePOs);
			} else {
				throw new Exception("无用户信息");
			}
		} catch (Exception e) {
			return CommonResponseParams.ofFailure(e.getMessage());
		}
	}

	private void addTraction(Long userId, BigDecimal amount) {
		CashAccountTransactionPO cashAccountTransactionPO = new CashAccountTransactionPO();
		cashAccountTransactionPO.setUserId(userId);
		cashAccountTransactionPO.setTransactionAmount(amount);
		cashAccountTransactionPO.setTransactionType(TransactionType.RECHARGE.toString());
		cashAccountTransactionService.save(cashAccountTransactionPO);
	}
}
