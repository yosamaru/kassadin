package com.yosamaru.kassadin.service;

import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.util.CommonResponseParams;

public interface ModeratorService extends BaseService<ModeratorPO, Long> {

	CommonResponseParams register(ModeratorPO moderatorPO);

	CommonResponseParams login(ModeratorPO moderatorPO);

	ModeratorPO findByName(String username);
}
