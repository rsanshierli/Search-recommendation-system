package com.javaproject.dianping.service;

import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.model.SellerModel;

import java.util.List;

public interface SellerService {

    SellerModel create(SellerModel sellerModel); // 商户创建

    SellerModel get(Integer id);                 // 商户查询

    List<SellerModel> selectAll();               // 查询list

    SellerModel changeStatus(Integer id, Integer disableFlag) throws BusinessException;  // 更改用户禁用状态

    Integer countAllSeller();
}
