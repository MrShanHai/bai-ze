package com.shanhai.baize.verify;

import com.shanhai.baize.verify.baizeUsers;

public interface baizeUsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(baizeUsers record);

    int insertSelective(baizeUsers record);

    baizeUsers selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(baizeUsers record);

    int updateByPrimaryKey(baizeUsers record);
}