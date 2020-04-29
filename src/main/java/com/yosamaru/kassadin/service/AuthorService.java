package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.AuthorPO;
import java.util.List;

/**
 * 作者service
 */
public interface AuthorService extends BaseService<AuthorPO, Long> {

	List<AuthorPO> findLikeName(String authorName);
}
