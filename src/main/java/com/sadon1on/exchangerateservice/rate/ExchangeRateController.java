package com.sadon1on.exchangerateservice.rate;

import com.sadon1on.exchangerateservice.currency.NationalBankRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/exrates")
@Tag(name = "Exchange rate controller")
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
    @Operation(summary = "Request exchange rate for particular currency on date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Invalid parameter was supplied",
                    content = {
                            @Content(schema = @Schema(implementation = Void.class))
                    }),
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ExchangeRateDTO.class))
                    }),
            @ApiResponse(responseCode = "503",
                    description = "National bank API is unavailable at the moment",
                    content = {
                            @Content(schema = @Schema(implementation = Void.class))
                    })
    })
    public ExchangeRateDTO exchangeRate(
            @RequestParam @Parameter(description = "Date on which the exchange rate is set",
                    example = "2023-01-10") LocalDate date,
            @RequestParam @Parameter(description = "Currency id") Integer currId) {
        return exchangeRateService.getExchangeRate(date.atStartOfDay(), currId);
    }

    @PostMapping
    @Operation(summary = "Load exchange rates for all currencies avalible on date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Data was successfully uploaded to the database"),
            @ApiResponse(responseCode = "503",
                    description = "National bank API is unavailable at the moment"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid data format")
    })
    public void loadData(
            @RequestParam @Parameter(description = "Date on which the exchange rate is set",
                    example = "2023-01-10") LocalDate date) {
        nationalBankRequestService.getAllExchangeRatesFromAPI(date.atStartOfDay());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid request: " + ex.getMessage());
    }
}
