package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.PublisherDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.PublisherPO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PublisherDaoImpl extends AbstractBaseDao<PublisherPO, Long> implements PublisherDao {
	@Override
	public List<PublisherPO> listPublisherByLikeName(String publisherName) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(PublisherPO.class);
		genericCriteria.where(genericCriteria.eq("publisherName", publisherName));
		return genericCriteria.createQuery().getResultList();
	}
}
