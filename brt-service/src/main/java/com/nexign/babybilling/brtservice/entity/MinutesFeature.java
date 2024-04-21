package com.nexign.babybilling.brtservice.entity;

import jakarta.persistence.Column;

import java.math.BigDecimal;

public class MinutesFeature {

    @Column(nullable = false)
    private BigDecimal incomingCallCost;

    @Column(nullable = false)
    private BigDecimal incomingCallCostOther;

    @Column(nullable = false)
    private BigDecimal outGoingCallCost;

    @Column(nullable = false)
    private BigDecimal outGoingCallCostOther;

    @Column(nullable = false)
    private BigDecimal monthlyCost;

    @Column(nullable = false)
    private Integer minutes;
}
