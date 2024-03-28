package com.oliver.cryptography.services;

import com.oliver.cryptography.dto.ClientRequest;
import com.oliver.cryptography.dto.ClientResponse;
import com.oliver.cryptography.entities.Client;
import com.oliver.cryptography.repositories.ClientRepository;
import com.oliver.cryptography.services.exceptions.DatabaseException;
import com.oliver.cryptography.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    private void copyDtoToEntity(ClientRequest response, Client entity) {
        entity.setUserDocument(response.getUserDocument());
        entity.setCreditCardToken(response.getCreditCardToken());
        entity.setResultValue(response.getResultValue());
    }

    public ClientResponse findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Resource not found!"));
        return new ClientResponse(client);
    }

    public Page<ClientResponse> findAll(Pageable pageable) {
        Page<Client> result = clientRepository.findAll(pageable);
        return result.map(x -> new ClientResponse(x));
    }

    public ClientResponse insert(ClientRequest client) throws NoSuchAlgorithmException {
        Client cli = new Client();
        String userDocumentEncrypted = CryptographyService.generate(client.getUserDocument());
        String creditCardTokenEncrypted = CryptographyService.generate(client.getCreditCardToken());
        client.setUserDocument(userDocumentEncrypted);
        client.setCreditCardToken(creditCardTokenEncrypted);
        copyDtoToEntity(client, cli);
        cli = clientRepository.save(cli);
        return new ClientResponse(cli);
    }

    public ClientResponse update(Long id, ClientRequest client) throws NoSuchAlgorithmException {
        try {
            Optional<Client> optionalClient = clientRepository.findById(id);
            if (!optionalClient.isPresent()) {
                throw new EntityNotFoundException("Client with ID " + id + " not found!");
            }
            Client existingClient = optionalClient.get();
            String userDocumentEncrypted = CryptographyService.generate(client.getUserDocument());
            String creditCardTokenEncrypted = CryptographyService.generate(client.getCreditCardToken());
            copyDtoToEntity(client, existingClient);
            existingClient.setUserDocument(userDocumentEncrypted);
            existingClient.setCreditCardToken(creditCardTokenEncrypted);
            existingClient = clientRepository.save(existingClient);
            return new ClientResponse(existingClient);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found!");
        }
    }

    public ClientResponse recoverUser(Long id) {
        Client client = clientRepository.getReferenceById(id);

        if (client != null) {
            client.setUserDocument(CryptographyService.decrypt(client.getUserDocument()));
            client.setCreditCardToken(CryptographyService.decrypt(client.getCreditCardToken()));
        }
        return new ClientResponse(client);
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found!");
        }
        try {
            clientRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referential integrity failure!");
        }
    }
}