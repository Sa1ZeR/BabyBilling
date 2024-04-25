package com.nexign.babybilling.brtservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tariffs_minutes")
public class TariffMinutes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer minutes;

    @Column(nullable = false)
    private Integer minutesOther;

    @Column(nullable = false)
    private boolean commonMinutes; //флаг, который говорит, что минуты могут быть общими
    // (То есть если тариф включает в себя просто 300 минут, то нам неважно с кем он разговаривает,
    // с текущими оператором или с другим)
}
