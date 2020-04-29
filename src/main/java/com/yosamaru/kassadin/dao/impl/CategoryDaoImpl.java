package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.CategoryDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.CategoryPO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDaoImpl extends AbstractBaseDao<CategoryPO, Long> implements CategoryDao {
	@Override
	public List<CategoryPO> listByLikeName(String categoryName) {
		GenericCriteria genericCriteria = this.getGenericCriteria(CategoryPO.class);
		genericCriteria.where(genericCriteria.like("categoryName", categoryName));
		return genericCriteria.createQuery().getResultList();
	}
}
