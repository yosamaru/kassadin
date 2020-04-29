package com.yosamaru.kassadin.entity.PO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
@Table(name = "D_CHEQUE")
public class ChequePO extends BasePO implements Serializable {


	private static final long serialVersionUID = 6433175370687162193L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cheque_seq")
	@SequenceGenerator(name = "cheque_seq", sequenceName = "cheque_seq")
	@Column(name = "ID")
	private Long id;

	@Column(name = "ACCOUNT_ID", unique = true)
	private Long accountId;

	@Transient
	private String accountName;

	@Column(name = "balance")
	private BigDecimal balance;

	@Transient
	private AccountPO accountPO;

	@Column(name = "CONTRACT_PRICE", nullable = false, columnDefinition = "DECIMAL(10,2) default 0.00")
	private BigDecimal contractPrice;

	@Column(name = "APPROVED_DATE")
	private Date approvedDate;

	@Column(name = "APPROVED_ID")
	private Long approvedId;

	@Transient
	private String moderatorName;
}
