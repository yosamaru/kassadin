package com.yosamaru.kassadin.dao;

import com.yosamaru.kassadin.entity.PO.ModeratorPO;

public interface ModeratorDao extends BaseDao<ModeratorPO, Long> {

	ModeratorPO getModeratorByUserName(final String userName);
}
