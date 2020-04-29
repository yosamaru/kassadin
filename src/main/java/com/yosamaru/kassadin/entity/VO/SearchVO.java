package com.yosamaru.kassadin.entity.VO;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchVO implements Serializable {
	private static final long serialVersionUID = 5680733926621730211L;
	private String subject;
	private String author;
	private String publisher;
	private String category;
}
