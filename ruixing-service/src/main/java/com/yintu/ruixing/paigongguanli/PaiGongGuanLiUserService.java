package com.yintu.ruixing.paigongguanli;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PaiGongGuanLiUserService {
	void addPGGLuser(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity);

	void deleteById(PaiGongGuanLiUserEntity paiGongGuanLiUserEntity);

	List<PaiGongGuanLiUserEntity> findAllUser(String name);
}
