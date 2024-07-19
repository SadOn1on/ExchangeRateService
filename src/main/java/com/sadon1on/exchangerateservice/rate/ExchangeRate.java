package com.sadon1on.exchangerateservice.rate;

import com.sadon1on.exchangerateservice.currency.Currency;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue
    @Column(name = "rate_id")
    private Long rateId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "cur_official_rate")
    private Double curOfficialRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cur_id", nullable = false)
    private Currency currency;

    public ExchangeRate() {
    }

    public ExchangeRate(LocalDateTime date) {
        this.date = date;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getCurOfficialRate() {
        return curOfficialRate;
    }

    public void setCurOfficialRate(Double curOfficialRate) {
        this.curOfficialRate = curOfficialRate;
    }
}
