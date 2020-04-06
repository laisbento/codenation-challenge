package com.codenation.service;

import com.codenation.client.CodenationClient;
import com.codenation.model.CodenationModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class DecipherService {

    private CodenationClient client;

    @Autowired
    public DecipherService(CodenationClient client) {
        this.client = client;
    }

    public void letsDecipher(String token) throws IOException {
        CodenationModel response = client.getMessage(token);

        char[] decifrado = getMessage(response);
        String message = String.copyValueOf(decifrado);
        response.setDecifrado(message);

        setCryptographySummary(response, message);

        MultipartFile multipartFile = getRequestFile(response);

        client.submitTest(token, multipartFile);
    }

    private MultipartFile getRequestFile(CodenationModel response) throws IOException {
        File file = new File("answer.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, response);
        InputStream inputStream = new FileInputStream(file);

        return new MockMultipartFile("answer", "answer.json", "application/json", IOUtils.toByteArray(inputStream));
    }

    private void setCryptographySummary(CodenationModel response, String message) {
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("SHA-1");
            instance.reset();
            instance.update(message.getBytes("utf8"));
            String resumo = String.format("%040x", new BigInteger(1, instance.digest()));
            response.setResumo_criptografico(resumo);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private char[] getMessage(CodenationModel response) {
        Integer casas = response.getNumero_casas();
        String cifrado = response.getCifrado();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] codigo = cifrado.toCharArray();
        char[] decifrado = new char[codigo.length];

        int i = 0;

        for (char character : codigo) {
            int j = 0;
            for (; j < alphabet.length; j++) {
                if (character == ' ') {
                    decifrado[i] = ' ';
                } else if (character == '.') {
                    decifrado[i] = '.';
                } else if (alphabet[j] == character) {
                    int index;
                    if (j < 11) {
                        index = (j - 11) + 26;
                    } else {
                        index = j - casas;
                    }
                    char correct = alphabet[index];
                    decifrado[i] = correct;
                }
            }
            i++;
        }

        return decifrado;
    }
}
