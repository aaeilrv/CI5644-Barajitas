package com.example.demo.controller.dto

import com.example.demo.model.*
import java.sql.Timestamp

data class CreateExchangeCounterofferDTO (
        val offeredCardId: Long,
        val exchangeOfferId: Long,
        val exchangeRequestId: Long?,
        val status: String?,
        val createdAt: Timestamp?
)