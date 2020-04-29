package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.entity.PO.ShelfPO;
import com.yosamaru.kassadin.service.ShelfService;
import com.yosamaru.kassadin.service.impl.FileService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(value = "/shelf")
public class ShelfController {
	Logger log = LoggerFactory.getLogger(ShelfController.class);

	@Autowired
	private ShelfService shelfService;

	@Autowired
	private FileService fileService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(@RequestBody ShelfPO shelfBean) {
		shelfService.save(shelfBean);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public CommonResponseParams edit(ShelfPO shelfBean) {
		shelfService.save(shelfBean);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		ShelfPO shelfBean = new ShelfPO();
		shelfBean.setId(id);
		shelfService.delete(shelfBean);
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
				userId = moderatorUser.getId();
			} else if (concurrentUser != null) {
				userId = concurrentUser.getId();
			} else {
				throw new Exception("无用户信息");
			}

			List<ShelfPO> shelfBeans = shelfService.findAll(userId);
			return CommonResponseParams.ofSuccessful(shelfBeans);
		} catch (Exception e) {
			return CommonResponseParams.ofFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "read-book", method = RequestMethod.GET)
	public CommonResponseParams readBook(Long id) {
		return shelfService.reader(id);
	}

	@RequestMapping(value = "download-file", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName) throws Exception {
		if (StringUtils.isBlank(fileName)) {
			return;
		}
		fileService.download(response, fileName);
	}
}
