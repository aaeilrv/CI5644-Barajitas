package com.example.demo.controller.dto

import java.sql.Timestamp

data class CreateExchangeRequestDTO (
        val userId: Long,
        val requestedCardId: Long,
        val requestStatus: String?,
        val createdAt: Timestamp?
)