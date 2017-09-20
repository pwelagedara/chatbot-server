package com.pnc.microservices.openapi.chatbot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Palamayuran
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Party implements Serializable {

    @JsonProperty("person")
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Person{
        @JsonProperty("forename")
        private String firstName;

        @JsonProperty("lastname")
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

}
