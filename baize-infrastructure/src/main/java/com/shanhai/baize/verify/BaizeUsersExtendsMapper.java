package com.shanhai.baize.verify;

public interface BaizeUsersExtendsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaizeUsersExtends record);

    int insertSelective(BaizeUsersExtends record);

    BaizeUsersExtends selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaizeUsersExtends record);

    int updateByPrimaryKey(BaizeUsersExtends record);
}