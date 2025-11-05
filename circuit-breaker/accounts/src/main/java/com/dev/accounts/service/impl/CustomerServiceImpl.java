package com.dev.accounts.service.impl;

import com.dev.accounts.dto.AccountsDto;
import com.dev.accounts.dto.CardsDto;
import com.dev.accounts.dto.CustomerDetailsDto;
import com.dev.accounts.dto.LoansDto;
import com.dev.accounts.entity.Accounts;
import com.dev.accounts.entity.Customer;
import com.dev.accounts.exception.ResourceNotFoundException;
import com.dev.accounts.mapper.AccountsMapper;
import com.dev.accounts.mapper.CustomerMapper;
import com.dev.accounts.repository.AccountsRepository;
import com.dev.accounts.repository.CustomerRepository;
import com.dev.accounts.service.ICustomersService;
import com.dev.accounts.service.client.CardsFeignClient;
import com.dev.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on given Mobile Number
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "Mobile Number", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customer id", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if (null != loansDtoResponseEntity) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if (null != cardsDtoResponseEntity) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }

        return customerDetailsDto;
    }
}
