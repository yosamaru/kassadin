package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.PublisherDao;
import com.yosamaru.kassadin.entity.PO.PublisherPO;
import com.yosamaru.kassadin.service.PublisherService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PublisherServiceAbstract extends AbstractBaseService<PublisherPO, Long> implements PublisherService {

	@Resource
	private PublisherDao publisherDao;

	@Override
	public BaseDao<PublisherPO, Long> getDAO() {
		return publisherDao;
	}

	@Override
	public List<PublisherPO> findLikeName(final String publisher) {
		return publisherDao.listPublisherByLikeName(publisher);
	}
}
