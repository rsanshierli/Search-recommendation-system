package com.javaproject.dianping.service.impl;

import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.common.EmBusinessError;
import com.javaproject.dianping.dal.UserModelMapper;
import com.javaproject.dianping.model.UserModel;
import com.javaproject.dianping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserModelMapper userModelMapper;

    @Override
    public UserModel getUser(Integer id) {
        return userModelMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    // Transactional 声明事务，保证之后的扩展一次register操作可能包含多个数据库的操作，必须保证事物的完整性。
    public UserModel register(UserModel registerUser) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 密码加密转换
        registerUser.setPassword(encodeByMd5(registerUser.getPassword()));

        registerUser.setCreatedAt(new Date());  // 设置创建时间
        registerUser.setUpdateAt(new Date());   // 设置更新时间

        // 防止用户的手机号已经被注册，当数据库的唯一值或者主键冲突时，抛出异常
        try {
            userModelMapper.insertSelective(registerUser);
        }catch (DuplicateKeyException duplicateKeyException) { //重复键异常
            throw new BusinessException(EmBusinessError.REGISTER_DUP_FAIL);
        }

        return getUser(registerUser.getId());
    }

    @Override
    public UserModel login(String telphone, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException, BusinessException {
        // login 只读操作，为了性能考虑，可以不用加transactional，不会对数据库进行写操作

        UserModel userModel = userModelMapper.selectByTelphoneAndPassword(telphone, encodeByMd5(password));
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }
        // 因为后台定义了telphone的唯一性，所以检索成功的话就只有一条数据，直接返回userModel
        return userModel;
    }

    @Override
    public Integer countAllUser() {
        return userModelMapper.countAllUser() ;
    }

    // 确认MD5计算方法，用于将铭文密码进行MD5加密转换
    private String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确认计算方法MD5
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
    }
}
