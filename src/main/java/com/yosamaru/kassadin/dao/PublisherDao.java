package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.PublisherPO;
import java.util.List;

public interface PublisherDao extends BaseDao<PublisherPO, Long> {
	List<PublisherPO> listPublisherByLikeName(final String publisherName);
}
