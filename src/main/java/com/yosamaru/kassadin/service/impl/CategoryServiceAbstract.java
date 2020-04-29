package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.CategoryDao;
import com.yosamaru.kassadin.entity.PO.CategoryPO;
import com.yosamaru.kassadin.service.CategoryService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceAbstract extends AbstractBaseService<CategoryPO, Long> implements CategoryService {

	@Resource
	private CategoryDao categoryDao;

	@Override
	public BaseDao<CategoryPO, Long> getDAO() {
		return categoryDao;
	}

	@Override
	public List<CategoryPO> findLikeName(final String categoryName) {
		return categoryDao.listByLikeName(categoryName);
	}
}
