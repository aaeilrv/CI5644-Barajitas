package com.example.demo.controller.dto

import com.example.demo.controller.CardController
import com.example.demo.controller.UserController
import com.example.demo.model.Card
import com.example.demo.model.ExchangeRequest
import java.io.Serializable
import com.example.demo.model.ExchangeRequestStatus
import com.example.demo.model.User
import java.sql.Timestamp

data class ExchangeRequestDTO (
        private val id: Long,
        private val userId: Long,
        private val requestedCardId: Long,
        private val status: String,
        private val createdAt: Timestamp
): Serializable {

    fun getId(): Long {
        return this.id
    }

    fun getUserId(): Long {
        return this.userId
    }

    fun getRequestedCardId(): Long {
        return this.requestedCardId
    }
    fun getStatus(): String {
        return this.status
    }

    fun getCreatedAt(): Timestamp {
        return this.createdAt
    }

    constructor(exchangeRequestObject: ExchangeRequest): this(
            exchangeRequestObject.getId(),
            exchangeRequestObject.getUser().getId(),
            exchangeRequestObject.getRequestedCard().getId(),
            exchangeRequestObject.getRequestStatus().toString(),
            exchangeRequestObject.getCreatedAt()
    )
}