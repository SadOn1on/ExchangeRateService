package com.sadon1on.exchangerateservice.rate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadon1on.exchangerateservice.currency.Currency;
import com.sadon1on.exchangerateservice.currency.NationalBankRequestService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExchangeRateController.class)
class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @MockBean
    private NationalBankRequestService nationalBankRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private static ClassPathResource resource;

    @BeforeAll
    static void setUp() {
        resource = new ClassPathResource("currency.json");
    }

    @Test
    void getExchangeRate() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse("2024-07-19T00:00:00");
        Integer currId = 456;
        Currency currency = objectMapper.readValue(resource.getFile(), Currency.class);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(currency.getCurID(), date,
                currency.getCurAbbreviation(), currency.getCurScale(),
                currency.getCurName(), 3.0009);

        when(exchangeRateService.getExchangeRate(date, currId)).thenReturn(exchangeRateDTO);

        mockMvc.perform(get("/exrates")
                        .param("currId", currId.toString())
                        .param("date", date.toLocalDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.Date").value(date.format(formatter)))
                .andExpect(jsonPath("$.Cur_ID").value(currency.getCurID()))
                .andExpect(jsonPath("$.Cur_Abbreviation").value(currency.getCurAbbreviation()))
                .andExpect(jsonPath("$.Cur_Scale").value(currency.getCurScale()))
                .andExpect(jsonPath("$.Cur_Name").value(currency.getCurName()))
                .andExpect(jsonPath("$.Cur_OfficialRate").value(3.0009));
    }

    @Test
    void testGetExchangeRateWithIllegalId() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2024-07-19T00:00:00");
        Integer currId = 4561;

        when(exchangeRateService.getExchangeRate(date, currId)).thenThrow(
                new IllegalArgumentException("Illegal argument was provided"));

        mockMvc.perform(get("/exrates")
                        .param("currId", currId.toString())
                        .param("date", date.toLocalDate().toString()))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testGetExchangeRateWithNotWorkingAPI() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2024-07-19T00:00:00");
        Integer currId = 4561;

        when(exchangeRateService.getExchangeRate(date, currId)).thenThrow(
                new NationalBankApiUnavailableException("National Bank api is unavailable at the moment"));

        mockMvc.perform(get("/exrates")
                        .param("currId", currId.toString())
                        .param("date", date.toLocalDate().toString()))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void testLoadData() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2024-07-19T00:00:00");

        when(nationalBankRequestService.getAllExchangeRatesFromAPI(date)).thenReturn(true);


        mockMvc.perform(post("/exrates")
                        .param("date", date.toLocalDate().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void testLoadDataWithNotWorkingAPI() throws Exception {
        LocalDateTime date = LocalDateTime.parse("2024-07-19T00:00:00");

        when(nationalBankRequestService.getAllExchangeRatesFromAPI(date)).thenThrow(
                new NationalBankApiUnavailableException("National Bank api is unavailable at the moment"));


        mockMvc.perform(post("/exrates")
                        .param("date", date.toLocalDate().toString()))
                .andExpect(status().isServiceUnavailable());
    }
}