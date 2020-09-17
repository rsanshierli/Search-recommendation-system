package com.javaproject.dianping.dal;

import com.javaproject.dianping.model.UserModel;
import org.apache.ibatis.annotations.Param;

public interface UserModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Mon Sep 14 23:13:59 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Mon Sep 14 23:13:59 CST 2020
     */
    int insert(UserModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Mon Sep 14 23:13:59 CST 2020
     */
    int insertSelective(UserModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Mon Sep 14 23:13:59 CST 2020
     */
    UserModel selectByPrimaryKey(Integer id);

    // 登入接口，通过telphone和password进行登入，通过param注解声明 使用什么名字读取这两个字符串
    UserModel selectByTelphoneAndPassword(@Param("telphone") String telphone, @Param("password") String password);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Mon Sep 14 23:13:59 CST 2020
     */
    int updateByPrimaryKeySelective(UserModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user
     *
     * @mbg.generated Mon Sep 14 23:13:59 CST 2020
     */
    int updateByPrimaryKey(UserModel record);

    Integer countAllUser();
}