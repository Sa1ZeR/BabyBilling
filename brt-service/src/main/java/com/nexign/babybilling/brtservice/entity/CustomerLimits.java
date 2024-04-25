package com.nexign.babybilling.brtservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_minutes")
public class CustomerLimits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    private Customer customer;

    @Column(nullable = false)
    private Integer minutes;

    @Column(nullable = false)
    private Integer minutesOther;

    @Column(nullable = false)
    private boolean commonMinutes; //флаг, который говорит, что минуты могут быть общими
    // (То есть если тариф включает в себя просто 300 минут, то нам неважно с кем он разговаривает,
    // с текущими оператором или с другим)
}
