package com.javaproject.dianping.service.impl;

import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.common.EmBusinessError;
import com.javaproject.dianping.dal.ShopModelMapper;
import com.javaproject.dianping.model.CategoryModel;
import com.javaproject.dianping.model.SellerModel;
import com.javaproject.dianping.model.ShopModel;
import com.javaproject.dianping.service.CategoryService;
import com.javaproject.dianping.service.SellerService;
import com.javaproject.dianping.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.provider.SHA;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopModelMapper shopModelMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SellerService sellerService;

    @Override
    @Transactional
    public ShopModel create(ShopModel shopModel) throws BusinessException {

        // 校验商家是否存在
        SellerModel sellerModel = sellerService.get(shopModel.getSellerId());
        if (sellerModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户不存在");
        }
        // 如果商户已经被禁用
        if (sellerModel.getDisabledFlag().intValue() == 1){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户已禁用");
        }
        // 校验产品类别
        CategoryModel categoryModel = categoryService.get(shopModel.getCategoryId());
        if (categoryModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "产品类别不存在");
        }

        shopModel.setCreatedAt(new Date());
        shopModel.setUpdatedAt(new Date());

        shopModelMapper.insertSelective(shopModel);

        return get(shopModel.getId());
    }

    @Override
    public ShopModel get(Integer id) {
        ShopModel shopModel = shopModelMapper.selectByPrimaryKey(id);
        if (shopModel == null){
            return null;
        }

        // 将shopmodel 与 其他两个model联合起来，通过shopmodel 可以知道其他两个model 对应的信息
        shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
        shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));

        return shopModel;
    }

    @Override
    public List<ShopModel> selectAll() {

        List<ShopModel> shopModelList = shopModelMapper.selectAll();
        shopModelList.forEach(shopModel -> {
            // 将shopmodel 与 其他两个model联合起来，通过shopmodel 可以知道其他两个model 对应的信息
            shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });

        return shopModelList;
    }

    @Override
    public Integer countAllShop() {
        return shopModelMapper.countAllShop();
    }

    @Override
    public List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags) {
        return shopModelMapper.searchGroupByTags(keyword, categoryId, tags);
    }

    @Override
    public List<ShopModel> recommend(BigDecimal longitude, BigDecimal latitude) {

        List<ShopModel> shopModelList = shopModelMapper.recommend(longitude, latitude);
        shopModelList.forEach(shopModel -> {
            // 将shopmodel 与 其他两个model联合起来，通过shopmodel 可以知道其他两个model 对应的信息
            shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModelList;
    }

    @Override
    public List<ShopModel> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderby, Integer categoryId, String tags) {
        List<ShopModel> shopModelList = shopModelMapper.search(longitude, latitude, keyword, orderby, categoryId, tags);
        shopModelList.forEach(shopModel -> {
            // 将shopmodel 与 其他两个model联合起来，通过shopmodel 可以知道其他两个model 对应的信息
            shopModel.setSellerModel(sellerService.get(shopModel.getSellerId()));
            shopModel.setCategoryModel(categoryService.get(shopModel.getCategoryId()));
        });
        return shopModelList;
    }
}
