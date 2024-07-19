package com.sadon1on.exchangerateservice.rate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadon1on.exchangerateservice.TestConfig;
import com.sadon1on.exchangerateservice.currency.Currency;
import com.sadon1on.exchangerateservice.currency.NationalBankRequestService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(TestConfig.class)
class ExchangeRateServiceTest {

    @Mock
    ExchangeRateRepository exchangeRateRepository;
    @Mock
    NationalBankRequestService nationalBankRequestService;

    @InjectMocks
    ExchangeRateService exchangeRateService;

    @Autowired
    private ObjectMapper objectMapper;

    private static ClassPathResource resource;

    @BeforeAll
    static void setUp() {
        resource = new ClassPathResource("currency.json");
    }

    @Test
    void testExistingInDBRates() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2024-07-19T00:00:00");
        Integer currId = 456;
        Currency currency = objectMapper.readValue(resource.getFile(), Currency.class);
        ExchangeRate exchangeRate = new ExchangeRate(date);
        exchangeRate.setCurrency(currency);
        exchangeRate.setRateId(1L);
        exchangeRate.setCurOfficialRate(3.0009);

        when(exchangeRateRepository.existsByDateAndCurrencyCurID(date, currId)).thenReturn(true);
        when(exchangeRateRepository.findExchangeRateByDateAndCurrencyCurID(date, currId)).thenReturn(exchangeRate);

        ExchangeRateDTO result = exchangeRateService.getExchangeRate(date, currId);
        assertNotNull(result);
        assertEquals(date, result.getDate());
        assertEquals(currency.getCurAbbreviation(), result.getCurAbbreviation());
        assertEquals(currency.getCurName(), result.getCurName());
        assertEquals(exchangeRate.getCurOfficialRate(), result.getCurOfficialRate());
    }

    @Test
    void testRatesNotInDB() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2024-07-19T00:00:00");
        Integer currId = 456;
        Currency currency = objectMapper.readValue(resource.getFile(), Currency.class);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(currency.getCurID(), date,
                currency.getCurAbbreviation(), currency.getCurScale(),
                currency.getCurName(), 3.0009);

        when(exchangeRateRepository.existsByDateAndCurrencyCurID(date, currId)).thenReturn(false);
        when(nationalBankRequestService.getExchangeRateFromAPI(date, currId)).thenReturn(exchangeRateDTO);

        ExchangeRateDTO result = exchangeRateService.getExchangeRate(date, currId);
        assertNotNull(result);
        assertEquals(exchangeRateDTO, result);
    }
}