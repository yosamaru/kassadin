package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.dao.query.SimpleQuery;
import com.yosamaru.kassadin.dao.query.SimpleResult;
import java.io.Serializable;
import java.util.List;

public interface BaseDao<T, ID extends Serializable> {
	List<T> findAll();

	SimpleResult<T> find(final SimpleQuery simpleQuery);

	T getOne();

	T getById(final ID id);

	T loadById(final ID id);

	boolean exists(final ID id);

	void add(final T entity);

	void update(final T entity);

	void saveOrUpdate(T entity);

	void executeUpdateSql(final String sql);

	void delete(final T entity);

	void deleteById(final ID id);

	void deleteById(final ID[] id);

	T merge(final T entity);

	void flush();
}