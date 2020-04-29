package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.BookDao;
import com.yosamaru.kassadin.entity.PO.BookPO;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImpl extends AbstractBaseDao<BookPO, Long> implements BookDao {

}
