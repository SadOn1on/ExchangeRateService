package com.sadon1on.exchangerateservice.rate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class NationalBankApiUnavailableException extends RuntimeException{
    public NationalBankApiUnavailableException(String message) {
        super(message);
    }
}
