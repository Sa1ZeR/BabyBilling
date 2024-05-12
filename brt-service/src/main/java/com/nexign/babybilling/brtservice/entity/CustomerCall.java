package com.nexign.babybilling.brtservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_call_data")
public class CustomerCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Customer customer;

    @Column(nullable = false)
    private Integer minutes;

    @Column(nullable = false)
    private Integer minutesOther;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;
}
