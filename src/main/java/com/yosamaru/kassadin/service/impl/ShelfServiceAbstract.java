package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.ShelfDao;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.BookPO;
import com.yosamaru.kassadin.entity.PO.ShelfPO;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.service.BookService;
import com.yosamaru.kassadin.service.ModeratorService;
import com.yosamaru.kassadin.service.ShelfService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ShelfServiceAbstract extends AbstractBaseService<ShelfPO, Long> implements ShelfService {

	@Resource
	private ShelfDao shelfDao;

	@Resource
	private BookService bookService;

	@Resource
	private AccountService accountService;

	@Resource
	private ModeratorService moderatorService;

	@Override
	public BaseDao<ShelfPO, Long> getDAO() {
		return shelfDao;
	}

	@Override
	public ShelfPO findInfo(Long id) {
		ShelfPO shelfBean = this.findById(id);
		if (shelfBean.getAccountId() != null) {
			AccountPO accountPO = accountService.findById(shelfBean.getAccountId());
			shelfBean.setUserPO(accountPO);
		}

		if (shelfBean.getBookId() != null) {
			BookPO bookPO = bookService.findById(shelfBean.getBookId());
			shelfBean.setBookPO(bookPO);
		}

		return shelfBean;
	}

	@Override
	public ShelfPO findByBookId(final Long bookId) {
		ShelfPO shelfBean = shelfDao.getShelfByBookId(bookId);
		return shelfBean;
	}

	@Override
	public CommonResponseParams findByBookIdAndUserId(final Long bookId, final Long accountId) {
		AccountPO accountPO = accountService.findById(accountId);
		if (accountPO == null) return CommonResponseParams.ofFailure("无此用户");

		ShelfPO ShelfPO = shelfDao.getShelfByBookIdAndAccountId(bookId, accountId);
		if (ShelfPO == null) return CommonResponseParams.ofFailure("你还未购买次书籍");

		// 修改书籍阅览信息
		ShelfPO.setLastAccessDate(new Date());
		ShelfPO.setNumberOfAccesses(ShelfPO.getNumberOfAccesses() + 1);

		BookPO bookPO = bookService.findInfo(bookId);
		return CommonResponseParams.ofSuccessful(bookPO);
	}

	@Override
	public ShelfPO findByBookInfoIdAndUserId(final Long bookId, final Long userId) {
		ShelfPO shelfBean = shelfDao.getShelfByBookIdAndAccountId(bookId, userId);
		return shelfBean;
	}

	@Override
	public List<ShelfPO> findAll(final Long accountId) {
		List<ShelfPO> shelfBeans = shelfDao.listShelfByAccountId(accountId);
		for (Iterator<ShelfPO> beanIterator = shelfBeans.iterator(); beanIterator.hasNext(); ) {
			ShelfPO shelfPO = beanIterator.next();
			AccountPO accountPO = accountService.findById(shelfPO.getAccountId());
			BookPO bookPO = bookService.findById(shelfPO.getBookId());
			shelfPO.setUserName(accountPO.getAccountName());
			shelfPO.setBookSubject(bookPO.getSubject());
		}
		return shelfBeans;
	}

	@Override
	public CommonResponseParams reader(final Long id) {
		ShelfPO shelfBean = this.findById(id);
		if (shelfBean == null) return CommonResponseParams.ofFailure("你还未购买次书籍");

		// 修改书籍阅览信息
		shelfBean.setLastAccessDate(new Date());
		shelfBean.setNumberOfAccesses(shelfBean.getNumberOfAccesses() + 1);

		this.save(shelfBean);

		BookPO bookPO = bookService.findInfo(shelfBean.getBookId());
		return CommonResponseParams.ofSuccessful(bookPO);
	}


	@Override
	public List<ShelfPO> findAll() {
		List<ShelfPO> shelfBeans = super.findAll();
		for (Iterator<ShelfPO> beanIterator = shelfBeans.iterator(); beanIterator.hasNext(); ) {
			ShelfPO shelfBean = beanIterator.next();
			AccountPO accountPO = accountService.findById(shelfBean.getAccountId());
			BookPO bookPO = bookService.findById(shelfBean.getBookId());

			shelfBean.setUserName(accountPO.getAccountName());
			shelfBean.setBookSubject(bookPO.getSubject());
		}
		return shelfBeans;
	}
}
