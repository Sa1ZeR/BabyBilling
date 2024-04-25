package com.nexign.babybilling.brtservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tariff_call_limits")
public class TariffCalls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal incomingCallCost;

    @Column(nullable = false)
    private BigDecimal incomingCallCostOther;

    @Column(nullable = false)
    private BigDecimal outgoingCallCost;

    @Column(nullable = false)
    private BigDecimal outgoingCallCostOther;

    @Column(nullable = false)
    private BigDecimal incomingCallCostWithoutPacket;

    @Column(nullable = false)
    private BigDecimal incomingCallCostOtherWithoutPacket;

    @Column(nullable = false)
    private BigDecimal outgoingCallCostWithoutPacket;

    @Column(nullable = false)
    private BigDecimal outgoingCallCostOtherWithoutPacket;
}
