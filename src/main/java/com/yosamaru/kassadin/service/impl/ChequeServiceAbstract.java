package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.ChequeDao;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.BasePO.Status;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO.TransactionType;
import com.yosamaru.kassadin.entity.PO.ChequePO;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.service.CashAccountTransactionService;
import com.yosamaru.kassadin.service.ChequeService;
import com.yosamaru.kassadin.service.ModeratorService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChequeServiceAbstract extends AbstractBaseService<ChequePO, Long> implements ChequeService {

	@Resource
	private ChequeDao chequeDao;

	@Resource
	private AccountService accountService;

	@Resource
	private CashAccountTransactionService cashAccountTransactionService;

	@Resource
	private ModeratorService moderatorService;

	@Override
	public BaseDao<ChequePO, Long> getDAO() {
		return chequeDao;
	}

	@Override
	public ChequePO findInfo(final Long id) {
		ChequePO chequePO = this.findById(id);
		if (chequePO.getAccountId() != null) {
			AccountPO accountPO = accountService.findById(chequePO.getAccountId());
			chequePO.setAccountPO(accountPO);
		}
		return chequePO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public CommonResponseParams recharge(final Long accountId, final BigDecimal amount) {
		AccountPO accountPO = accountService.findById(accountId);
		if (accountPO == null) {
			return CommonResponseParams.ofFailure("无此用户");
		}
		ChequePO chequePO = chequeDao.getChequeByAccountId(accountId);
		if (chequePO == null) {
			chequePO = new ChequePO();
			chequePO.setAccountId(accountId);
			chequePO.setBalance(amount);
			chequePO.setStatus(Status.THAW.toString());
		} else {
			if (Status.FREEZE.toString().equals(chequePO.getStatus())) {
				return CommonResponseParams.ofFailure("钱包账户被冻结");
			}
			chequePO.setAccountId(accountId);
			BigDecimal add = chequePO.getBalance().add(amount);
			chequePO.setBalance(add);
		}
		this.savaTransaction(chequePO, amount);
		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public List<ChequePO> findAll() {
		List<ChequePO> chequePOS = chequeDao.findAll();
		for (Iterator<ChequePO> beanIterator = chequePOS.iterator(); beanIterator.hasNext(); ) {
			ChequePO chequePO = beanIterator.next();
			AccountPO accountPO = accountService.findById(chequePO.getAccountId());
			ModeratorPO moderatorPO = moderatorService.findById(chequePO.getApprovedId());

			chequePO.setAccountName(accountPO.getAccountName());
			chequePO.setModeratorName(moderatorPO.getModeratorName());
		}
		return chequePOS;
	}

	@Override
	public ChequePO getChequeByAccountId(final Long userId) {
		return chequeDao.getChequeByAccountId(userId);
	}

	/**
	 * 保存充值明细
	 *
	 * @param chequePO
	 * @param amount
	 */
	private void savaTransaction(final ChequePO chequePO, final BigDecimal amount) {
		CashAccountTransactionPO cashAccountTransactionPO = new CashAccountTransactionPO();
		cashAccountTransactionPO.setUserId(chequePO.getAccountId());
		cashAccountTransactionPO.setTransactionAmount(amount);
		cashAccountTransactionPO.setTransactionType(TransactionType.RECHARGE.toString());
		cashAccountTransactionService.save(cashAccountTransactionPO);
	}
}
