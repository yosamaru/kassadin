package com.yosamaru.kassadin.dao.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.domain.Page;

public class SmartTableResponse<T extends Serializable> implements Serializable
{
	private static final long serialVersionUID = 1172813802368907179L;

	@JsonProperty("totalRecords")
	private int totalRecords;

	@JsonProperty("rows")
	private List<T> rows;

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(final int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(final List<T> rows) {
		this.rows = rows;
	}

	public static <Q extends Serializable> SmartTableResponse<Q> fromPage(final Page<Q> page)
	{
		final SmartTableResponse<Q> dtr = new SmartTableResponse<>();
		dtr.setRows(page.getContent());
		dtr.setTotalRecords((int) page.getTotalElements());
		return dtr;

	}

	public static <Q extends Serializable> SmartTableResponse<Q> fromSimpleResponse(final SimpleResult<Q> simpleResult)
	{
		final SmartTableResponse<Q> dtr = new SmartTableResponse<>();
		dtr.setRows(simpleResult.getResult());
		dtr.setTotalRecords(simpleResult.getTotalFilteredRows());
		return dtr;
	}

}