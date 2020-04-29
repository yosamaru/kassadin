package com.yosamaru.kassadin.entity.VO;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsVO implements Serializable {
	private static final long serialVersionUID = -1316702870674850086L;
	private Long orderNum;
	private Long wOderNum;
	private Long sOderNum;
	private Long fOrderNum;
	private Long aSum;
	private BigDecimal dAmountAll;
	private BigDecimal aAmountAll;
}
