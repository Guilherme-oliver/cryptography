package com.oliver.cryptography.service;

import com.oliver.cryptography.dto.ClientRequest;
import com.oliver.cryptography.dto.ClientResponse;
import com.oliver.cryptography.entities.Client;
import com.oliver.cryptography.repositories.ClientRepository;
import com.oliver.cryptography.services.ClientService;
import com.oliver.cryptography.services.CryptographyService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

import com.oliver.cryptography.services.exceptions.DatabaseException;
import com.oliver.cryptography.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Page<Client> page = createClientPage();
        Pageable pageable = PageRequest.of(0, 10);

        when(clientRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ClientResponse> result = clientService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("userDocument1", result.getContent().get(0).getUserDocument());
        assertEquals("userDocument2", result.getContent().get(1).getUserDocument());
    }

    @Test
    public void testInsert() throws NoSuchAlgorithmException {
        ClientRequest clientRequest = createClientRequest();
        Client savedClient = createClientFromRequest(clientRequest);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponse clientResponse = clientService.insert(clientRequest);

        assertClientResponse(savedClient, clientResponse);

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testInsertFailure() throws NoSuchAlgorithmException {
        ClientRequest clientRequest = createClientRequest();

        when(clientRepository.save(any(Client.class))).thenThrow(new RuntimeException("Erro ao salvar cliente"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clientService.insert(clientRequest);
        });

        assertEquals("Erro ao salvar", exception.getMessage());

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testUpdate() throws NoSuchAlgorithmException {
        Long id = 1L;
        ClientRequest clientRequest = createClientRequest();
        Client existingClient = createExistingClient(id);

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));

        Client updatedClient = createClientFromRequest(clientRequest);
        updatedClient.setId(id);

        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);

        ClientResponse clientResponse = clientService.update(id, clientRequest);

        assertClientResponse(updatedClient, clientResponse);

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void testUpdateClientNotFound() {
        Long id = 1L;
        ClientRequest clientRequest = createClientRequest();

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.update(id, clientRequest);
        });

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(0)).save(any(Client.class));
    }

    @Test
    void testDelete_SuccessfulDeletion() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> clientService.delete(id));

        verify(clientRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_ResourceNotFoundException() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> clientService.delete(id));
        assertEquals("Resource not found!", exception.getMessage());

        verify(clientRepository, never()).deleteById(id);
    }

    @Test
    void testDelete_DatabaseException() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("")).when(clientRepository).deleteById(id);

        Exception exception = assertThrows(DatabaseException.class, () -> clientService.delete(id));
        assertEquals("Referential integrity failure!", exception.getMessage());

        verify(clientRepository, times(1)).deleteById(id);
    }

    private ClientRequest createClientRequest() {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setUserDocument("123456789");
        clientRequest.setCreditCardToken("987654321");
        return clientRequest;
    }

    private Client createClientFromRequest(ClientRequest clientRequest) throws NoSuchAlgorithmException {
        Client client = new Client();
        client.setUserDocument(CryptographyService.generate(clientRequest.getUserDocument()));
        client.setCreditCardToken(CryptographyService.generate(clientRequest.getCreditCardToken()));
        client.setResultValue(clientRequest.getResultValue());
        return client;
    }

    private Client createExistingClient(Long id) {
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setUserDocument("OLD_DOCUMENT");
        existingClient.setCreditCardToken("OLD_TOKEN");
        return existingClient;
    }

    private Page<Client> createClientPage() {
        Client client1 = new Client();
        client1.setId(1L);
        client1.setUserDocument("userDocument1");
        client1.setCreditCardToken("creditCardToken1");
        client1.setResultValue(100L);

        Client client2 = new Client();
        client2.setId(2L);
        client2.setUserDocument("userDocument2");
        client2.setCreditCardToken("creditCardToken2");
        client2.setResultValue(200L);

        return new PageImpl<>(Arrays.asList(client1, client2));
    }

    private void assertClientResponse(Client client, ClientResponse response) {
        assertNotNull(response);
        assertEquals(client.getUserDocument(), response.getUserDocument());
        assertEquals(client.getCreditCardToken(), response.getCreditCardToken());
        assertEquals(client.getResultValue(), response.getResultValue());
    }
}