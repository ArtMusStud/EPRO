package com.epro.ws2122.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Todo:
        - implement method, add queries for filtering by time intervals
 */
@RestController
public class DashboardController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("dashboard not implemented yet");
    }
}
