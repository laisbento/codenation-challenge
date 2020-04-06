package com.codenation.client;

import com.codenation.model.CodenationModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(
        name = "codenation-client",
        url = "${codenation.feign.url}"
)
public interface CodenationClient {

    @GetMapping(path = "v1/challenge/dev-ps/generate-data")
    CodenationModel getMessage(@RequestParam String token);

    @PostMapping(path = "v1/challenge/dev-ps/submit-solution", headers = "multipart/form-data", consumes = "multipart/form-data")
    void submitTest(@RequestParam String token,
                    @RequestBody MultipartFile answer);
}
