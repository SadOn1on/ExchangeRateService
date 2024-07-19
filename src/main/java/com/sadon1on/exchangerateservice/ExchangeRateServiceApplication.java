package com.sadon1on.exchangerateservice;

import com.sadon1on.exchangerateservice.currency.NationalBankRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExchangeRateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRateServiceApplication.class, args);
    }

}
