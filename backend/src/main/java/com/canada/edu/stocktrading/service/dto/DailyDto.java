package com.canada.edu.stocktrading.service.dto;

import com.canada.edu.stocktrading.model.Daily;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public abstract class DLDto {
    protected Integer dailyId;

    protected Date timestamp;

    protected BigDecimal price;

    protected BigDecimal open;

    protected BigDecimal high;

    protected BigDecimal low;

    protected BigDecimal close;

    protected BigDecimal volume;

    protected BigDecimal prevClose;

    protected BigDecimal change;

    protected BigDecimal changeInPercent;


    public DLDto(Daily daily){
        this.dailyId = daily.getDailyId();

        this.timestamp = daily.getTimestamp();

        this.price = daily.getPrice();

        this.open = daily.getOpen();

        this.high = daily.getHigh();

        this.low = daily.getLow();

        this.close = daily.getClose();

        this.volume = daily.getVolume();

        this.prevClose = daily.getPrevClose();

    }

    public void setChange() {
        BigDecimal regularPrice = this.price;
        this.change = regularPrice.subtract(this.prevClose);
    };

    public void setChangeInPercent() {
        this.changeInPercent = change.multiply(new BigDecimal(100)).divide(this.price, RoundingMode.HALF_UP);
    };
}
