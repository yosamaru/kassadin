package com.yosamaru.kassadin.web.controller;

import com.yosamaru.kassadin.entity.PO.PublisherPO;
import com.yosamaru.kassadin.service.PublisherService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/publisher")
public class PublisherController {
	@Resource
	private PublisherService publisherService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public CommonResponseParams create(@RequestBody PublisherPO publisherPO) {
		publisherService.save(publisherPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public CommonResponseParams edit(PublisherPO publisherPO) {
		publisherService.save(publisherPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public CommonResponseParams delete(Long id) {
		PublisherPO publisherPO = new PublisherPO();
		publisherPO.setId(id);
		publisherService.delete(publisherPO);
		return CommonResponseParams.ofSuccessful();
	}

	@RequestMapping(value = "/find-all", method = RequestMethod.GET)
	public CommonResponseParams findAll() {
		List<PublisherPO> publisherPOS = publisherService.findAll();
		return CommonResponseParams.ofSuccessful(publisherPOS);
	}
}
