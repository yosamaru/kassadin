package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.BasePO;
import java.io.Serializable;
import java.util.List;

public interface BaseService<T extends BasePO, ID extends Serializable> {

	/**
	 * 新增或更新
	 */
	void save(T t);

	/**
	 * 根据ID删除
	 */
	void deleteById(ID id);

	/**
	 * 根据实体删除
	 */
	void delete(T t);

	/**
	 * 根据ID查找对象
	 */
	T findById(ID id);

	List<T> findAll();
}