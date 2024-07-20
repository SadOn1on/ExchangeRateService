package com.sadon1on.exchangerateservice.rate;

import com.sadon1on.exchangerateservice.currency.Currency;
import com.sadon1on.exchangerateservice.currency.NationalBankRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * This class is responsible for fetching exchange rates from the database or the National Bank API.
 * It provides a method for getting the exchange rate for a specific currency on a specific date.
 */
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

    /**
     * This method returns the exchange rate for a specific currency on a specific date.
     * If the exchange rate is not found in the database, it will be fetched from the National Bank API.
     * @param date the date on which the exchange rate is set
     * @param currId the unique id of the currency
     * @return the exchange rate for the specified currency on the specified date
     **/
    public ExchangeRateDTO getExchangeRate(LocalDateTime date, Integer currId)  {
        if (exchangeRateRepository.existsByDateAndCurrencyCurID(date, currId)) {
            ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByDateAndCurrencyCurID(date, currId);
            Currency currency = exchangeRate.getCurrency();
            String name = currency.getCurScale() > 1 ? currency.getCurNameMulti() : currency.getCurName();
            return new ExchangeRateDTO(currency.getCurID(), exchangeRate.getDate(), currency.getCurAbbreviation(),
                    currency.getCurScale(), name, exchangeRate.getCurOfficialRate());
        } else {
            return nationalBankRequestService.getExchangeRateFromAPI(date, currId);
        }
    }
}
