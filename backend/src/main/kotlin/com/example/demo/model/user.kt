package com.example.demo.model

import com.example.demo.controller.dto.CreateUserRequest
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*


@Entity
@Table(name = "users")
open class User (
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long?,

    @Column(name = "auth0_sub")
    private val auth0Sub: String,

    @Column(nullable = false, length = 100)
    private var firstName: String,

    @Column(nullable = false, length = 100)
    private var lastName: String,

    @Column(nullable = false, length = 100)
    private var birthday: java.sql.Date,

    @Column(nullable = false, length = 32)
    private var username: String,

    @Column(nullable = false, length = 100)
    private var email: String,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)])
    private var cards: Set<Ownership> = mutableSetOf(), // El conjunto de cartas que tiene el usuario

    @OneToMany(mappedBy = "cardHolder", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)])
    private var userCreditCards: List<CreditCard> = mutableListOf(),

    @OneToMany(mappedBy = "purchasingUser", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)])
    private var userPurchases: List<Purchase> = mutableListOf()
) {
    open fun getId(): Long {
        return this.id!!
    }

    open fun getFirstName(): String {
        return this.firstName
    }

    open fun getLastName(): String {
        return this.lastName
    }

    open fun getFullName(): String {
        return ("${this.firstName} ${this.lastName}")
    }

    open fun getBirthDay(): java.sql.Date {
        return this.birthday
    }

    open fun getUsername(): String {
        return this.username
    }

    open fun getEmailAddress(): String {
        return this.email
    }

    open fun setFirstName(firstName : String) {
        this.firstName = firstName
    }

    open fun setLastName(lastName : String) {
        this.lastName = lastName
    }
    
    open fun setUsername(username : String) {
        this.username = username
    }

    open fun setEmail(email : String) {
        this.email = email
    }

    open fun getAuth0Sub(): String = this.auth0Sub


    open fun getCardsOwned(): MutableList<Ownership> = this.cards.toMutableList()

    constructor() : this(null, "", "", "", java.sql.Date(-1), "", "")
    constructor(request: CreateUserRequest, auth0Sub: String) : this(
        null,
        auth0Sub,
        request.firstName,
        request.lastName,
        java.sql.Date.valueOf(request.birthDay),
        request.username,
        request.emailAddress
    )
}