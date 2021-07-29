package com.canada.edu.stocktrading.service.impl;

import com.canada.edu.stocktrading.dto.DailyBidAskDto;
import com.canada.edu.stocktrading.model.Daily;
import com.canada.edu.stocktrading.repository.DailyRepository;
import com.canada.edu.stocktrading.service.DailyService;
import com.canada.edu.stocktrading.dto.DailyDetailsDto;
import com.canada.edu.stocktrading.dto.DailyDto03MSummary;
import com.canada.edu.stocktrading.service.SymbolService;
import com.canada.edu.stocktrading.service.utils.ConvertTimeUtils;
import com.canada.edu.stocktrading.service.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DailyServiceImpl implements DailyService {
    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private SymbolService symbolService;

    public List<DailyDetailsDto> findAllDailiesBySymbolIds(List<Integer> symbolIds){
        // convert current time to match with the dailies in database 14/7
        Timestamp ts = ConvertTimeUtils.convertCurrentTimeTo14July();

        List<Daily>dailies = dailyRepository.findDailiesBySymbolIds(ts, symbolIds);

        List<DailyDetailsDto>dailyDtos = new ArrayList<>();

        // convert to DailyDto
        dailies.stream().forEach(daily->{
            DailyDetailsDto dailyDto = new DailyDetailsDto(daily);
            dailyDtos.add(dailyDto);
        });
        return dailyDtos;
    }

    public DailyDto03MSummary findDailyBySymbolId(Integer symbolId){
        Timestamp ts = ConvertTimeUtils.convertCurrentTimeTo14July();
        // returns 1 result => get the first result
        List<Daily> dailies= dailyRepository.findDailiesBySymbolIds(ts, new ArrayList<Integer>(List.of(symbolId)));
        if(dailies.size()>0){
            return new DailyDto03MSummary(dailies.get(0));
        }
        return null;
    }

    public DailyBidAskDto getDailyBidAskBySymbolId(Integer symbolId){
        Timestamp ts = ConvertTimeUtils.convertCurrentTimeTo14July();

        Daily daily = dailyRepository.findCurrentDailyBySymbolId(ts, symbolId);

        DailyBidAskDto dto = MapperUtils.mapperObject(daily, DailyBidAskDto.class);

        dto.setSpread();

        return dto;
    }

    public BigDecimal getMatchedPriceInNext15sBySymbolId(Integer symbolId) {
        if(!symbolService.isSymbolValid(symbolId)){
            throw new IllegalArgumentException("Unable to find symbol with id " + symbolId);
        }
        Timestamp ts = ConvertTimeUtils.convertCurrentTimeTo14July();

        return dailyRepository.findMatchedPriceNext15sBySymbolId(
                ts.toLocalDateTime().getHour()
                ,ts.toLocalDateTime().getMinute()
                ,ts.toLocalDateTime().getSecond() + 15
                ,symbolId);
    }
}
