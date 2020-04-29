package com.yosamaru.kassadin.service;

import java.math.BigDecimal;

public interface StatisticsService {
	Long countContractByStatus(final String code);

	BigDecimal sumTransactionAmountByStatus(final String code);
}
