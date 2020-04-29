package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.ShelfPO;
import java.util.List;

public interface ShelfDao extends BaseDao<ShelfPO, Long> {

	ShelfPO getShelfByBookId(final Long bookId);

	ShelfPO getShelfByBookIdAndAccountId(final Long bookId, final Long accountId);

	List<ShelfPO> listShelfByAccountId(final Long accountId);
}
