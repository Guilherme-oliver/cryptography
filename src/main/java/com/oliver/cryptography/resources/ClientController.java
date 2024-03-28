package com.oliver.cryptography.resources;

import com.oliver.cryptography.dto.ClientRequest;
import com.oliver.cryptography.dto.ClientResponse;
import com.oliver.cryptography.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        ClientResponse dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> findAll(Pageable pageable) {
        Page<ClientResponse> dto = service.findAll(pageable);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ClientResponse> insert(@Valid @RequestBody ClientRequest response) throws NoSuchAlgorithmException {
        var dto = service.insert(response);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable Long id, @Valid @RequestBody ClientRequest response) throws NoSuchAlgorithmException {
        var dto = service.update(id, response);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}