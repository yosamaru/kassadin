package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.AuthorDao;
import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.entity.PO.AuthorPO;
import com.yosamaru.kassadin.service.AuthorService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceAbstract extends AbstractBaseService<AuthorPO, Long> implements AuthorService {

	@Resource
	private AuthorDao authorDao;

	@Override
	public BaseDao<AuthorPO, Long> getDAO() {
		return authorDao;
	}

	@Override
	public List<AuthorPO> findLikeName(final String authorName) {
		return authorDao.listAuthorByLikeName(authorName);
	}
}
