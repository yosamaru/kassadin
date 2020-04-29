package com.yosamaru.kassadin.entity.PO;

import java.io.Serializable;
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
@Table(name = "D_SHELF")
public class ShelfPO extends BasePO implements Serializable {

	private static final long serialVersionUID = 6673448982542257796L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shelf_seq")
	@SequenceGenerator(name = "shelf_seq", sequenceName = "shelf_seq")
	@Column(name = "ID")
	private Long id;

	@Column(name = "book_id", nullable = false)
	private Long bookId;

	@Transient
	private String bookSubject;

	@Transient
	private BookPO bookPO;

	@Column(name = "account_Id", nullable = false)
	private Long accountId;

	@Transient
	private String userName;

	@Transient
	private AccountPO userPO;

	@Column(name = "LAST_ACCESS_DATE")
	private Date lastAccessDate;

	@Column(name = "NUMBER_OF_ACCESSES", columnDefinition = "integer default 0", nullable = false)
	private Integer numberOfAccesses;

}
