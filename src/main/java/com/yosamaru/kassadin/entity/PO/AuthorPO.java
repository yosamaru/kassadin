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
@Table(name = "D_AUTHOR")
public class AuthorPO extends BasePO implements Serializable {

  private static final long serialVersionUID = 6538380830441600917L;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq")
  @SequenceGenerator(name = "author_seq", sequenceName = "author_seq")
  @Column(name = "ID")
  private Long Id;

  @Column(name = "AUTHOR_NAME", nullable = false)
  private String authorName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "COUNTRY")
  private String Country;

}
