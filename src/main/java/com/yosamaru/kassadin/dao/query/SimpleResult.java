package com.yosamaru.kassadin.dao.query;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleResult<T> implements Serializable
{
	private static final long serialVersionUID = 5952453011647200506L;

	private List<T> result;
	private int totalFilteredRows;

	@SuppressWarnings("unused")
	private SimpleResult()
	{

	}

	public SimpleResult(final Set<T> result, final int totalFilteredRows)
	{
		this.result = new LinkedList<>(result);
		this.totalFilteredRows = totalFilteredRows;
	}

	public SimpleResult(final List<T> result, final int totalFilteredRows)
	{
		this.result = result;
		this.totalFilteredRows = totalFilteredRows;
	}

	public List<T> getResult()
	{
		return result;
	}

	public int getTotalFilteredRows() {
		return totalFilteredRows;
	}


}
