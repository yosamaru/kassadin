package com.yosamaru.kassadin.dao.impl;

import com.yosamaru.kassadin.dao.ModeratorDao;
import com.yosamaru.kassadin.dao.query.GenericCriteria;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import org.springframework.stereotype.Repository;

@Repository
public class ModeratorDaoImpl extends AbstractBaseDao<ModeratorPO, Long> implements ModeratorDao {
	@Override
	public ModeratorPO getModeratorByUserName(String moderatorName) {
		GenericCriteria genericCriteria = this.getGenericCriteria(ModeratorPO.class);
		genericCriteria.where(genericCriteria.eq("moderatorName", moderatorName));
		return (ModeratorPO) genericCriteria.createQuery().getSingleResult();
	}
}
