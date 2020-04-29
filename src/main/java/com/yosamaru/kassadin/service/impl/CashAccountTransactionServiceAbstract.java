package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.CashAccountTransactionDao;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.service.CashAccountTransactionService;
import com.yosamaru.kassadin.service.ModeratorService;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CashAccountTransactionServiceAbstract extends AbstractBaseService<CashAccountTransactionPO, Long> implements CashAccountTransactionService {

	@Resource
	private CashAccountTransactionDao cashAccountTransactionDao;
	@Resource
	private AccountService accountService;

	@Resource
	private ModeratorService moderatorService;

	@Override
	public BaseDao<CashAccountTransactionPO, Long> getDAO() {
		return cashAccountTransactionDao;
	}

	@Override
	public CashAccountTransactionPO findInfo(final Long id) {
		CashAccountTransactionPO cashAccountTransactionPO = this.findById(id);
		if (cashAccountTransactionPO.getUserId() != null) {
			cashAccountTransactionPO.setAccountPO(accountService.findById(cashAccountTransactionPO.getUserId()));
		}
		return cashAccountTransactionPO;
	}

	@Override
	public List<CashAccountTransactionPO> findAll(final Long accountId) {
		List<CashAccountTransactionPO> cashAccountTransactionPOS = cashAccountTransactionDao.listByAccountId(accountId);
		for (Iterator<CashAccountTransactionPO> beanIterator = cashAccountTransactionPOS.iterator(); beanIterator.hasNext(); ) {
			CashAccountTransactionPO cashAccountTransactionPO = beanIterator.next();

			AccountPO accountPO = accountService.findById(cashAccountTransactionPO.getUserId());
			cashAccountTransactionPO.setUserName(accountPO.getAccountName());
		}
		return cashAccountTransactionPOS;
	}

	@Override
	public BigDecimal sumAmoutByStatus(final String code) {
		return cashAccountTransactionDao.sumAmountByType(code);
	}

	@Override
	public Long count() {
		return cashAccountTransactionDao.countAll();
	}

	@Override
	public List<CashAccountTransactionPO> findAll() {
		List<CashAccountTransactionPO> cashAccountTransactionPOS = super.findAll();
		for (Iterator<CashAccountTransactionPO> beanIterator = cashAccountTransactionPOS.iterator(); beanIterator.hasNext(); ) {
			CashAccountTransactionPO cashAccountTransactionPO = beanIterator.next();
			AccountPO accountPO = accountService.findById(cashAccountTransactionPO.getUserId());
			cashAccountTransactionPO.setUserName(accountPO.getAccountName());
		}
		return cashAccountTransactionPOS;
	}
}
