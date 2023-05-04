package com.shanhai.baize.verify;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaizeOauthsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaizeOauths record);

    int insertSelective(BaizeOauths record);

    BaizeOauths selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaizeOauths record);

    int updateByPrimaryKey(BaizeOauths record);
}