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
@Table(name = "D_ACCOUNT")
public class AccountPO extends BasePO implements Serializable {

  private static final long serialVersionUID = 6420234421261761103L;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  @SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
  @Column(name = "ID")
  private Long id;

  @Column(name = "ACCOUNT_NAME", nullable = false)
  private String accountName;

  @Column(name = "PASS_WORD", nullable = false)
  private String password;

  @Column(name = "ANOTHER_NAME")
  private String anotherName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "TELEPHONE", nullable = false, unique = true)
  private Integer telephone;

  @Column(name = "MOBILE")
  private String mobile;

  @Column(name = "ADDRESS")
  private String address;

  @Column(name = "APPROVED_DATE")
  private Date approvedDate;

  @Column(name = "APPROVED_ID")
  private Long approvedId;

  @Column(name = "NUMBER_OF_RETRIES", columnDefinition = "integer default 0", nullable = false)
  private Integer numberOfRetries;

  @Column(name = "LAST_LOGIN_DATE")
  private Date lastLoginDate;

}
