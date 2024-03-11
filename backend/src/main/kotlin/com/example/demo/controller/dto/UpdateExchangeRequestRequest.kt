package com.example.demo.controller.dto

import com.example.demo.model.*
import java.sql.Timestamp
import java.util.Date

data class UpdateExchangeRequestRequest (
        val id: Long,
        val userId: Long?,
        val requestedCardId: Long?,
        val status: ExchangeRequestStatus,
        val createdAt: Timestamp?
)