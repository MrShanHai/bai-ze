package com.shanhai.baize.verify;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaizeUsersExtendsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaizeUsersExtends record);

    int insertSelective(BaizeUsersExtends record);

    BaizeUsersExtends selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaizeUsersExtends record);

    int updateByPrimaryKey(BaizeUsersExtends record);
}