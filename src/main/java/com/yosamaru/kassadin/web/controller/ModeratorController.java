package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.service.ModeratorService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import com.yosamaru.kassadin.util.ReturnCode;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/moderator")
public class ModeratorController {
	@Resource
	private ModeratorService moderatorService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public CommonResponseParams register(@RequestBody ModeratorPO moderatorPO) {
		moderatorService.register(moderatorPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public CommonResponseParams login(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) throws IOException {
		ModeratorPO moderatorPO = new ModeratorPO();
		moderatorPO.setModeratorName(username);
		moderatorPO.setPassword(password);

		if (username != null && password != null) {
			CommonResponseParams commonResponseParams = this.moderatorService.login(moderatorPO);
			if (commonResponseParams.getErrCode().equals(ReturnCode.OK.getCode())) {
				ModeratorPO loginModerator = moderatorService.findByName(moderatorPO.getModeratorName());

				AccountPO userPO = (AccountPO) request.getSession().getAttribute("concurrentUser");
				if (userPO != null) {
					request.getSession().removeAttribute("concurrentUser");
				}
				//保存当前登录信息到session
				request.getSession().setAttribute("moderatorUser", loginModerator);
			}
			return commonResponseParams;
		}
		return CommonResponseParams.ofFailure();
	}

	@RequestMapping(value = "/login-out", method = RequestMethod.GET)
	public void loginOut(HttpServletRequest request) {
		request.getSession().removeAttribute("moderatorUser");
	}


	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		List<ModeratorPO> moderatorPOS = moderatorService.findAll();
		return CommonResponseParams.ofSuccessful(moderatorPOS);
	}
}
