package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.CashAccountTransactionDao;
import com.yosamaru.kassadin.dao.ContractDao;
import com.yosamaru.kassadin.service.StatisticsService;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsServiceImpl implements StatisticsService {
	@Resource
	private ContractDao contractDao;

	@Resource
	private CashAccountTransactionDao cashAccountTransactionDao;

	@Override
	public Long countContractByStatus(final String code) {
		Long count = contractDao.countByTransactionStatus(code);
		return count;
	}

	@Override
	public BigDecimal sumTransactionAmountByStatus(final String code) {
		BigDecimal amount = cashAccountTransactionDao.sumAmountByType(code);
		return amount;
	}
}
