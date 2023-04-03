package com.shanhai.baize.domain.customer.gateway;

import com.shanhai.baize.domain.customer.Customer;
import com.shanhai.baize.domain.customer.Credit;

//Assume that the credit info is in antoher distributed Service
public interface CreditGateway {
    public Credit getCredit(String customerId);
}
