package com.shanhai.baize.dto;

import com.alibaba.cola.dto.Query;
import lombok.Data;

@Data
public class CustomerListByNameQry extends Query{
   private String name;
}
