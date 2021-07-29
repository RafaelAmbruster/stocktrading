package com.canada.edu.stocktrading.controller;

import com.canada.edu.stocktrading.config.WebSocketEventListener;
import com.canada.edu.stocktrading.dto.DailyBidAskDto;
import com.canada.edu.stocktrading.dto.DailyDto03MSummary;
import com.canada.edu.stocktrading.dto.OrderDto;
import com.canada.edu.stocktrading.dto.OrderFilledDto;
import com.canada.edu.stocktrading.model.Daily;
import com.canada.edu.stocktrading.model.Order;
import com.canada.edu.stocktrading.model.OrderStatus;
import com.canada.edu.stocktrading.repository.DailyRepository;
import com.canada.edu.stocktrading.repository.OrderRepository;
import com.canada.edu.stocktrading.service.DailyService;
import com.canada.edu.stocktrading.service.OrderService;
import com.canada.edu.stocktrading.service.utils.ConvertTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class OrderWSController {

    @Autowired
    WebSocketEventListener webSocketEventListener;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    DailyService dailyService;

    @Autowired
    OrderService orderService;


    public OrderWSController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    public void updateOrderStatus() {
        List<Order> orders = orderService.getAllOrdersByStatus(OrderStatus.WORKING);

        orders.forEach(o -> {
            List<OrderFilledDto>dto = orderService.getAllOrdersAfterFilled(o);
            if(dto != null){
                this.simpMessagingTemplate.convertAndSendToUser(o.getUser().getUserId(),"/queue/order",dto.toString());
            }
        });
     }
}