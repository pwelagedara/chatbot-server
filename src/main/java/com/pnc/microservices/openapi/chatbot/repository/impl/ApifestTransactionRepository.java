package com.pnc.microservices.openapi.chatbot.repository.impl;

import com.pnc.microservices.openapi.chatbot.domain.Customer;
import com.pnc.microservices.openapi.chatbot.domain.Transactions;
import com.pnc.microservices.openapi.chatbot.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.pnc.microservices.openapi.chatbot.util.DateConstants.DATE_FORMATTER;

/**
 * @author Palamayuran
 */
@Repository
public class ApifestTransactionRepository implements TransactionRepository {

    private final String getTransactionsUrl;

    private RestTemplate restTemplate;

    @Value("${apifest.app-token}")
    private String appToken;

    private String endDate;

    public ApifestTransactionRepository(RestTemplateBuilder restTemplateBuilder, @Value("${apifest.host}") String
            host, @Value("${apifest.current-date}") String endDate) {
        restTemplate = restTemplateBuilder.build();
        this.endDate = endDate;
        getTransactionsUrl = host + "/Transactions/v2.0.0/transaction/find" + "?accountId={accountId}" +
                "&startDate={startDate}" + "&endDate=" + endDate + "&page=0" + "&size=10";
    }

    @Override
    public Transactions findByAccountIdAndStartDateAfter(Customer customer, int accountId, int noOfDays) throws
            ParseException {
        Date currentDate = DATE_FORMATTER.parse(endDate);
        long duration = noOfDays * 86400000;
        Date startDate = new Date(currentDate.getTime() - duration);

        ResponseEntity<Transactions> accounts = restTemplate.
                exchange(getTransactionsUrl, HttpMethod.GET, new HttpEntity(getHeaders(customer.getToken())),
                        Transactions.class, accountId, DATE_FORMATTER.format(startDate));
        return accounts.getBody();
    }

    private HttpHeaders getHeaders(String customerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + appToken);
        headers.add("X-Authorization", customerToken);
        headers.add("Accept", "application/json");
        return headers;
    }

}