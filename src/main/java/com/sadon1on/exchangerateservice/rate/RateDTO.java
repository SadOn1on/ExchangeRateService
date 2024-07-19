package com.sadon1on.exchangerateservice.rate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RateDTO {

    private Integer Cur_ID;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("Date")
    private LocalDateTime date;
    private String curAbbreviation;
    private Long curScale;
    private String curName;
    private Double curOfficialRate;

    public RateDTO() {
    }

    public RateDTO(Integer curId, LocalDateTime date, String curAbbreviation, Long curScale, String curName, Double curOfficialRate) {
        this.Cur_ID = curId;
        this.date = date;
        this.curAbbreviation = curAbbreviation;
        this.curScale = curScale;
        this.curName = curName;
        this.curOfficialRate = curOfficialRate;
    }

    public Integer getCur_ID() {
        return Cur_ID;
    }

    public void setCur_ID(Integer curId) {
        this.Cur_ID = curId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCur_Abbreviation() {
        return curAbbreviation;
    }

    public void setCur_Abbreviation(String curAbbreviation) {
        this.curAbbreviation = curAbbreviation;
    }

    public Long getCurScale() {
        return curScale;
    }

    public void setCurScale(Long curScale) {
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
}
