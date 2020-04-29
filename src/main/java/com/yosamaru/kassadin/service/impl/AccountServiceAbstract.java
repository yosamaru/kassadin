package com.yosamaru.kassadin.service.impl;

import com.yosamaru.kassadin.dao.AccountDao;
import com.yosamaru.kassadin.dao.BaseDao;
import com.yosamaru.kassadin.entity.PO.AccountPO;
import com.yosamaru.kassadin.entity.PO.BasePO.Status;
import com.yosamaru.kassadin.service.AccountService;
import com.yosamaru.kassadin.util.CommonResponseParams;
import com.yosamaru.kassadin.util.ReturnCode;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceAbstract extends AbstractBaseService<AccountPO, Long> implements AccountService {
	@Resource
	private AccountDao accountDao;

	@Override
	public BaseDao<AccountPO, Long> getDAO() {
		return accountDao;
	}

	@Override
	public CommonResponseParams register(final AccountPO accountPO) {
		if (StringUtils.isBlank(accountPO.getAccountName()) || StringUtils.isBlank(accountPO.getPassword()) || accountPO.getTelephone() == null) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "参数不全");
		}
		AccountPO oldUser = accountDao.getAccountByPhone(accountPO.getTelephone());
		if (oldUser != null && accountPO.getTelephone().equals(oldUser.getTelephone())) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "手机号码重复");
		}

		AccountPO userByUserName = accountDao.getAccountByAccountName(accountPO.getAccountName());
		if (oldUser != null && accountPO.getAccountName().equals(userByUserName.getAccountName())) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名重复");
		}
		accountPO.setStatus(Status.FREEZE.toString());
		accountPO.setNumberOfRetries(0);
		accountDao.saveOrUpdate(accountPO);

		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public CommonResponseParams login(final AccountPO userPO) {

		if (StringUtils.isBlank(userPO.getAccountName()) || StringUtils.isBlank(userPO.getPassword())) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名和密码不能为空");
		}


		AccountPO oldUser = accountDao.getAccountByAccountName(userPO.getAccountName());

		if (oldUser == null) {
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名与密码不匹配");
		}

		if (oldUser.getNumberOfRetries() > 3) {
			if (new Date().getTime() - oldUser.getLastLoginDate().getTime() > 5 * 60 * 1000) {
				return CommonResponseParams.ofFailure(ReturnCode.FAIL, "登陆失败超过三次，请5分钟后重试");
			} else {
				oldUser.setNumberOfRetries(0);
				oldUser.setLastLoginDate(null);
				this.save(oldUser);
			}
		}

		if (oldUser != null && (!userPO.getAccountName().equals(oldUser.getAccountName()) || !userPO.getPassword().equals(oldUser.getPassword()))) {
			oldUser.setNumberOfRetries(oldUser.getNumberOfRetries() + 1);
			oldUser.setLastLoginDate(new Date());
			this.save(oldUser);
			return CommonResponseParams.ofFailure(ReturnCode.FAIL, "用户名与密码不匹配");
		}

		if (Status.FREEZE.equals(userPO.getStatus())) {
			return CommonResponseParams.ofFailure("用户处于冻结状态");
		}

		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public CommonResponseParams loginOut(final AccountPO userPO) {
		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public CommonResponseParams frozen(final AccountPO userPO, final Long moderatorId) {
		AccountPO oldUserPO = this.findById(userPO.getId());
		oldUserPO.setApprovedId(moderatorId);
		oldUserPO.setApprovedDate(new Date());
		this.save(oldUserPO);
		return CommonResponseParams.ofSuccessful();
	}

	@Override
	public AccountPO findByAccountName(final String accountName) {
		AccountPO userPO = accountDao.getAccountByAccountName(accountName);
		return userPO;
	}
}
