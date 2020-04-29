package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.ShelfDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.ShelfPO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ShelfDaoImpl extends AbstractBaseDao<ShelfPO, Long> implements ShelfDao {
	@Override
	public ShelfPO getShelfByBookId(Long bookId) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(ShelfPO.class);
		genericCriteria.where(genericCriteria.eq("bookId", bookId));
		return (ShelfPO) genericCriteria.createQuery().getSingleResult();
	}

	@Override
	public ShelfPO getShelfByBookIdAndAccountId(Long bookId, Long accountId) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(ShelfPO.class);
		genericCriteria.where(genericCriteria.eq("bookId", bookId));
		genericCriteria.where(genericCriteria.eq("accountId", accountId));
		return (ShelfPO) genericCriteria.createQuery().getSingleResult();
	}

	@Override
	public List<ShelfPO> listShelfByAccountId(Long accountId) {
		final GenericCriteria genericCriteria = this.getGenericCriteria(ShelfPO.class);
		genericCriteria.where(genericCriteria.eq("accountId", accountId));
		return genericCriteria.createQuery().getResultList();
	}
}
