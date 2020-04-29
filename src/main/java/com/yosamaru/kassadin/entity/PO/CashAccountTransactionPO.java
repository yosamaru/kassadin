package com.yosamaru.kassadin.entity.PO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.EnumSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "D_CASH_ACCOUNT_TRANSACTION")
public class CashAccountTransactionPO extends BasePO implements Serializable {


	private static final long serialVersionUID = 3055698153685772786L;

	public enum TransactionType {
		ORDER("order.buy"),
		RECHARGE("balance.recharge");

		private final String value;

		@Override
		public String toString() {
			return value;
		}

		private TransactionType(final String value) {
			this.value = value;
		}

		public static TransactionType resolve(final Integer value) {
			for (final TransactionType s : EnumSet.allOf(TransactionType.class)) {
				if (s.toString().equals(value)) {
					return s;
				}
			}
			return null;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_account_transaction_seq")
	@SequenceGenerator(name = "cash_account_transaction_seq", sequenceName = "cash_account_transaction_seq")
	@Column(name = "ID")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Transient
	private String userName;

	@Transient
	private AccountPO accountPO;

	@Column(name = "TRANSACTION_AMOUNT", nullable = false, columnDefinition = "DECIMAL(10,2) default 0.00")
	private BigDecimal transactionAmount;


	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;

	@Column(name = "REMARKS")
	private String remarks;


}
