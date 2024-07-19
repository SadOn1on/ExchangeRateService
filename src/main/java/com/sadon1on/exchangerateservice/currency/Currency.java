package com.sadon1on.exchangerateservice.currency;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sadon1on.exchangerateservice.rate.ExchangeRate;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Currency {
    @Id
    @Column(name = "cur_id")
    @JsonProperty("Cur_ID")
    private Integer curID;

    @Column(name = "cur_parentid")
    @JsonProperty("Cur_ParentID")
    private Integer curParentID;

    @Column(name = "cur_code")
    @JsonProperty("Cur_Code")
    private int curCode;

    @Column(name = "cur_abbreviation")
    @JsonProperty("Cur_Abbreviation")
    private String curAbbreviation;

    @Column(name = "cur_name")
    @JsonProperty("Cur_Name")
    private String curName;

    @Column(name = "cur_name_bel")
    @JsonProperty("Cur_Name_Bel")
    private String curNameBel;

    @Column(name = "cur_name_eng")
    @JsonProperty("Cur_Name_Eng")
    private String curNameEng;

    @Column(name = "cur_quotname")
    @JsonProperty("Cur_QuotName")
    private String curQuotName;

    @Column(name = "cur_quotname_bel")
    @JsonProperty("Cur_QuotName_Bel")
    private String curQuotNameBel;

    @Column(name = "cur_quotname_eng")
    @JsonProperty("Cur_QuotName_Eng")
    private String curQuotNameEng;

    @Column(name = "cur_namemulti")
    @JsonProperty("Cur_NameMulti")
    private String curNameMulti;

    @Column(name = "cur_name_belmulti")
    @JsonProperty("Cur_Name_BelMulti")
    private String curNameBelMulti;

    @Column(name = "cur_name_engmulti")
    @JsonProperty("Cur_Name_EngMulti")
    private String curNameEngMulti;

    @Column(name = "cur_scale")
    @JsonProperty("Cur_Scale")
    private int curScale;

    @Column(name = "cur_periodicity")
    @JsonProperty("Cur_Periodicity")
    private int curPeriodicity;

    @Column(name = "cur_datestart")
    @JsonProperty("Cur_DateStart")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime curDateStart;

    @Column(name = "cur_dateend")
    @JsonProperty("Cur_DateEnd")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime curDateEnd;

    @JsonIgnore
    @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY)
    private List<ExchangeRate> exchangeRateList;

    public Currency() {
    }

    public Currency(Integer curID, Integer curParentID, int curCode, String curAbbreviation, String curName,
                    String curNameBel, String curNameEng, String curQuotName, String curQuotNameBel,
                    String curQuotNameEng, String curNameMulti, String curNameBelMulti, String curNameEngMulti,
                    int curScale, int curPeriodicity, LocalDateTime curDateStart, LocalDateTime curDateEnd) {
        this.curID = curID;
        this.curParentID = curParentID;
        this.curCode = curCode;
        this.curAbbreviation = curAbbreviation;
        this.curName = curName;
        this.curNameBel = curNameBel;
        this.curNameEng = curNameEng;
        this.curQuotName = curQuotName;
        this.curQuotNameBel = curQuotNameBel;
        this.curQuotNameEng = curQuotNameEng;
        this.curNameMulti = curNameMulti;
        this.curNameBelMulti = curNameBelMulti;
        this.curNameEngMulti = curNameEngMulti;
        this.curScale = curScale;
        this.curPeriodicity = curPeriodicity;
        this.curDateStart = curDateStart;
        this.curDateEnd = curDateEnd;
    }

    public Integer getCurID() {
        return curID;
    }

    public void setCurID(Integer curID) {
        this.curID = curID;
    }

    public Integer getCurParentID() {
        return curParentID;
    }

    public void setCurParentID(Integer curParentID) {
        this.curParentID = curParentID;
    }

    public int getCurCode() {
        return curCode;
    }

    public void setCurCode(int curCode) {
        this.curCode = curCode;
    }

    public String getCurAbbreviation() {
        return curAbbreviation;
    }

    public void setCurAbbreviation(String curAbbreviation) {
        this.curAbbreviation = curAbbreviation;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getCurNameBel() {
        return curNameBel;
    }

    public void setCurNameBel(String curNameBel) {
        this.curNameBel = curNameBel;
    }

    public String getCurNameEng() {
        return curNameEng;
    }

    public void setCurNameEng(String curNameEng) {
        this.curNameEng = curNameEng;
    }

    public String getCurQuotName() {
        return curQuotName;
    }

    public void setCurQuotName(String curQuotName) {
        this.curQuotName = curQuotName;
    }

    public String getCurQuotNameBel() {
        return curQuotNameBel;
    }

    public void setCurQuotNameBel(String curQuotNameBel) {
        this.curQuotNameBel = curQuotNameBel;
    }

    public String getCurQuotNameEng() {
        return curQuotNameEng;
    }

    public void setCurQuotNameEng(String curQuotNameEng) {
        this.curQuotNameEng = curQuotNameEng;
    }

    public String getCurNameMulti() {
        return curNameMulti;
    }

    public void setCurNameMulti(String curNameMulti) {
        this.curNameMulti = curNameMulti;
    }

    public String getCurNameBelMulti() {
        return curNameBelMulti;
    }

    public void setCurNameBelMulti(String curNameBelMulti) {
        this.curNameBelMulti = curNameBelMulti;
    }

    public String getCurNameEngMulti() {
        return curNameEngMulti;
    }

    public void setCurNameEngMulti(String curNameEngMulti) {
        this.curNameEngMulti = curNameEngMulti;
    }

    public int getCurScale() {
        return curScale;
    }

    public void setCurScale(int curScale) {
        this.curScale = curScale;
    }

    public int getCurPeriodicity() {
        return curPeriodicity;
    }

    public void setCurPeriodicity(int curPeriodicity) {
        this.curPeriodicity = curPeriodicity;
    }

    public LocalDateTime getCurDateStart() {
        return curDateStart;
    }

    public void setCurDateStart(LocalDateTime curDateStart) {
        this.curDateStart = curDateStart;
    }

    public LocalDateTime getCurDateEnd() {
        return curDateEnd;
    }

    public void setCurDateEnd(LocalDateTime curDateEnd) {
        this.curDateEnd = curDateEnd;
    }

    public List<ExchangeRate> getExchangeRateList() {
        return exchangeRateList;
    }

    public void setExchangeRateList(List<ExchangeRate> exchangeRateList) {
        this.exchangeRateList = exchangeRateList;
    }
}