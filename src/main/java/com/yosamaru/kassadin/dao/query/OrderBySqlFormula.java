package com.yosamaru.kassadin.dao.query;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;

public class OrderBySqlFormula extends Order {
	private static final long serialVersionUID = 2346072007546115304L;
	private String sqlFormula;
	protected OrderBySqlFormula(String sqlFormula) {
		super(sqlFormula, true);
		this.sqlFormula = sqlFormula;
	}
	public String toString() {
		return sqlFormula;
	}
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		return sqlFormula;
	}
	public static Order sqlFormula(String sqlFormula) {
		return new OrderBySqlFormula(sqlFormula);
	}
}