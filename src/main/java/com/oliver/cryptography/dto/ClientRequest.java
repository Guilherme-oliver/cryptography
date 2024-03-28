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
public class ClientRequest {

    private Long id;
    private String userDocument;
    private String creditCardToken;
    private Long resultValue;

    public ClientRequest(Client client) {
        id = client.getId();
        userDocument = client.getUserDocument();
        creditCardToken = client.getCreditCardToken();
        resultValue = client.getResultValue();
    }
}