package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO.TransactionType;
import com.yosamaru.kassadin.entity.PO.ContractPO.ContractStatus;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.entity.VO.StatisticsVO;
import com.yosamaru.kassadin.service.CashAccountTransactionService;
import com.yosamaru.kassadin.service.ContractService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(value = "/check")
public class CheckLoginController {

	@Resource
	private ContractService contractService;

	@Resource
	private CashAccountTransactionService cashAccountTransactionService;

	@RequestMapping(value = "/is-login", method = RequestMethod.GET)
	public CommonResponseParams isLogin(HttpServletRequest request) {
		ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");

		AccountPO concurrentUser = (AccountPO) request.getSession().getAttribute("concurrentUser");
		if (moderatorUser == null && concurrentUser == null) {
			return CommonResponseParams.ofFailure("用户未登录");
		}

		if (concurrentUser != null) {
			return CommonResponseParams.ofSuccessful(0);
		}


		if (moderatorUser != null) {
			return CommonResponseParams.ofSuccessful(1);
		}

		return CommonResponseParams.ofFailure("用户未登录");
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public CommonResponseParams statistics() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");

		AccountPO concurrentUser = (AccountPO) request.getSession().getAttribute("concurrentUser");
		if (moderatorUser == null && concurrentUser == null) {
			return CommonResponseParams.ofFailure("用户未登录");
		}

		if (concurrentUser != null) {
			return CommonResponseParams.ofFailure("请登录管理员账号");
		}

		Long count = contractService.count();

		Long sorder = contractService.countByStatus(ContractStatus.SUCCESSS.toString());

		Long worder = contractService.countByStatus(ContractStatus.WAIT_APPROVED.toString());

		Long forder = contractService.countByStatus(ContractStatus.FAIL.toString());

		Long aSum = cashAccountTransactionService.count();

		BigDecimal aAmount = cashAccountTransactionService.sumAmoutByStatus(TransactionType.RECHARGE.toString());
		if (aAmount == null) {
			aAmount = BigDecimal.ZERO;
		}

		BigDecimal dAmount = cashAccountTransactionService.sumAmoutByStatus(TransactionType.ORDER.toString());

		if (dAmount == null) {
			dAmount = BigDecimal.ZERO;
		}

		StatisticsVO statisticsVO = new StatisticsVO(count, worder, sorder, forder, aSum, dAmount, aAmount);

		return CommonResponseParams.ofSuccessful(statisticsVO);
	}


}
