package com.javaproject.dianping.service;

import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.model.ShopModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShopService {

    ShopModel create(ShopModel shopModel) throws BusinessException;

    ShopModel get(Integer id);

    List<ShopModel> selectAll();

    Integer countAllShop();

    List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags);

    // 推荐
    // 输入经纬度，返回shopmodel 的一个列表
    List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude);


    // 搜索
    List<ShopModel> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags);
}
