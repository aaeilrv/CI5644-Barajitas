package com.example.demo.controller.dto

import com.example.demo.model.Ownership

data class CardOwnedByUserDTO(
    private val cardId: Long,
    private val playerName: String,
    private val playerPosition: String,
    private val playerNumber: Short,
    private val numberOwned: Int,
    private val country: String,
    private val photoUrl: String
) {
    fun getCardId(): Long = this.cardId
    fun getPlayerName(): String = this.playerName
    fun getPlayerPosition(): String = this.playerPosition
    fun getPlayerNumber(): Short = this.playerNumber
    fun getNumberOwned(): Int = this.numberOwned
    fun getCountry(): String = this.country
    fun getPhotoURL(): String = this.photoUrl

    constructor(ownershipObject: Ownership): this(
        ownershipObject.getCard().getId(),
        ownershipObject.getCard().getName(),
        ownershipObject.getCard().getPlayerPosition().toString(),
        ownershipObject.getCard().getPlayerNumber(),
        ownershipObject.getNumberOwned(),
        ownershipObject.getCard().getCountry(),
        ownershipObject.getCard().getPhotoURL()!!
    )


}

data class UserWhoOwnsCardDTO(
    private val userId: Long,
    private val firstName: String,
    private val lastName: String,
    private val birthday: String,
    private val username: String,
    private val email: String,
    private val numberOwned: Int
) {
    fun getUserId(): Long = this.userId
    fun getFirstName(): String = this.firstName
    fun getLastName(): String = this.lastName
    fun getBirthday(): String = this.birthday
    fun getUserName(): String = this.username
    fun getEmail(): String = this.email
    fun getNumberOwned(): Int = this.numberOwned

    constructor(ownershipObject: Ownership): this(
        ownershipObject.getUser().getId(),
        ownershipObject.getUser().getFirstName(),
        ownershipObject.getUser().getLastName(),
        ownershipObject.getUser().getBirthDay().toString(),
        ownershipObject.getUser().getUsername(),
        ownershipObject.getUser().getEmailAddress(),
        ownershipObject.getNumberOwned()
    )
}
