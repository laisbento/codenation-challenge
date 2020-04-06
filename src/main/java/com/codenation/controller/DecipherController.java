package com.codenation.controller;

import com.codenation.service.DecipherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DecipherController {

    private DecipherService decipherService;

    @Autowired
    public DecipherController(DecipherService decipherService) {
        this.decipherService = decipherService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void decipherNow(@RequestParam String token) throws IOException {
        decipherService.letsDecipher(token);
    }
}
