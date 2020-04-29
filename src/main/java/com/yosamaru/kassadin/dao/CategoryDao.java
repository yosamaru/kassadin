package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.CategoryPO;
import java.util.List;

public interface CategoryDao extends BaseDao<CategoryPO, Long> {
	List<CategoryPO> listByLikeName(final String categoryName);
}
