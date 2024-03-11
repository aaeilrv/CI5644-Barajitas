package com.example.demo.model

import com.example.demo.controller.dto.CreateExchangeCounterofferRequest
import com.example.demo.controller.dto.CreateExchangeOfferRequest
import com.example.demo.controller.dto.CreateExchangeRequestRequest
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.awt.print.Pageable
import java.sql.Time
import java.sql.Timestamp

public enum class ExchangeRequestStatus {
    PENDING, ACCEPTED, REJECTED, CANCELLED
}

public enum class ExchangeOfferStatus {
    PENDING, ACCEPTED, REJECTED, CANCELLED, COUNTEROFFER
}

@Entity
@Table(name = "exchange_request")
class ExchangeRequest(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val id: Long?,

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private var requester: User,

        @ManyToOne
        @JoinColumn(name = "requested_card_id", nullable = false)
        private var requestedCard: Card,

        @Column(name = "status", nullable = false, columnDefinition = "varchar")
        @Enumerated(EnumType.STRING)
        var status: ExchangeRequestStatus,

        @Column(name = "created_at", nullable = false)
        private val createdAt: java.sql.Timestamp,
        ) {
    constructor() : this(-1, User(), Card(), ExchangeRequestStatus.PENDING, java.sql.Timestamp(0))
    constructor(request: CreateExchangeRequestRequest) : this(
            null,
            request.user,
            request.requestedCard,
            ExchangeRequestStatus.valueOf(request.requestStatus.uppercase()),
            request.createdAt
    )

    fun getId(): Long = this.id!!
    fun getUser(): User = this.requester
    fun getRequestedCard(): Card = this.requestedCard
    fun getRequestStatus(): ExchangeRequestStatus = this.status
    fun getCreatedAt(): Timestamp = this.createdAt
}

@Entity
@Table(name = "exchange_offer")
class ExchangeOffer(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val id: Long?,

        @ManyToOne
        @JoinColumn(name = "bidder_id", nullable = false)
        private val bidder: User,

        @ManyToOne
        @JoinColumn(name = "exchange_request_id", nullable = false)
        private val exchangeRequest: ExchangeRequest,

        @ManyToOne
        @JoinColumn(name = "offered_card_id", nullable = false)
        private val offeredCard: Card,

        @Column(name = "status", nullable = false, columnDefinition = "varchar")
        @Enumerated(EnumType.STRING)
        var status: ExchangeOfferStatus,

        @Column(name = "created_at", nullable = false)
        private val createdAt: java.sql.Timestamp,

        ) {
    constructor() : this(-1, User(), ExchangeRequest(), Card(), ExchangeOfferStatus.PENDING, java.sql.Timestamp(0))

    constructor(request: CreateExchangeOfferRequest) : this(
            null,
            request.bidder,
            request.exchangeRequest,
            request.offeredCard,
            ExchangeOfferStatus.valueOf(request.status.uppercase()),
            request.createdAt
    )

    fun getId(): Long = this.id!!
    fun getBidder(): User = this.bidder
    fun getExchangeRequest(): ExchangeRequest = this.exchangeRequest
    fun getOfferedCard(): Card = this.offeredCard
    fun getRequestStatus(): ExchangeOfferStatus = this.status
    fun getCreatedAt(): Timestamp = this.createdAt
}

@Entity
@Table(name = "exchange_counteroffer")
class ExchangeCounteroffer(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val id: Long?,

        @ManyToOne
        @JoinColumn(name = "offered_card_id", nullable = false)
        private val card: Card,

        @Column(name = "status", nullable = false, columnDefinition = "varchar")
        @Enumerated(EnumType.STRING)
        var status: ExchangeRequestStatus,

        @ManyToOne
        @JoinColumn(name = "exchange_request_id", nullable = false)
        private val exchangeRequest: ExchangeRequest,

        @ManyToOne
        @JoinColumn(name = "exchange_offer_id", nullable = false)
        private val exchangeOffer: ExchangeOffer,

        @Column(name = "created_at", nullable = false)
        private val createdAt: java.sql.Timestamp,

        ) {
    constructor() : this(-1, Card(), ExchangeRequestStatus.PENDING, ExchangeRequest(), ExchangeOffer(), java.sql.Timestamp(0))

    constructor(request: CreateExchangeCounterofferRequest) : this(
            null,
            request.offeredCard,
            ExchangeRequestStatus.valueOf(request.status.uppercase()),
            request.exchangeRequest,
            request.exchangeOffer,
            request.createdAt
    )

    fun getId(): Long = this.id!!
    fun getOfferedCard(): Card = this.card
    fun getRequestStatus(): ExchangeRequestStatus = this.status
    fun getExchangeRequest(): ExchangeRequest = this.exchangeRequest
    fun getExchangeOffer(): ExchangeOffer = this.exchangeOffer
    fun getCreatedAt(): Timestamp = this.createdAt
}