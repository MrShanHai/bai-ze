package com.shanhai.baize.api;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.shanhai.baize.dto.CustomerAddCmd;
import com.shanhai.baize.dto.CustomerListByNameQry;
import com.shanhai.baize.dto.data.CustomerDTO;

public interface CustomerServiceI {

    public Response addCustomer(CustomerAddCmd customerAddCmd);

    public MultiResponse<CustomerDTO> listByName(CustomerListByNameQry customerListByNameQry);
}
