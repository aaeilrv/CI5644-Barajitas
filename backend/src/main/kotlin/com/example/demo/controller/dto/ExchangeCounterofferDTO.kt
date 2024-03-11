package com.example.demo.controller.dto

import com.example.demo.model.*
import java.io.Serializable
import java.sql.Timestamp
import java.util.Date

data class ExchangeCounterofferDTO(
        private val id: Long,
        private val offeredCardId: Long,
        private val status: ExchangeRequestStatus,
        private val exchangeRequestId: Long,
        private val exchangeOfferId: Long,
        private val createdAt: Timestamp
): Serializable {
    fun getId(): Long {
        return this.id
    }

    fun getOfferedCardId(): Long {
        return this.offeredCardId
    }

    fun getStatus(): ExchangeRequestStatus {
        return this.status
    }

    fun getExchangeRequestId(): Long {
        return this.exchangeRequestId
    }

    fun getExchangeOfferId(): Long {
        return this.exchangeOfferId
    }

    fun getCreatedAt(): Timestamp {
        return this.createdAt
    }

    constructor(exchangeCounterOffer: ExchangeCounteroffer): this(
            exchangeCounterOffer.getId(),
            exchangeCounterOffer.getOfferedCard().getId(),
            exchangeCounterOffer.getRequestStatus(),
            exchangeCounterOffer.getExchangeRequest().getId(),
            exchangeCounterOffer.getExchangeOffer().getId(),
            exchangeCounterOffer.getCreatedAt()
    )
}