package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.ContractPO;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.service.ContractService;
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
@RequestMapping(value = "/contract")
public class ContractController {
	@Resource
	private ContractService contractService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(@RequestBody ContractPO contractPO) {
		contractService.save(contractPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/audit", method = RequestMethod.GET)
	public CommonResponseParams examine(HttpServletRequest request, Long id) {
		ModeratorPO moderatorPO = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
		if (moderatorPO == null) {
			return CommonResponseParams.ofFailure("请登录管理员账号操作");
		}
		return contractService.examine(id, moderatorPO.getId());
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		ContractPO contractPO = new ContractPO();
		contractPO.setId(id);
		contractService.delete(contractPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
			AccountPO concurrentUser = (AccountPO) request.getSession().getAttribute("concurrentUser");
			List<ContractPO> contractPOS = null;
			Long userId = null;
			if (moderatorUser != null) {
				contractPOS = contractService.findAll();
			} else if (concurrentUser != null) {
				userId = concurrentUser.getId();
				contractPOS = contractService.findAll(userId);
			} else {
				throw new Exception("无用户信息");
			}
			return CommonResponseParams.ofSuccessful(contractPOS);
		} catch (Exception e) {
			return CommonResponseParams.ofFailure(e.getMessage());
		}
	}
}
