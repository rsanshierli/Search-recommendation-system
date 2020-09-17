package com.javaproject.dianping.service;

import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.model.UserModel;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public interface UserService {
    /**
     * 用户接口，包括注册、登入
     */

    UserModel getUser(Integer id);

    UserModel register(UserModel userModel) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException;

    UserModel login(String telphone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException;

    // 运营后台注册用户统计
    Integer countAllUser();
}
