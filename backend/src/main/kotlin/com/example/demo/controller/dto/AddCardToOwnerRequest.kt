package com.example.demo.controller.dto

data class AddCardToOwnerRequest (
    val ownerSub: String,
    val cardId:Long
)