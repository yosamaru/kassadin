package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.AuthorPO;
import com.yosamaru.kassadin.service.AuthorService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * zuozhe
 */
@RestController
@RequestMapping(value = "/author")
public class AuthorController {
	@Resource
	private AuthorService authorService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(@RequestBody AuthorPO authorPO) {
		authorService.save(authorPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public CommonResponseParams edit(AuthorPO AuthorPO) {
		authorService.save(AuthorPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		AuthorPO authorPO = new AuthorPO();
		authorPO.setId(id);
		authorService.delete(authorPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		List<AuthorPO> authorPOS = authorService.findAll();
		return CommonResponseParams.ofSuccessful(authorPOS);
	}
}
