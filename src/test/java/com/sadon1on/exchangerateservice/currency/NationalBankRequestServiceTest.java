package com.sadon1on.exchangerateservice.currency;

import com.sadon1on.exchangerateservice.rate.ExchangeRate;
import com.sadon1on.exchangerateservice.rate.ExchangeRateDTO;
import com.sadon1on.exchangerateservice.rate.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersUriSpec;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NationalBankRequestServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private RestClient restClient;

    @Mock
    private RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private RequestHeadersSpec requestHeadersSpecMock;

    @Mock
    private ResponseSpec responseSpecMock;

    @InjectMocks
    private NationalBankRequestService nationalBankRequestService;

    private Currency currency;
    private ExchangeRateDTO exchangeRateDTO;
    private LocalDateTime date;
    private static final String EXTERNAL_API_UNAVAILABLE_MESSAGE = "National Bank api is unavailable at the moment";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);

        //For some reason if I get currency object from file like I did in other classes
        // it messes up mocks of restClient, so I had to create it manually
        currency = new Currency();
        currency.setCurID(456);
        currency.setCurParentID(190);
        currency.setCurCode(643);
        currency.setCurAbbreviation("RUB");
        currency.setCurName("Российский рубль");
        currency.setCurNameBel("Расійскі рубель");
        currency.setCurNameEng("Russian Ruble");
        currency.setCurQuotName("100 Российских рублей");
        currency.setCurQuotNameBel("100 Расійскіх рублёў");
        currency.setCurQuotNameEng("100 Russian Rubles");
        currency.setCurNameMulti("Российских рублей");
        currency.setCurNameBelMulti("Расійскіх рублёў");
        currency.setCurNameEngMulti("Russian Rubles");
        currency.setCurScale(100);

        date = LocalDateTime.parse("2024-07-19T00:00:00");

        exchangeRateDTO = new ExchangeRateDTO(currency.getCurID(), date,
                currency.getCurAbbreviation(), currency.getCurScale(),
                currency.getCurName(), 3.0009);
    }

    @Test
    void testGetExchangeRateFromAPI() {
        ResponseEntity<ExchangeRateDTO> responseEntity = new ResponseEntity<>(exchangeRateDTO, HttpStatus.OK);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
        when(responseSpecMock.toEntity(any(Class.class))).thenReturn(responseEntity);
        when(currencyRepository.getReferenceById(currency.getCurID())).thenReturn(currency);

        ExchangeRateDTO testDTO = nationalBankRequestService.getExchangeRateFromAPI(date, currency.getCurID());

        verify(restClient, times(1)).get();
        verify(currencyRepository, times(1)).getReferenceById(currency.getCurID());
        verify(exchangeRateRepository, times(1)).save(any(ExchangeRate.class));

        assertEquals(exchangeRateDTO, testDTO);
    }

    @Test
    void testGetAllExchangeRatesFromAPI() {
        List<ExchangeRateDTO> exchangeRateDTOList = Collections.singletonList(exchangeRateDTO);
        ResponseEntity<List<ExchangeRateDTO>> responseEntity = new ResponseEntity<>(exchangeRateDTOList, HttpStatus.OK);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
        when(responseSpecMock.toEntity(any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        when(currencyRepository.getReferenceById(anyInt())).thenReturn(currency);

        boolean result = nationalBankRequestService.getAllExchangeRatesFromAPI(date);

        verify(restClient, times(1)).get();
        verify(currencyRepository, times(1)).getReferenceById(anyInt());
        verify(exchangeRateRepository, times(1)).save(any(ExchangeRate.class));

        assertTrue(result);
    }

    @Test
    void testLoadCurrenciesInfoFromAPI() {
        List<Currency> currencyList = Collections.singletonList(currency);
        ResponseEntity<List<Currency>> responseEntity = new ResponseEntity<>(currencyList, HttpStatus.OK);
        when(responseSpecMock.onStatus(any(), any())).thenReturn(responseSpecMock);
        when(responseSpecMock.toEntity(any(ParameterizedTypeReference.class))).thenReturn(responseEntity);

        nationalBankRequestService.loadCurrenciesInfoFromAPI();

        verify(restClient, times(1)).get();
        verify(currencyRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testAPIUnavailableCall() {
        when(responseSpecMock.onStatus(any(), any()))
                .thenThrow(new NationalBankApiUnavailableException(EXTERNAL_API_UNAVAILABLE_MESSAGE));

        assertThrows(NationalBankApiUnavailableException.class, () -> {
            nationalBankRequestService.getExchangeRateFromAPI(date, currency.getCurID());
        });

        assertThrows(NationalBankApiUnavailableException.class, () -> {
            nationalBankRequestService.getAllExchangeRatesFromAPI(date);
        });

        assertThrows(NationalBankApiUnavailableException.class, () -> {
            nationalBankRequestService.loadCurrenciesInfoFromAPI();
        });
    }

    @Test
    void testIllegalArgumentCall() {
        when(responseSpecMock.onStatus(any(), any()))
                .thenThrow(new IllegalArgumentException("Illegal argument was provided"));

        assertThrows(IllegalArgumentException.class, () -> {
            nationalBankRequestService.getExchangeRateFromAPI(date, currency.getCurID());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            nationalBankRequestService.getAllExchangeRatesFromAPI(date);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            nationalBankRequestService.loadCurrenciesInfoFromAPI();
        });
    }
}