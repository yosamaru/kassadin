package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.BookPO;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.List;

public interface BookService extends BaseService<BookPO, Long> {
	/**
	 * 查询书籍的详情信息
	 *
	 * @param id
	 * @return
	 */
	public BookPO findInfo(Long id);

	CommonResponseParams buyBook(AccountPO accountPO, Long id) throws Exception;

	CommonResponseParams addBook(BookPO bookPO);

	List<BookPO> likeSearch(String subject, List<Long> authorId, List<Long> categoryId,
			List<Long> publisherId);

//	CommonResponseParams mBuyBook(ModeratorPO moderatorBean, Long id) throws Exception;
}
