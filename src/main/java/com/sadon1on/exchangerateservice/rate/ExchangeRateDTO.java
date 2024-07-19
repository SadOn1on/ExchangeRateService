package com.sadon1on.exchangerateservice.rate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ExchangeRateDTO {
    @JsonProperty("Cur_ID")
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("Date")
    private LocalDateTime date;

    @JsonProperty("Cur_Abbreviation")
    private String curAbbreviation;

    @JsonProperty("Cur_Scale")
    private int curScale;

    @JsonProperty("Cur_Name")
    private String curName;

    @JsonProperty("Cur_OfficialRate")
    private Double curOfficialRate;

    public ExchangeRateDTO() {
    }

    public ExchangeRateDTO(Integer id, LocalDateTime date, String curAbbreviation, int curScale,
                           String curName, Double curOfficialRate) {
        this.id = id;
        this.date = date;
        this.curAbbreviation = curAbbreviation;
        this.curScale = curScale;
        this.curName = curName;
        this.curOfficialRate = curOfficialRate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCurAbbreviation() {
        return curAbbreviation;
    }

    public void setCurAbbreviation(String curAbbreviation) {
        this.curAbbreviation = curAbbreviation;
    }

    public int getCurScale() {
        return curScale;
    }

    public void setCurScale(int curScale) {
        this.curScale = curScale;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public Double getCurOfficialRate() {
        return curOfficialRate;
    }

    public void setCurOfficialRate(Double curOfficialRate) {
        this.curOfficialRate = curOfficialRate;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRateDTO that)) return false;

        return curScale == that.curScale && date.equals(that.date) && curAbbreviation.equals(that.curAbbreviation) && curName.equals(that.curName) && curOfficialRate.equals(that.curOfficialRate);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + curAbbreviation.hashCode();
        result = 31 * result + curScale;
        result = 31 * result + curName.hashCode();
        result = 31 * result + curOfficialRate.hashCode();
        return result;
    }
}
