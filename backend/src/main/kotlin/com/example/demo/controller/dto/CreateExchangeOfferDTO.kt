package com.example.demo.controller.dto

import java.sql.Timestamp

data class CreateExchangeOfferDTO (
        val bidderId: Long,
        val exchangeRequestId: Long,
        val offeredCardId: Long,
        val status: String?,
        val createdAt: Timestamp?
)