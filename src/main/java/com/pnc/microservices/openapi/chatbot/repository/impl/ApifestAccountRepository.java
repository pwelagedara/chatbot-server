package com.pnc.microservices.openapi.chatbot.repository.impl;

import com.pnc.microservices.openapi.chatbot.domain.Account;
import com.pnc.microservices.openapi.chatbot.domain.Accounts;
import com.pnc.microservices.openapi.chatbot.domain.Customer;
import com.pnc.microservices.openapi.chatbot.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * @author Palamayuran
 */
@Repository
public class ApifestAccountRepository implements AccountRepository {

    private final String getAccountsUrl;

    private final String getAccountUrl;

    private final String getTransactionsUrl;

    private RestTemplate restTemplate;

    @Value("${apifest.app-token}")
    private String appToken;

    public ApifestAccountRepository(RestTemplateBuilder restTemplateBuilder, @Value("${apifest.host}") String host) {
        restTemplate = restTemplateBuilder.build();
        getAccountUrl = host + "/Account/v2.0.0/account/";
        getAccountsUrl = host + "/Account/v2.0.0/account?page=0&size=10";
        getTransactionsUrl = host + "/Transactions/v2.0.0/transaction/findByAccountId/{accountId}?page=0&size=10";
    }

    public Accounts getAccounts(Customer customer) {
        ResponseEntity<Accounts> accounts = restTemplate.
                exchange(getAccountsUrl, HttpMethod.GET, new HttpEntity(getHeaders(customer.getToken())),
                        Accounts.class);
        return accounts.getBody();
    }

    private HttpHeaders getHeaders(String customerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + appToken);
        headers.add("X-Authorization", customerToken);
        headers.add("Accept", "application/json");
        return headers;
    }

    public Account getAccount(Customer customer, String accountId) {
        try {
            ResponseEntity<Account> account = restTemplate.
                    exchange(getAccountUrl + accountId, HttpMethod.GET, new HttpEntity(getHeaders(customer.getToken()
                    )), Account.class);
            return account.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
