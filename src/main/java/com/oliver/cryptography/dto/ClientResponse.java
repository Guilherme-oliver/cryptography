package com.oliver.cryptography.dto;

import com.oliver.cryptography.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientResponse {

    private String userDocument;
    private String creditCardToken;
    private Long resultValue;

    public ClientResponse(Client client) {
        userDocument = client.getUserDocument();
        creditCardToken = client.getCreditCardToken();
        resultValue = client.getResultValue();
    }
}