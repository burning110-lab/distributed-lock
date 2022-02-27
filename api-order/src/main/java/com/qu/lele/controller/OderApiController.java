package com.qu.lele.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author: 屈光乐
 * @create: 2022-02-24 20-14
 */
@RestController
@RequestMapping(value = "/order")
public class OderApiController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/do/{goodId}")
    public String grabOrder(@PathVariable("goodId") String goodId,String userId) {
        String url = "http://service-order/secKill/inventory/"+ goodId + "/" + userId;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
