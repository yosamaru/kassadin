package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.PublisherPO;
import java.util.List;

public interface PublisherService extends BaseService<PublisherPO, Long> {

	List<PublisherPO> findLikeName(String publisher);
}
