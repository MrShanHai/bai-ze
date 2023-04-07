package com.shanhai.baize.verify;

public interface BaizeUsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaizeUsers record);

    int insertSelective(BaizeUsers record);

    BaizeUsers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaizeUsers record);

    int updateByPrimaryKey(BaizeUsers record);

    //根据手机号进行查询
    BaizeUsers selectByPhone(String phone);
}