package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.CategoryPO;
import com.yosamaru.kassadin.service.CategoryService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {
	@Resource
	private CategoryService categoryService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(@RequestBody CategoryPO categoryPO) {
		categoryService.save(categoryPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public CommonResponseParams edit(CategoryPO categoryPO) {
		categoryService.save(categoryPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		CategoryPO categoryPO = new CategoryPO();
		categoryPO.setId(id);
		categoryService.delete(categoryPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		List<CategoryPO> categoryPOS = categoryService.findAll();
		return CommonResponseParams.ofSuccessful(categoryPOS);
	}
}
