package com.shanhai.baize.domain.customer.gateway;

import com.shanhai.baize.domain.customer.Customer;

public interface CustomerGateway {
    public Customer getByById(String customerId);
}
