package com.sadon1on.exchangerateservice.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    boolean existsByDateAndCurrencyCurID(LocalDateTime date, Integer curID);
    ExchangeRate findExchangeRateByDateAndCurrencyCurID(LocalDateTime date, Integer curId);
}
