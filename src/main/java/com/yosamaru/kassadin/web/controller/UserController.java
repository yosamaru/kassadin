package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import com.yosamaru.kassadin.util.ReturnCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("user")
public class UserController {

	@Resource
	private AccountService accountService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public CommonResponseParams register(@RequestBody AccountPO accountPO) {
		return accountService.register(accountPO);
	}

	@RequestMapping(value = "find-all")
	public CommonResponseParams findAll() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
		AccountPO concurrentUser = (AccountPO) request.getSession().getAttribute("concurrentUser");
		List<AccountPO> all = null;
		if (moderatorUser != null) {
			all = accountService.findAll();
		} else if (concurrentUser != null) {
			AccountPO userPO = accountService.findById(concurrentUser.getId());
			all = new ArrayList<AccountPO>();
			all.add(userPO);
		}
		return CommonResponseParams.ofSuccessful(all);
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public CommonResponseParams login(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) throws IOException {
		AccountPO userPO = new AccountPO();
		userPO.setAccountName(username);
		userPO.setPassword(password);

		if (username != null && password != null) {
			CommonResponseParams commonResponseParams = this.accountService.login(userPO);
			if (commonResponseParams.getErrCode().equals(ReturnCode.OK.getCode())) {
				AccountPO loginUser = accountService.findByAccountName(userPO.getAccountName());
				ModeratorPO moderatorUser = (ModeratorPO) request.getSession().getAttribute("moderatorUser");
				if (moderatorUser != null) {
					request.getSession().removeAttribute("moderatorUser");
				}
				//保存当前登录信息到session
				request.getSession().setAttribute("concurrentUser", loginUser);
			}
			return commonResponseParams;
		}
		return CommonResponseParams.ofFailure();
	}

	@RequestMapping(value = "/login-out", method = RequestMethod.GET)
	public void loginOut(HttpServletRequest request) {
		request.getSession().removeAttribute("concurrentUser");
	}
}
