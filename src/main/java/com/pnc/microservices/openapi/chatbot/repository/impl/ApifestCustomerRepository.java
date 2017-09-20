package com.pnc.microservices.openapi.chatbot.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.microservices.openapi.chatbot.domain.Customer;
import com.pnc.microservices.openapi.chatbot.domain.LoginRequest;
import com.pnc.microservices.openapi.chatbot.domain.LoginResponse;
import com.pnc.microservices.openapi.chatbot.domain.Party;
import com.pnc.microservices.openapi.chatbot.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Palamayuran
 */
@Repository
public class ApifestCustomerRepository implements CustomerRepository {

    private final String loginUrl;
    private final String customerUrl;

    private RestTemplate restTemplate;

    private final Map<String, Customer> customers;

    @Autowired
    private ObjectMapper mapper;

    @Value("${apifest.app-token}")
    private String appToken;

    public ApifestCustomerRepository(RestTemplateBuilder restTemplateBuilder, @Value("${apifest.host}") String host) {
        restTemplate = restTemplateBuilder.build();
        loginUrl = host + "/Security/v2.0.0/login";
        customerUrl = host + "/Customers/v2.0.0/party/profile";
        customers = new ConcurrentHashMap<>();
    }

    public Customer getCustomer(String id){
        return customers.get(id);
    }

    @Override
    public Customer createCustomer(String id, String username, String password) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setUsername(username);
        customer.setPassword(password);

        LoginRequest loginRequest = new LoginRequest().setUsername(username).setPassword(password);

        HttpHeaders headers = getHeaders();

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<LoginResponse> response = restTemplate.exchange(loginUrl, HttpMethod.POST, httpEntity, LoginResponse.class);
        customer.setToken(response.getBody().getToken());

        headers.add("X-Authorization", customer.getToken());
        httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Party> party = restTemplate.exchange(customerUrl, HttpMethod.GET, httpEntity, Party.class);
        customer.setFirstName(party.getBody().getPerson().getFirstName());

        customers.put(customer.getId(), customer);
        return customer;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + appToken);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
