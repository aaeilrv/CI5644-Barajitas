package com.example.demo.controller.dto

import com.example.demo.model.*
import java.sql.Timestamp

data class UpdateExchangeOfferRequest (
        val id: Long,
        val bidder: Long?,
        val exchangeRequest: Long?,
        val offeredCard: Long?,
        val status: ExchangeOfferStatus,
        val createdAt: Timestamp?
)