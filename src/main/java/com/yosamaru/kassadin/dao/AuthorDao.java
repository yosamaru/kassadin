package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.AuthorPO;
import java.util.List;

public interface AuthorDao extends BaseDao<AuthorPO, Long> {
	/**
	 * 根据姓名模糊查询
	 * @param authorName
	 * @return
	 */
	List<AuthorPO> listAuthorByLikeName(final String authorName);
}
