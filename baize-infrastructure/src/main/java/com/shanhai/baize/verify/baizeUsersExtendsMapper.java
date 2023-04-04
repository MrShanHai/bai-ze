package com.shanhai.baize.verify;

import com.shanhai.baize.verify.baizeUsersExtends;

public interface baizeUsersExtendsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(baizeUsersExtends record);

    int insertSelective(baizeUsersExtends record);

    baizeUsersExtends selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(baizeUsersExtends record);

    int updateByPrimaryKey(baizeUsersExtends record);
}