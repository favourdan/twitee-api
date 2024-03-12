package org.tm30.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fb")
public class FallbackController {

    @PostMapping(value = "/twit")
    public ResponseEntity<HttpStatus> ticketFallback(){
        return ResponseEntity.ok(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<HttpStatus> reservationFallback(){
        return ResponseEntity.ok(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PostMapping(value = "/swagger")
    public ResponseEntity<HttpStatus> reservationFallbackForSwagger(){
        return ResponseEntity.ok(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
