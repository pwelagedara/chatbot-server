package com.pnc.microservices.openapi.chatbot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnc.microservices.openapi.chatbot.domain.Account;
import com.pnc.microservices.openapi.chatbot.domain.Accounts;
import com.pnc.microservices.openapi.chatbot.domain.Customer;
import com.pnc.microservices.openapi.chatbot.domain.Transactions;
import com.pnc.microservices.openapi.chatbot.model.*;
import com.pnc.microservices.openapi.chatbot.model.enums.EntityType;
import com.pnc.microservices.openapi.chatbot.model.enums.SenderAction;
import com.pnc.microservices.openapi.chatbot.repository.AccountRepository;
import com.pnc.microservices.openapi.chatbot.repository.CustomerRepository;
import com.pnc.microservices.openapi.chatbot.repository.TransactionRepository;
import com.pnc.microservices.openapi.chatbot.service.ChatbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.pnc.microservices.openapi.chatbot.util.StringLiterals.*;

/**
 * @author Palamayuran
 */
@Service
public class ChatbotServiceImpl implements ChatbotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatbotServiceImpl.class);

    private final RestTemplate restTemplate;

    private final ExecutorService executorService;

    private String token;

    private final String sendMessageUrl;

    private final String accountImageUrl;

    private final String yesImageUrl;

    private final String noImageUrl;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    public ChatbotServiceImpl(RestTemplateBuilder restTemplateBuilder, @Value("${facebook.webhook.setup}") boolean
            setup, @Value("${facebook.webhook.token-length}") int tokenLength, @Value("${facebook.send-message" +
            ".api-url}") String apiUrl, @Value("${facebook.page.token}") String pageToken, @Value("${chatbot" +
            ".image-url.account}") String accountImageUrl, @Value("${chatbot.image-url.accept}") String yesImageUrl,
                              @Value("${chatbot.image-url.deny}") String noImageUrl) {
        this.accountImageUrl = accountImageUrl;
        this.yesImageUrl = yesImageUrl;
        this.noImageUrl = noImageUrl;

        executorService = Executors.newSingleThreadExecutor();
        restTemplate = restTemplateBuilder.build();
        sendMessageUrl = apiUrl + "?access_token=" + pageToken;

        if (setup) {
            SecureRandom random = new SecureRandom();
            byte[] randomBytes = new byte[tokenLength];
            random.nextBytes(randomBytes);
            token = Base64.getEncoder().encodeToString(randomBytes);
            LOGGER.info("Facebook Verification Token: " + token);
        }

    }

    public boolean verifyToken(String tokenPresented) {
        return token.equals(tokenPresented);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    /**
     * This method replies to the customer's account-oriented message by giving account options to choose from.
     *
     * @param recipient
     */
    private void sendAccountOptionsForAccountDetails(Recipient recipient) {
        Customer customer = customerRepo.getCustomer(recipient.getId());
        Accounts accounts = accountRepo.getAccounts(customer);

        final Message message = new Message();
        message.setText("I understand that you want to check your account details. Pick an account to proceed...");
        message.setQuickReplies(new ArrayList<>());

        accounts.getAccounts().stream().forEach(account -> {
            try {
                QuickReply qr = new QuickReply();
                qr.setTitle(account.getSummary());
                qr.setContentType(TEXT);
                qr.setImageUrl(accountImageUrl);

                Option payload = new Option().setId(account.getId()).setType(EntityType.ACCOUNTS);
                qr.setPayload(mapper.writeValueAsString(payload));

                message.getQuickReplies().add(qr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        Reply reply = new Reply();
        reply.setRecipient(recipient);
        reply.setMessage(message);

        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method replies to the customer's transaction-oriented message by giving account options to choose from.
     *
     * @param recipient
     */
    private void sendAccountOptionsForTransactions(Recipient recipient) {
        Customer customer = customerRepo.getCustomer(recipient.getId());
        Accounts accounts = accountRepo.getAccounts(customer);

        final Message message = new Message();
        message.setText("I can see that you wanna checkout your transactions. Pick an account to view transactions...");
        message.setQuickReplies(new ArrayList<>());

        accounts.getAccounts().stream().forEach(account -> {
            try {
                QuickReply qr = new QuickReply();
                qr.setTitle(account.getSummary());
                qr.setContentType(TEXT);
                qr.setImageUrl(accountImageUrl);

                Option payload = new Option().setId(account.getId()).setType(EntityType.TRANSACTIONS).setAccept(true);
                qr.setPayload(mapper.writeValueAsString(payload));

                message.getQuickReplies().add(qr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        Reply reply = new Reply();
        reply.setRecipient(recipient);
        reply.setMessage(message);

        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method informs the customer that his/her current message has been seen/read by the bot.
     *
     * @param recipient
     */
    private void sendSeen(Recipient recipient) {
        Reply reply = new Reply();
        reply.setRecipient(recipient);
        reply.setSenderAction(SenderAction.mark_seen);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method suggests the customer, after a account view request, that a transaction view could be seen too.
     *
     * @param recipient
     * @param accountId
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    private void sendViewTransactionSuggestion(Recipient recipient, int accountId) throws InterruptedException,
            JsonProcessingException {
        Thread.sleep(2000);

        final Message message = new Message();
        message.setText("Wanna check your recent transactions for the account too?");
        message.setQuickReplies(new ArrayList<>());

        QuickReply yesQR = new QuickReply();
        yesQR.setTitle(YES);
        yesQR.setContentType(TEXT);
        yesQR.setImageUrl(yesImageUrl);

        Option yesOption = new Option().setId(accountId).setType(EntityType.TRANSACTIONS).setAccept(true);
        yesQR.setPayload(mapper.writeValueAsString(yesOption));

        message.getQuickReplies().add(yesQR);

        QuickReply noQR = new QuickReply();
        noQR.setTitle(NO);
        noQR.setContentType(TEXT);
        noQR.setImageUrl(noImageUrl);

        Option noOption = new Option().setId(accountId).setType(EntityType.TRANSACTIONS).setAccept(false);
        noQR.setPayload(mapper.writeValueAsString(noOption));

        message.getQuickReplies().add(noQR);

        Reply reply = new Reply();
        reply.setRecipient(recipient);
        reply.setMessage(message);

        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This methos suggests the custoemr to choose transaction duration.
     *
     * @param recipient
     * @param accountId
     */
    private void sendTxDurationOptions(Recipient recipient, int accountId) throws JsonProcessingException {
        final Message message = new Message();
        message.setText("I can get you transactions made within last... ");
        message.setQuickReplies(new ArrayList<>());

        QuickReply oneDayQR = new QuickReply();
        oneDayQR.setTitle("1 Day");
        oneDayQR.setContentType(TEXT);
        Option oneDay = new Option().setId(accountId).setType(EntityType.TRANSACTIONS).setValue(1);
        oneDayQR.setPayload(mapper.writeValueAsString(oneDay));
        message.getQuickReplies().add(oneDayQR);

        QuickReply threeDaysQR = new QuickReply();
        threeDaysQR.setTitle("3 Days");
        threeDaysQR.setContentType(TEXT);
        Option threeDays = new Option().setId(accountId).setType(EntityType.TRANSACTIONS).setValue(3);
        threeDaysQR.setPayload(mapper.writeValueAsString(threeDays));
        message.getQuickReplies().add(threeDaysQR);

        QuickReply oneWeekQR = new QuickReply();
        oneWeekQR.setTitle("7 Days");
        oneWeekQR.setContentType(TEXT);
        Option oneWeek = new Option().setId(accountId).setType(EntityType.TRANSACTIONS).setValue(7);
        oneWeekQR.setPayload(mapper.writeValueAsString(oneWeek));
        message.getQuickReplies().add(oneWeekQR);

        QuickReply twoWeeksQR = new QuickReply();
        twoWeeksQR.setTitle("14 Days");
        twoWeeksQR.setContentType(TEXT);
        Option twoWeeks = new Option().setId(accountId).setType(EntityType.TRANSACTIONS).setValue(14);
        twoWeeksQR.setPayload(mapper.writeValueAsString(twoWeeks));
        message.getQuickReplies().add(twoWeeksQR);

        QuickReply oneMonthQR = new QuickReply();
        oneMonthQR.setTitle("30 Days");
        oneMonthQR.setContentType(TEXT);
        Option oneMonth = new Option().setId(accountId).setType(EntityType.TRANSACTIONS).setValue(30);
        oneMonthQR.setPayload(mapper.writeValueAsString(oneMonth));
        message.getQuickReplies().add(oneMonthQR);

        Reply reply = new Reply();
        reply.setRecipient(recipient);
        reply.setMessage(message);

        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method informs the customer that the bot is currently typing a response.
     *
     * @param recipient
     * @throws InterruptedException
     */
    private void sendTypingOn(Recipient recipient) throws InterruptedException {
        Thread.sleep(1000);
        Reply reply = new Reply();
        reply.setRecipient(recipient);
        reply.setSenderAction(SenderAction.typing_on);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method returns the account details to the customer.
     *
     * @param recipient
     * @param accountId
     */
    private void sendAccountDetails(Recipient recipient, int accountId) {
        Customer customer = customerRepo.getCustomer(recipient.getId());
        Account account = accountRepo.getAccount(customer, Integer.toString(accountId));

        final Message message = new Message();
        message.setText(account.toString());

        Reply reply = new Reply().setRecipient(recipient).setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());

    }

    /**
     * This method sends the transaction details to customer.
     *
     * @param recipient
     * @param accountId
     * @param noOfDays
     */
    private void sendTransactionDetails(Recipient recipient, int accountId, int noOfDays) {
        final String customerId = recipient.getId();
        Customer customer = customerRepo.getCustomer(customerId);

        try {
            Transactions transactions = transactionRepo.findByAccountIdAndStartDateAfter(customer, accountId, noOfDays);

            if (transactions.getTransactions().isEmpty()) {
                Message message = new Message().setText("I'm sorry... but there are no transactions recorded for this" +
                        " account.");
                Reply reply = new Reply().setRecipient(recipient).setMessage(message);
                restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
            } else {
                StringBuilder builder = new StringBuilder("Transactions\n-------------");

                transactions.getTransactions().stream().forEach(transaction -> {
                    builder.append("\n").append(transaction.toString()).append("\n");

                    if (builder.length() > 500) {
                        final Message message = new Message().setText(builder.toString());

                        Reply reply = new Reply().setRecipient(recipient).setMessage(message);
                        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
                        // Send typing on to customer between transaction messages.
                        try {
                            sendTypingOn(recipient);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        builder.delete(0, builder.length()).append("Transactions\n------------");
                    }
                });

                if (builder.length() != 0) {
                    Message message = new Message().setText(builder.toString());

                    Reply reply = new Reply().setRecipient(recipient).setMessage(message);
                    restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This methods continues the conversation with the customer.
     *
     * @param recipient
     * @throws InterruptedException
     */
    private void continueConversation(Recipient recipient) throws InterruptedException {
        sendTypingOn(recipient);
        Thread.sleep(2000);

        final Message message = new Message();
        message.setText("What else can I do for you today? :)");
        Reply reply = new Reply().setRecipient(recipient).setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method sends login invite.
     * @param recipient
     * @param host
     * @throws InterruptedException
     */
    private void sendLoginInvite(Recipient recipient, String host) throws InterruptedException {
        Message message = new Message();
        message.setText("Looks like you want me to pull up your financial account information... Lemme see...");
        Reply reply = new Reply().setRecipient(recipient).setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());

        Thread.sleep(1000);
        message.setText("Oops... too bad! I see you aren't authenticated with me yet...!");
        reply.setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());

        Thread.sleep(1000);
        message.setText("Please click on the link and login to continue. " +
                "https://" + host + "/chatbot/v1/login?id=" + recipient.getId());
        reply.setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method welcomes customer, once logged in.
     * @param id
     * @param firstName
     */
    public void welcomeCustomer(String id, String firstName){
        final Message message = new Message();
        message.setText("Hi " + firstName + "! Thanks for logging in. How can I be of service today?");
        Reply reply = new Reply().setRecipient(new Recipient().setId(id)).setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method welcomes anonymous user.
     * @param recipient
     */
    public void welcomeAnonymous(Recipient recipient){
        final Message message = new Message();
        message.setText("Hi there! Hope you're having a wonderful day :D How can I add to it?");
        Reply reply = new Reply().setRecipient(recipient).setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    public void windChat(Recipient recipient){
        final Message message = new Message();
        message.setText("My pleasure serving you! Have a great one! Goodbye :)");
        Reply reply = new Reply().setRecipient(recipient).setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    public void unrecognizedMessage(Recipient recipient){
        final Message message = new Message();
        message.setText("Sorry I didn't understand that. Could you please be a bit more precise, like, say 'show me my accounts' or 'I wanna see my transactions'.");
        Reply reply = new Reply().setRecipient(recipient).setMessage(message);
        restTemplate.postForEntity(sendMessageUrl, reply, Response.class, getHeaders());
    }

    /**
     * This method is where all the message gets dispatched to the respective handlers.
     *
     * @param webhookPayload
     * @throws IOException
     * @throws InterruptedException
     */
    public void process(WebhookPayload webhookPayload, String host) throws IOException, InterruptedException {
        Message message = webhookPayload.getEntries().get(0).getMessageInfos().get(0).getMessage();

        Recipient recipient = new Recipient().setId(webhookPayload.getEntries().get(0).getMessageInfos().get(0)
                .getSender().getId());

        // Immediately inform the customer that the message is been seen/read.
        sendSeen(recipient);

        // Inform the customer that the reply is getting prepared.
        sendTypingOn(recipient);

        Optional<String> text = Optional.ofNullable(message.getText());
        Optional<QuickReply> quickReply = Optional.ofNullable(message.getQuickReply());

        if (text.isPresent() && !quickReply.isPresent()) {
            if (text.get().toLowerCase().contains("hi") || text.get().toLowerCase().contains("hello") || text.get().toLowerCase().contains("hey")) {
                executorService.submit(() -> welcomeAnonymous(recipient));
            } else if (text.get().toLowerCase().contains("transaction")) {
                Optional<Customer> customer = Optional.ofNullable(customerRepo.getCustomer(recipient.getId()));
                if (!customer.isPresent()) {
                    sendLoginInvite(recipient, host);
                }
                executorService.submit(() -> sendAccountOptionsForTransactions(recipient));
            } else if (text.get().toLowerCase().contains("account")) {
                Optional<Customer> customer = Optional.ofNullable(customerRepo.getCustomer(recipient.getId()));
                if (!customer.isPresent()) {
                    sendLoginInvite(recipient, host);
                }
                executorService.submit(() -> sendAccountOptionsForAccountDetails(recipient));
            } else if (text.get().toLowerCase().contains("thank") || text.get().toLowerCase().contains("bye")) {
                executorService.submit(() -> windChat(recipient));
            } else {
                executorService.submit(() -> unrecognizedMessage(recipient));
            }
        }


        if (quickReply.isPresent()) {
            String payload = quickReply.get().getPayload();
            Option option = mapper.readValue(payload, Option.class);
            if (option.getType().equals(EntityType.ACCOUNTS)) {
                executorService.submit(() -> sendAccountDetails(recipient, option.getId()));
                executorService.submit(() -> {
                    try {
                        sendViewTransactionSuggestion(recipient, option.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (option.getType().equals(EntityType.TRANSACTIONS)) {
                if (option.getAccept() != null) {
                    if (option.getAccept()) {
                        executorService.submit(() -> {
                            try {
                                sendTxDurationOptions(recipient, option.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        continueConversation(recipient);
                    }
                } else {
                    sendTransactionDetails(recipient, option.getId(), option.getValue());
                    continueConversation(recipient);
                }
            }
        }

    }
}
