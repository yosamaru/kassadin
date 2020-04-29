package com.yosamaru.kassadin.entity.PO;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "D_BOOK")
public class BookPO extends BasePO implements Serializable {


	private static final long serialVersionUID = -3164724408963660721L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
	@SequenceGenerator(name = "book_seq", sequenceName = "book_seq")
	@Column(name = "ID")
	private Long id;


	@Column(name = "SUBJECT", unique = true)
	private String subject;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ISBN")
	private String isbn;

	@Column(name = "CONTENT")
	private String content;

	@Column(name = "CONTENT_TYPE")
	private String contentType;

	@Column(name = "AUTHOR_ID", nullable = false)
	private Long authorId;

	@Transient
	private String authorName;

	@Transient
	private AuthorPO authorPO;

	@Column(name = "PUBLISHER_ID", nullable = false)
	private Long publisherId;

	@Transient
	private String publisherName;

	@Transient
	private PublisherPO publisherPO;

	@Column(name = "CATEGORY_ID", nullable = false)
	private Long categoryId;

	@Transient
	private String categoryName;

	@Transient
	private CategoryPO categoryPO;
	@Column(name = "price", nullable = false, columnDefinition = "Decimal(10,2) default 0.00")
	private BigDecimal price;

	@Column(name = "FILE_DOWNLOAD_URI")
	private String fileDownloadUri;
}
