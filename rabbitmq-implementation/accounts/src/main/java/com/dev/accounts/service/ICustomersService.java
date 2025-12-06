package com.dev.accounts.service;

import com.dev.accounts.dto.CustomerDetailsDto;

public interface ICustomersService {
    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on given Mobile Number
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
