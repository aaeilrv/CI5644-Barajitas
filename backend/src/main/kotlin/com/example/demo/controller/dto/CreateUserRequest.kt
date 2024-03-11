package com.example.demo.controller.dto

data class CreateUserRequest (
    val firstName: String,
    val lastName: String,
    val birthDay: String,
    val username: String,
    val emailAddress: String
)