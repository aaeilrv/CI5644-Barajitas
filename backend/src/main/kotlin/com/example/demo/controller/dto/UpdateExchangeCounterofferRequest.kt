package com.example.demo.controller.dto

import com.example.demo.model.*
import java.sql.Timestamp
import java.util.Date

data class UpdateExchangeCounterofferRequest(
        val id: Long,
        val offeredCardId: Long?,
        val status: ExchangeRequestStatus,
        val exchangeRequestId: Long?,
        val exchangeOfferId: Long?,
        val createdAt: Timestamp?
)