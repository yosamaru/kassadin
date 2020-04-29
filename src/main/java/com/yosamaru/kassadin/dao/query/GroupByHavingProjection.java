package com.yosamaru.kassadin.dao.query;

import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.type.Type;

public class GroupByHavingProjection {

	public static Projection groupByHaving(String column, Type type,
	                                       String having) {

		String[] columns = new String[1];
		Type[] types = new Type[1];

		columns[0] = column;
		types[0] = type;

		return groupByHaving(columns, null, types, having);
	}

	public static Projection groupByHaving(String column, String alias,
	                                       Type type, String having) {

		String[] columns = new String[1];
		String[] aliases = new String[1];
		Type[] types = new Type[1];

		columns[0] = column;
		aliases[0] = alias;
		types[0] = type;

		return groupByHaving(columns, aliases, types, having);
	}

	public static Projection groupByHaving(String[] columns, Type[] types,
	                                       String having) {

		return groupByHaving(columns, null, types, having);
	}

	public static Projection groupByHaving(String[] columns, String[] aliases,
	                                       Type[] types, String having) {

		if (aliases != null && columns.length != aliases.length) {
			return null;
		}
		if (columns.length != types.length) {
			return null;
		}

		final StringBuffer sql = new StringBuffer();
		final StringBuffer groupBy = new StringBuffer();
		for (int i = 0; i < columns.length; i++) {
			sql.append(columns[i]);
			groupBy.append(columns[i]);
			if (aliases != null) {
				sql.append(" as " + aliases[i]);
			}
			if (i < columns.length - 1) {
				sql.append(',');
				groupBy.append(',');
			}
		}

		return Projections.sqlGroupProjection(sql.toString(), groupBy.toString()
				+ " having " + having, aliases, types);
	}
}