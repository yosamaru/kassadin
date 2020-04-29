package com.yosamaru.kassadin.entity.PO;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "D_PUBLISHER")
public class PublisherPO extends BasePO implements Serializable {

	private static final long serialVersionUID = -6037203449649888145L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publisher_seq")
	@SequenceGenerator(name = "publisher_seq", sequenceName = "publisher_seq")
	@Column(name = "ID")
	private Long id;

	@Column(name = "PUBLISHER_NAME", nullable = false)
	private String publisherName;

	@Column(name = "EMAIL", nullable = false)
	private String email;


	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "TELEPHONE")
	private String telephone;

	@Column(name = "FAX")
	private String fax;

}
