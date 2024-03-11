package com.example.demo.controller.dto

import com.example.demo.model.FieldPosition

data class CreateCardRequest (
    val name: String,
    val playerNumber: Short,
    val playerPosition: String,
    val photoURL: String?,
    val country: String
)