package com.nexign.babybilling.cdrservice.domain.entity;

import com.nexign.babybilling.payload.dto.CallType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "customer"})
@Table(name = "call_transaction")
public class CallTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    @Column(nullable = false)
    private CallType callType;

    @Column(nullable = false)
    private LocalDateTime dateStart;

    @Column(nullable = false)
    private LocalDateTime dateEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer firstCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer secondCustomer;
}
