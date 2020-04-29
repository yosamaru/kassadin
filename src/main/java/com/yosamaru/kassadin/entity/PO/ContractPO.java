package com.yosamaru.kassadin.entity.PO;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "D_CONTRACT")
public class ContractPO extends BasePO implements Serializable {

	private static final long serialVersionUID = 3719725034573005971L;

	public enum ContractStatus {
		SUCCESSS("success"),
		FAIL("fail"),
		WAIT_APPROVED("wait_approved");

		private final String value;

		@Override
		public String toString() {
			return value;
		}

		private ContractStatus(final String value) {
			this.value = value;
		}

		public static ContractPO.ContractStatus resolve(final Integer value) {
			for (final ContractPO.ContractStatus s : EnumSet.allOf(ContractPO.ContractStatus.class)) {
				if (s.toString().equals(value)) {
					return s;
				}
			}
			return null;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_seq")
	@SequenceGenerator(name = "contract_seq", sequenceName = "contract_seq")
	@Column(name = "ID")
	private Long id;

	@Column(name = "ACCOUNT_ID")
	private Long accountId;

	@Transient
	private String userName;

	@Transient
	private AccountPO accountPO;

	@Column(name = "book_id")
	private Long bookId;

	@Transient
	private String bookSubject;

	@Transient
	private BookPO bookPO;

	@Column(name = "VOIDED_DATE")
	private Date voidedDate;

	@Column(name = "APPROVED_ID")
	private Long approvedId;

	@Column(name = "contract_status")
	private String contractStatus;

	@Transient
	private String moderatorName;

	@Column(name = "CASH_ACCOUNT_TRANSACTION_ID")
	private Long cashAccountTransactionId;
}
