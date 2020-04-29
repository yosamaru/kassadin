package com.yosamaru.kassadin.service.impl;


import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.entity.PO.BasePO;
import com.yosamaru.kassadin.entity.PO.BasePO.Status;
import com.yosamaru.kassadin.service.BaseService;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class AbstractBaseService<T extends BasePO, ID extends Serializable> implements
    BaseService<T, ID> {

  public abstract BaseDao<T, ID> getDAO();

  @Override
  public void save(final T t) {
    getDAO().saveOrUpdate(t);
  }

  @Override
  public void deleteById(final ID id) {
    T t = findById(id);
    if (t == null) {
      return;
    }
    if (t instanceof BasePO) {
      BasePO baseModel = (BasePO) t;
      baseModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
      baseModel.setStatus(Status.FREEZE.toString());
      getDAO().saveOrUpdate(t);
    } else {
      getDAO().delete(t);
    }

  }

  @Override
  public void delete(final T t) {
    if (t instanceof BasePO) {
      BasePO baseModel = (BasePO) t;
      baseModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
      baseModel.setStatus(Status.FREEZE.toString());
      getDAO().saveOrUpdate(t);
    } else {
      getDAO().delete(t);
    }
  }

  @Override
  public T findById(final ID id) {
    return getDAO().getById(id);
  }

  @Override
  public List<T> findAll() {
    return getDAO().findAll();
  }
}
