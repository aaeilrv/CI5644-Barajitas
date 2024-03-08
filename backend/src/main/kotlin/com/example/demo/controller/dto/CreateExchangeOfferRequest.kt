package com.example.demo.controller.dto

import com.example.demo.model.*
import java.sql.Timestamp

data class CreateExchangeOfferRequest (
        val id: Long,
        val bidder: User,
        val exchangeRequest: ExchangeRequest,
        val offeredCard: Card,
        val status: ExchangeOfferStatus,
        val createdAt: Timestamp
)