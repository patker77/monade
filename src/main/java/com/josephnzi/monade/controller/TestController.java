package com.josephnzi.monade.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {
    @GetMapping("/")
    public ResponseEntity<String> getSecret(){
         return ResponseEntity.ok("welekom to the secret page");
    }
}
