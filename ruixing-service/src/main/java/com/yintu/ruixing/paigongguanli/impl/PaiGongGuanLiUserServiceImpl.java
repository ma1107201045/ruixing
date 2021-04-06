package com.yintu.ruixing.paigongguanli.impl;

import com.github.pagehelper.PageInfo;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiTaskDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiTaskUserDao;
import com.yintu.ruixing.master.paigongguanli.PaiGongGuanLiUserDao;
import com.yintu.ruixing.master.xitongguanli.UserDao;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiTaskUserEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserEntity;
import com.yintu.ruixing.paigongguanli.PaiGongGuanLiUserService;
import com.yintu.ruixing.xitongguanli.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaiGongGuanLiUserServiceImpl implements PaiGongGuanLiUserService {
	@Autowired
	private PaiGongGuanLiUserDao paiGongGuanLiUserDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private PaiGongGuanLiTaskDao paiGongGuanLiTaskDao;
	@Autowired
	private PaiGongGuanLiTaskUserDao paiGongGuanLiTaskUserDao;

	@Override
	public List<PaiGongGuanLiUserEntity> findAllUser(String name) {
		return paiGongGuanLiUserDao.findAllUser(name);
	}

	@Override
	public void deleteById(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity) {
		Long userid = paiGongGuanLiUserEntity.getId();//人员id
		paiGongGuanLiUserDao.updateByUid(paiGongGuanLiUserEntity);
		UserEntity userEntity=new UserEntity();
		userEntity.setId(userid);
		userEntity.setPaiGongGuanLiState(0);
		userDao.updateByPrimaryKeySelective(userEntity);
		//刪除分數
		paiGongGuanLiTaskUserDao.deleteByuid(userid.intValue());
	}

	@Override
	public void addPGGLuser(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity) {
		Integer userid = paiGongGuanLiUserEntity.getUserid();
		paiGongGuanLiUserDao.insertSelective(paiGongGuanLiUserEntity);
		Long id = paiGongGuanLiUserEntity.getId();
		UserEntity userEntity=new UserEntity();
		userEntity.setId((long) userid);
		userEntity.setPaiGongGuanLiState(1);
		userDao.updateByPrimaryKeySelective(userEntity);
		List<Integer> taskid=paiGongGuanLiTaskDao.findId();
		if (taskid.size()!=0){
			for (Integer taskId : taskid) {
				PaiGongGuanLiTaskUserEntity paiGongGuanLiTaskUserEntity=new PaiGongGuanLiTaskUserEntity();
				paiGongGuanLiTaskUserEntity.setTid(taskId);
				paiGongGuanLiTaskUserEntity.setUid(id.intValue());
				paiGongGuanLiTaskUserDao.insertSelective(paiGongGuanLiTaskUserEntity);
			}
		}
	}
}
