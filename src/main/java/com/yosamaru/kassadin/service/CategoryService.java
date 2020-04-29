package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.CategoryPO;
import java.util.List;

public interface CategoryService extends BaseService<CategoryPO, Long> {

	List<CategoryPO> findLikeName(String categoryName);
}
