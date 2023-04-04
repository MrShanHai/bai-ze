package com.shanhai.baize.verify;

import com.shanhai.baize.verify.baizeOauths;

public interface baizeOauthsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(baizeOauths record);

    int insertSelective(baizeOauths record);

    baizeOauths selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(baizeOauths record);

    int updateByPrimaryKey(baizeOauths record);
}