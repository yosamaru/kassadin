package com.yosamaru.kassadin.dao.query;

import com.yosamaru.kassadin.dao.query.SimpleQuery.WrappedObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({WrappedObject.class, ArrayList.class})
public class SimpleQuery implements Serializable
{
	private static final long serialVersionUID = 4652711864361672603L;

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(SimpleQuery.class);

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class WrappedObject implements Serializable
	{
		private static final long serialVersionUID = -3655160118226429201L;
		private List<Object> objects;

		public WrappedObject(){
		}

		@SuppressWarnings("unchecked")
		public WrappedObject(@SuppressWarnings("rawtypes") List objects){
			this.objects=objects;
		}

		public Object getObjects() {
			return objects;
		}

		public void setObjects(List<Object> objects) {
			this.objects = objects;
		}

		@Override
		public String toString() {
			return "WrappedObject [objects=" + objects + "]";
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Column implements Comparable<Column>, Serializable
	{
		private static final long serialVersionUID = -7223456849508882896L;

		private String name;
		private Boolean ascending;
		private boolean isInner;
		private String value;

		@SuppressWarnings("unused")
		private Column()
		{

		}

		public Column(final String name)
		{
			this(name, null);
		}

		public Column(final String name, final Boolean ascending)
		{
			this(name, ascending, true);
		}

		public Column(final String name, final Boolean ascending, final boolean isInner)
		{
			this(name, ascending, isInner, null);
		}

		public Column(final String name, final Boolean ascending, final boolean isInner, final String value)
		{
			this.name = name;
			this.ascending = ascending;
			this.isInner = isInner;
			this.value = value;
		}

		void setValue(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public boolean isInner() {
			return isInner;
		}

		public String getName()
		{
			return name;
		}
		public Boolean isAscending()
		{
			return ascending;
		}

		void setAscending(final Boolean ascending) {
			this.ascending = ascending;
		}

		@Override
		public String toString()
		{
			return String.format("%s:%s:%s",name, value, ascending==null ? "NA" : (ascending ? "ASC" : "DESC"));
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Column other = (Column) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			return true;
		}

		@Override
		public int compareTo(final Column o)
		{
			return name.compareTo(o.name);
		}
	}

	private long startRow;
	private long pageSize;
	private Map<String,Object> extra;
	private Column[] columns;
	private String query;

	@SuppressWarnings("unused")
	private SimpleQuery()
	{
		this.columns = new Column[]{};
	}

	public static SimpleQuery createByPageIndexAndPageSize(final long pageIndex, final long pageSize)
	{
		long startRow = 0;
		if(pageIndex > 0)
		{
			startRow = (pageIndex - 1) * pageSize;
		}
		return new SimpleQuery(startRow, pageSize);
	}

	public SimpleQuery(final long startRow, final long pageSize)
	{
		this(null, startRow, pageSize, new HashMap<String,Object>());
	}

	public SimpleQuery(final String query, final long startRow, final long pageSize, final Map<String,Object> extra, final Collection<Column> columns)
	{
		this(query, startRow, pageSize, extra, columns.toArray(new Column[0]));
	}

	@SuppressWarnings("unchecked")
	public SimpleQuery(final String query, final long startRow, final long pageSize, Map<String,Object> extra, final Column ... column)
	{
		this.query = query;
		this.startRow = startRow;
		this.pageSize = pageSize;
		this.columns = column;
		if(extra!=null)
		{
			final Map<String,Object> p = new HashMap<>();
			for(final Map.Entry<String, Object> en : extra.entrySet())
			{
				final String key = en.getKey();
				final Object value = en.getValue();
				if(value!=null && value instanceof Collection)
				{
					final WrappedObject wo = new WrappedObject();
					final ArrayList<Object> objects = new ArrayList<>();
					objects.addAll((Collection<Object>) value);
					wo.setObjects(objects);
					p.put(key, wo);
				}else
				{
					p.put(key, value);
				}
			}
			extra = p;
		}
		this.extra = extra;
	}


	public Map<String, Object> getExtra() {
		return extra;
	}

	public String getQuery()
	{
		return query;
	}

	public Column[] getColumns()
	{
		return columns==null ? new Column[]{} : columns;
	}

	public long getStartRow()
	{
		return startRow;
	}

	public long getPageSize()
	{
		return pageSize;
	}

	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("Query:").append(query).append(" StartRow:").append(startRow).append(" PageSize:").append(pageSize).append(" Extra:").append(extra)
				.append(" Filter&Order:[");
		for(final Column c : this.getColumns())
		{
			sb.append('(');
			sb.append(c.toString());
			sb.append(')');
		}
		sb.append(']');
		return sb.toString();
	}

}
