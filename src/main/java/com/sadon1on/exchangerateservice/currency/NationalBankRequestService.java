package com.sadon1on.exchangerateservice.currency;

import com.sadon1on.exchangerateservice.rate.ExchangeRate;
import com.sadon1on.exchangerateservice.rate.ExchangeRateDTO;
import com.sadon1on.exchangerateservice.rate.ExchangeRateRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;


/**
 * This class is responsible for fetching data from the National Bank API.
 * It provides methods for fetching exchange rates for specific currencies on specific dates,
 * fetching all exchange rates for all currencies on specific dates, and fetching all currencies information.
 */
@Service
public class NationalBankRequestService {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestClient restClient;
    private static final String EXTERNAL_API_UNAVAILABLE_MESSAGE = "National Bank api is unavailable at the moment";

    @Autowired
    public NationalBankRequestService(CurrencyRepository currencyRepository,
                                      ExchangeRateRepository exchangeRateRepository,
                                      RestClient restClient) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.restClient = restClient;
    }

    /**
     * This method fetches the exchange rate for a specific currency on a specific date from the National Bank API.
     *
     * @param date   the date on which the exchange rate is set
     * @param currId the unique id of the currency
     * @return the exchange rate for the specified currency on the specified
     * @throws IllegalArgumentException            if an illegal argument is provided
     * @throws NationalBankApiUnavailableException if the National Bank API is unavailable
     */
    public ExchangeRateDTO getExchangeRateFromAPI(LocalDateTime date, Integer currId) {
        ResponseEntity<ExchangeRateDTO> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rates/{currId}")
                        .queryParam("onDate", date.toString())
                        .build(currId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, resp) -> {
                    throw new IllegalArgumentException("Illegal argument was provided");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, resp) -> {
                    throw new NationalBankApiUnavailableException(EXTERNAL_API_UNAVAILABLE_MESSAGE);
                })
                .toEntity(ExchangeRateDTO.class);

        ExchangeRateDTO dto = response.getBody();
        ExchangeRate exchangeRate = new ExchangeRate(dto.getDate());
        exchangeRate.setCurrency(currencyRepository.getReferenceById(dto.getId()));
        exchangeRate.setCurOfficialRate(dto.getCurOfficialRate());
        exchangeRateRepository.save(exchangeRate);
        return dto;
    }

    //generate documentation for this method

    /**
     * This method fetches all exchange rates for all currencies from the National Bank API on a specific date.
     *
     * @param date the date on which the exchange rates are set
     * @return true if all exchange rates are fetched successfully
     * @throws IllegalArgumentException            if an illegal argument is provided
     * @throws NationalBankApiUnavailableException if the National Bank API is unavailable
     */
    public boolean getAllExchangeRatesFromAPI(LocalDateTime date) {
        ResponseEntity<List<ExchangeRateDTO>> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/rates")
                        .queryParam("onDate", date.toString())
                        .queryParam("periodicity", 0)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, resp) -> {
                    throw new IllegalArgumentException("Illegal argument was provided");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, resp) -> {
                    throw new NationalBankApiUnavailableException(EXTERNAL_API_UNAVAILABLE_MESSAGE);
                })
                .toEntity(new ParameterizedTypeReference<List<ExchangeRateDTO>>() {
                });

        List<ExchangeRateDTO> exchangeRates = response.getBody();
        for (ExchangeRateDTO dto : exchangeRates) {
            ExchangeRate temp = new ExchangeRate(dto.getDate());
            temp.setCurOfficialRate(dto.getCurOfficialRate());
            temp.setCurrency(currencyRepository.getReferenceById(dto.getId()));
            exchangeRateRepository.save(temp);
        }
        return true;
    }

    /**
     * This method fetches all currencies information from the National Bank API.
     * It is called immediately after the application starts to ensure that all currencies are available in the database.
     *
     * @throws NationalBankApiUnavailableException if the National Bank API is unavailable
     */
    @PostConstruct
    public void loadCurrenciesInfoFromAPI() {
        ResponseEntity<List<Currency>> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/currencies")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, (request, resp) -> {
                    throw new NationalBankApiUnavailableException(EXTERNAL_API_UNAVAILABLE_MESSAGE);
                })
                .toEntity(new ParameterizedTypeReference<List<Currency>>() {
                });

        List<Currency> currencies = response.getBody();
        currencyRepository.saveAll(currencies);
    }
}
