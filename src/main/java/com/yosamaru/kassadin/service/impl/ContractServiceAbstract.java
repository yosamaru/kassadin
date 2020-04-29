package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.ContractDao;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.BasePO.Status;
import com.yosamaru.kassadin.entity.PO.BookPO;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO;
import com.yosamaru.kassadin.entity.PO.CashAccountTransactionPO.TransactionType;
import com.yosamaru.kassadin.entity.PO.ChequePO;
import com.yosamaru.kassadin.entity.PO.ContractPO;
import com.yosamaru.kassadin.entity.PO.ContractPO.ContractStatus;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.entity.PO.ShelfPO;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.service.BookService;
import com.yosamaru.kassadin.service.CashAccountTransactionService;
import com.yosamaru.kassadin.service.ChequeService;
import com.yosamaru.kassadin.service.ContractService;
import com.yosamaru.kassadin.service.ModeratorService;
import com.yosamaru.kassadin.service.ShelfService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractServiceAbstract extends AbstractBaseService<ContractPO, Long> implements ContractService {

	@Resource
	private ContractDao contractDao;

	@Resource
	private AccountService accountService;

	@Resource
	private ChequeService chequeService;

	@Resource
	private BookService bookService;

	@Resource
	private ShelfService shelfService;

	@Resource
	private ModeratorService moderatorService;

	@Resource
	private CashAccountTransactionService cashAccountTransactionService;

	@Override
	public BaseDao<ContractPO, Long> getDAO() {
		return contractDao;
	}

	@Override
	public ContractPO findInfo(final Long id) {
		ContractPO contractPO = this.findById(id);
		if (contractPO.getAccountId() != null) {
			AccountPO accountPO = accountService.findById(contractPO.getAccountId());
			contractPO.setAccountPO(accountPO);
		}

		return contractPO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public CommonResponseParams buy(final AccountPO accountPO, final BookPO bookPO) {
		//1.查找支票账户
		ChequePO chequePO = chequeService.findById(accountPO.getId());
		if (chequePO == null) {
			return CommonResponseParams.ofFailure("用户未开户");
		}
		//2查看账户状态
		if (Status.FREEZE.toString().equals(chequePO.getStatus())) {
			return CommonResponseParams.ofFailure("用户账户处于冻结状态");
		}

		//3.判断金额是否足够支付书籍
		if (chequePO.getBalance().compareTo(bookPO.getPrice()) < 0) {
			return CommonResponseParams.ofFailure("用户账户余额不足");
		}
		//4.保存订单
		ContractPO contractPO = new ContractPO();
		contractPO.setAccountId(accountPO.getId());
		contractPO.setBookId(bookPO.getId());
		contractPO.setContractStatus(ContractStatus.WAIT_APPROVED.toString());
		this.save(contractPO);

		return CommonResponseParams.ofSuccessful();
	}

	@Override
	@Transactional
	public CommonResponseParams examine(final Long id, final Long approvedId) {
		ModeratorPO approvedBean = moderatorService.findById(approvedId);
		if (approvedBean == null) return CommonResponseParams.ofFailure("无此用户");

		//1.查询订单
		ContractPO contractPO = this.findById(id);
		//2.查找支票账户
		ChequePO chequePO = chequeService.findById(contractPO.getAccountId());
		if (chequePO == null) return CommonResponseParams.ofFailure("用户未开户");

		//3查看账户状态
		if (Status.FREEZE.toString().equals(chequePO.getStatus())) {
			return CommonResponseParams.ofFailure("用户账户处于冻结状态");
		}
		//4.查看此书是否已经购买
		ShelfPO oldShelfBean = shelfService.findByBookId(id);
		if (oldShelfBean != null) return CommonResponseParams.ofFailure("此书籍已经购买");

		//5.查询书籍信息
		BookPO bookPO = bookService.findById(contractPO.getBookId());
		if (bookPO == null) return CommonResponseParams.ofFailure("书籍已卖完或已下架");

		//6.判断金额是否足够支付书籍
		if (chequePO.getBalance().compareTo(bookPO.getPrice()) < 0)
			return CommonResponseParams.ofFailure("用户账户余额不足");

		//7.扣减金额
		BigDecimal newAmount = chequePO.getBalance().subtract(bookPO.getPrice());
		chequePO.setBalance(newAmount);
		//8.更新账户
		chequeService.save(chequePO);
		//9.生成支付明细
		CashAccountTransactionPO cashAccountTransactionPO = new CashAccountTransactionPO();
		cashAccountTransactionPO.setUserId(contractPO.getAccountId());
		cashAccountTransactionPO.setTransactionAmount(bookPO.getPrice());
		cashAccountTransactionPO.setTransactionType(TransactionType.ORDER.toString());
		cashAccountTransactionService.save(cashAccountTransactionPO);

		//10.修改订单
		contractPO.setApprovedId(approvedId);
		contractPO.setCashAccountTransactionId(cashAccountTransactionPO.getId());
		contractPO.setContractStatus(ContractStatus.SUCCESSS.toString()); //修改为成功
		contractPO.setVoidedDate(new Date());
		this.save(contractPO);

		//11.书架添加购买的书籍
		ShelfPO shelfPO = new ShelfPO();
		shelfPO.setBookId(bookPO.getId());
		shelfPO.setAccountId(contractPO.getAccountId());
		shelfPO.setNumberOfAccesses(0);
		shelfService.save(shelfPO);
		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public List<ContractPO> findAll(final Long accountId) {
		List<ContractPO> contractPOS = contractDao.listByAccountId(accountId);
		for (Iterator<ContractPO> beanIterator = contractPOS.iterator(); beanIterator.hasNext(); ) {
			ContractPO contractPO = beanIterator.next();
			AccountPO accountPO = accountService.findById(contractPO.getAccountId());
			BookPO bookPO = bookService.findById(contractPO.getBookId());

			contractPO.setUserName(accountPO.getAccountName());
			contractPO.setBookSubject(bookPO.getSubject());
			if (contractPO.getApprovedId() != null) {
				ModeratorPO moderatorPO = moderatorService.findById(contractPO.getApprovedId());
				if (moderatorPO != null) {
					contractPO.setModeratorName(moderatorPO.getModeratorName());
				}
			}
		}
		return contractPOS;
	}

	@Override
	public Long countByStatus(final String code) {
		return contractDao.countByTransactionStatus(code);
	}

	@Override
	public Long count() {
		return contractDao.countAll();
	}


	@Override
	public List<ContractPO> findAll() {
		List<ContractPO> contractPOS = super.findAll();
		for (Iterator<ContractPO> beanIterator = contractPOS.iterator(); beanIterator.hasNext(); ) {
			ContractPO contractPO = beanIterator.next();
			AccountPO accountPO = accountService.findById(contractPO.getAccountId());
			BookPO bookPO = bookService.findById(contractPO.getBookId());

			contractPO.setUserName(accountPO.getAccountName());
			contractPO.setBookSubject(bookPO.getSubject());
			if (contractPO.getApprovedId() != null) {
				ModeratorPO moderatorPO = moderatorService.findById(contractPO.getApprovedId());
				if (moderatorPO != null) {
					contractPO.setModeratorName(moderatorPO.getModeratorName());
				}
			}
		}
		return contractPOS;
	}
}
