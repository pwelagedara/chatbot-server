package com.pnc.microservices.openapi.chatbot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import static com.pnc.microservices.openapi.chatbot.util.StringLiterals.CUSTOMER;

/**
 * @author Palamayuran
 */
@Configuration
@EnableWebSecurity
public class ChatbotSecurityConfigurer extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests().antMatchers("/**/login").authenticated()
                .and().authorizeRequests().antMatchers("/**").permitAll()
                .and().formLogin()
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(false).inMemoryAuthentication()
                .withUser("mayduncan").password("mayduncan").roles(CUSTOMER).and()
                .withUser("libbywaldron").password("libbywaldron").roles(CUSTOMER).and()
                .withUser("palmahite").password("palmahite").roles(CUSTOMER).and()
                .withUser("jettieshuman").password("jettieshuman").roles(CUSTOMER).and()
                .withUser("gilreilly").password("gilreilly").roles(CUSTOMER).and()
                .withUser("bridgepoint").password("bridgepoint").roles(CUSTOMER).and()
                .withUser("swinds").password("swinds").roles(CUSTOMER).and()
                .withUser("smadden").password("smadden").roles(CUSTOMER).and()
                .withUser("ipgpc").password("ipgpc").roles(CUSTOMER).and()
                .withUser("ejones").password("ejones").roles(CUSTOMER).and()
                .withUser("peterjpseph").password("peterjpseph").roles(CUSTOMER).and()
                .withUser("philjackey").password("philjackey").roles(CUSTOMER).and()

                .withUser("mayduncan128").password("mayduncan128").roles(CUSTOMER).and()
                .withUser("libbywaldron128").password("libbywaldron128").roles(CUSTOMER).and()
                .withUser("palmahite128").password("palmahite128").roles(CUSTOMER).and()
                .withUser("jettieshuman128").password("jettieshuman128").roles(CUSTOMER).and()
                .withUser("gilreilly128").password("gilreilly128").roles(CUSTOMER).and()
                .withUser("bridgepoint128").password("bridgepoint128").roles(CUSTOMER).and()
                .withUser("swinds128").password("swinds128").roles(CUSTOMER).and()
                .withUser("smadden128").password("smadden128").roles(CUSTOMER).and()
                .withUser("ipgpc128").password("ipgpc128").roles(CUSTOMER).and()
                .withUser("ejones128").password("ejones128").roles(CUSTOMER).and()
                .withUser("peterjpseph128").password("peterjpseph128").roles(CUSTOMER).and()
                .withUser("philjackey128").password("philjackey128").roles(CUSTOMER);
    }
}
