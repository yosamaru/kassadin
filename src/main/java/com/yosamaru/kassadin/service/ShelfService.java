package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.ShelfPO;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;

public interface ShelfService extends BaseService<ShelfPO, Long> {
	/**
	 * 查询详情信息
	 *
	 * @param id
	 * @return
	 */
	public ShelfPO findInfo(Long id);

	ShelfPO findByBookId(Long bookId);

	CommonResponseParams findByBookIdAndUserId(Long bookId, Long userId);

	ShelfPO findByBookInfoIdAndUserId(Long bookId, Long userId);

	/**
	 * 根据用户Id查询
	 *
	 * @param userId
	 * @return
	 */
	List<ShelfPO> findAll(Long userId);

	/**
	 * 阅读
	 *
	 * @param id
	 * @return
	 */
	CommonResponseParams reader(Long id);
}
