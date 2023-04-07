package com.shanhai.baize.verify;

public interface BaizeOauthsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaizeOauths record);

    int insertSelective(BaizeOauths record);

    BaizeOauths selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaizeOauths record);

    int updateByPrimaryKey(BaizeOauths record);
}