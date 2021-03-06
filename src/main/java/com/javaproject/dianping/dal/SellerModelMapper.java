package com.javaproject.dianping.dal;

import com.javaproject.dianping.model.SellerModel;

import java.util.List;

public interface SellerModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller
     *
     * @mbg.generated Wed Sep 16 20:58:36 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller
     *
     * @mbg.generated Wed Sep 16 20:58:36 CST 2020
     */
    int insert(SellerModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller
     *
     * @mbg.generated Wed Sep 16 20:58:36 CST 2020
     */
    int insertSelective(SellerModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller
     *
     * @mbg.generated Wed Sep 16 20:58:36 CST 2020
     */
    SellerModel selectByPrimaryKey(Integer id);

    List<SellerModel> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller
     *
     * @mbg.generated Wed Sep 16 20:58:36 CST 2020
     */
    int updateByPrimaryKeySelective(SellerModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table seller
     *
     * @mbg.generated Wed Sep 16 20:58:36 CST 2020
     */
    int updateByPrimaryKey(SellerModel record);

    Integer countAllSeller();
}