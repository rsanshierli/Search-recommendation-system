package com.javaproject.dianping.service.impl;

import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.common.EmBusinessError;
import com.javaproject.dianping.dal.CategoryModelMapper;
import com.javaproject.dianping.model.CategoryModel;
import com.javaproject.dianping.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service // @Service是把spring容器中的bean进行实例化，也就是等同于new操作，只有实现类是可以进行new实例化的，而接口则不能，所以是加在实现类上的。
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryModelMapper categoryModelMapper;


    @Override
    public CategoryModel create(CategoryModel categoryModel) throws BusinessException {
        categoryModel.setCreatedAt(new Date());
        categoryModel.setUpdatedAt(new Date());

        // 防止category中的 name 已经被注册，当数据库的唯一值或者主键冲突时，抛出异常
        try {
            categoryModelMapper.insertSelective(categoryModel);
        }catch (DuplicateKeyException duplicateKeyException) { //重复键异常
            throw new BusinessException(EmBusinessError.CATEGORT_NAME_DUPLICATED);
        }

        categoryModelMapper.insertSelective(categoryModel);

        return get(categoryModel.getId());
    }

    @Override
    public CategoryModel get(Integer id) {
        return categoryModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CategoryModel> selectAll() {
        return categoryModelMapper.selectAll();
    }

    @Override
    public Integer countAllCategory() {
        return categoryModelMapper.countAllCategory();
    }
}
