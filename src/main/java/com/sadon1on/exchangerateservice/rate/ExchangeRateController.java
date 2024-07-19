package com.sadon1on.exchangerateservice.rate;

import com.sadon1on.exchangerateservice.currency.NationalBankRequestService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/exrates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    private final NationalBankRequestService nationalBankRequestService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService,
                                  NationalBankRequestService nationalBankRequestService) {
        this.exchangeRateService = exchangeRateService;
        this.nationalBankRequestService = nationalBankRequestService;
    }

    @GetMapping
    public ExchangeRateDTO exchangeRate(@RequestParam LocalDate date, @RequestParam Integer currId) {
        return exchangeRateService.getExchangeRate(date.atStartOfDay(), currId);
    }

    @PostMapping
    public void loadData(@RequestParam LocalDate date, HttpServletResponse response) {
        nationalBankRequestService.getAllExchangeRatesFromAPI(date.atStartOfDay());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid request: " + ex.getMessage());
    }
}
