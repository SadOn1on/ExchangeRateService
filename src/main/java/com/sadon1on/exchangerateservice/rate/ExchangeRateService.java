package com.sadon1on.exchangerateservice.rate;

import com.sadon1on.exchangerateservice.currency.NationalBankRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final NationalBankRequestService nationalBankRequestService;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository,
                               NationalBankRequestService nationalBankRequestService) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.nationalBankRequestService = nationalBankRequestService;
    }

    public ExchangeRateDTO getExchangeRate(LocalDateTime date, Integer currId)  {
        if (exchangeRateRepository.existsByDateAndCurrencyCurID(date, currId)) {
            ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByDateAndCurrencyCurID(date, currId);
            return new ExchangeRateDTO(exchangeRate.getCurrency().getCurID(), exchangeRate.getDate(),
                    exchangeRate.getCurrency().getCurAbbreviation(), exchangeRate.getCurrency().getCurScale(),
                    exchangeRate.getCurrency().getCurName(), exchangeRate.getCurOfficialRate());
        } else {
            return nationalBankRequestService.getExchangeRateFromAPI(date, currId);
        }
    }
}
