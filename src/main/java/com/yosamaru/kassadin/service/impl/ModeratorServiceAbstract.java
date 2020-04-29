package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.dao.ModeratorDao;
import com.yosamaru.kassadin.entity.PO.BasePO.Status;
import com.yosamaru.kassadin.entity.PO.ModeratorPO;
import com.yosamaru.kassadin.service.ModeratorService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import com.yosamaru.kassadin.util.ReturnCode;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ModeratorServiceAbstract extends AbstractBaseService<ModeratorPO, Long> implements ModeratorService {

	@Resource
	private ModeratorDao moderatorDao;


	@Override
	public BaseDao<ModeratorPO, Long> getDAO() {
		return moderatorDao;
	}

	@Override
	public CommonResponseParams register(final ModeratorPO moderatorPO) {
		if (StringUtils.isBlank(moderatorPO.getModeratorName()) || StringUtils.isBlank(moderatorPO.getPassword())) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名和密码不能为空");
		}

		ModeratorPO oldUser = moderatorDao.getModeratorByUserName(moderatorPO.getModeratorName());
		if (oldUser != null && moderatorPO.getModeratorName().equals(moderatorPO.getModeratorName())) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名重复");
		}

		moderatorDao.saveOrUpdate(moderatorPO);

		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public CommonResponseParams login(final ModeratorPO moderatorPO) {

		if (StringUtils.isBlank(moderatorPO.getModeratorName()) || StringUtils.isBlank(moderatorPO.getPassword())) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名和密码不能为空");
		}

		ModeratorPO oldUser = moderatorDao.getModeratorByUserName(moderatorPO.getModeratorName());
		if (oldUser == null || !oldUser.getModeratorName().equals(oldUser.getModeratorName()) || !oldUser.getPassword().equals(oldUser.getPassword())) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名与密码不匹配");
		}

		if (Status.FREEZE.toString().equals(oldUser.getStatus() )) {
			return CommonResponseParams.ofSuccessful("用户不存在");
		}

		oldUser.setLastLoginDate(new Date());

		moderatorDao.saveOrUpdate(oldUser);


		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public ModeratorPO findByName(final String username) {
		ModeratorPO moderatorPO = moderatorDao.getModeratorByUserName(username);
		return moderatorPO;
	}
}
