package com.oliver.cryptography.dto;

import com.oliver.cryptography.entities.Client;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank
    private String userDocument;
    @NotBlank
    private String creditCardToken;
    @NotNull @Positive
    private Long resultValue;

    public ClientRequest(Client client) {
        id = client.getId();
        userDocument = client.getUserDocument();
        creditCardToken = client.getCreditCardToken();
        resultValue = client.getResultValue();
    }
}