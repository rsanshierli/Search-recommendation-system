package com.javaproject.dianping.controller;


import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.common.CommonRes;
import com.javaproject.dianping.common.EmBusinessError;
import com.javaproject.dianping.model.CategoryModel;
import com.javaproject.dianping.model.ShopModel;
import com.javaproject.dianping.service.CategoryService;
import com.javaproject.dianping.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/shop")
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;  // 用于后续与 类目 相关的过滤操作

    // 门店推荐服务
    @RequestMapping("/recommend")
    @ResponseBody
    public CommonRes recommend(@RequestParam(name = "longitude")BigDecimal longitude,
                               @RequestParam(name = "latitude")BigDecimal latitude) throws BusinessException {
        if (longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        List<ShopModel> shopModelList = shopService.recommend(longitude, latitude);
        return CommonRes.create(shopModelList);
    }

    // 门店搜索服务
    @RequestMapping("/search")
    @ResponseBody
    public CommonRes search(@RequestParam(name = "longitude")BigDecimal longitude,
                            @RequestParam(name = "latitude")BigDecimal latitude,
                            @RequestParam(name = "keyword")String keyword,
                            @RequestParam(name = "orderby", required = false)Integer orderby,
                            @RequestParam(name = "categoryId", required = false)Integer categoryId,
                            @RequestParam(name = "tags", required = false)String tags) throws BusinessException {
        // @RequestParam(name = "categoryId", required = false)Integer categoryId)
        // 提供根据类目进行二次查询，则需要提供一个非必要的类目信息
        if (StringUtils.isEmpty(keyword) || longitude == null || latitude == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        List<ShopModel> shopModelList = shopService.search(longitude, latitude, keyword, orderby, categoryId, tags);
        List<CategoryModel> categoryModelList = categoryService.selectAll();   // 返回索引类目信息
        List<Map<String, Object>> tagsAggregation = shopService.searchGroupByTags(keyword, categoryId, tags);

        // 使用Map方便回叙搜索查询条件的扩展，
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("shop", shopModelList);
        resMap.put("category", categoryModelList);  // 类目 过滤
        resMap.put("tags", tagsAggregation);        // tag 标签过滤
        return CommonRes.create(resMap);
    }
}
