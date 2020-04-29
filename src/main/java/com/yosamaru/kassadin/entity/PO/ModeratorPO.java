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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "D_MODERATOR")
public class ModeratorPO extends BasePO implements Serializable {

	private static final long serialVersionUID = -5936144011349627461L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moderator_seq")
	@SequenceGenerator(name = "moderator_seq", sequenceName = "moderator_seq")
	@Column(name = "ID")
	private Long id;

	@Column(name = "MODERATOR_NAME", unique = true)
	private String moderatorName;

	@Column(name = "PASS_WORD")
	private String password;


	@Column(name = "LAST_LOGIN_DATE")
	private Date lastLoginDate;
}
