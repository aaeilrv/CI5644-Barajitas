package com.example.demo.controller

import com.example.demo.controller.dto.CreateCardRequest
import com.example.demo.controller.dto.CardDTO
import com.example.demo.controller.dto.UserDTO
import com.example.demo.controller.dto.UserWhoOwnsCardDTO
import com.example.demo.model.Card
import com.example.demo.model.User
import com.example.demo.repo.UserRepository
import com.example.demo.service.CardService
import jakarta.persistence.Entity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(value = ["http://localhost:3000", "http://207.246.119.200:3000"])
@RestController
@RequestMapping("v1/card")
class CardController {

    @Autowired
    lateinit var cardService: CardService

    @PostMapping
    fun createCard(@RequestBody request: CreateCardRequest): ResponseEntity<CardDTO> {
        return ResponseEntity.ok(CardDTO(cardService.create(Card(request))))
    }

    @PostMapping("createMultipleCards")
    fun createCards(@RequestBody request: List<CreateCardRequest>): String {
        for(r:CreateCardRequest in request) {
            cardService.create(Card(r))
        }
        return "done"+request.size
    }
    @GetMapping()
    fun getAllCard(pageable: Pageable): List<CardDTO> {
        return cardService.getAll(pageable).map {CardDTO(it)}
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<CardDTO> {
        val cardOpt = cardService.getById(id)
        if (cardOpt.isPresent) {
            return ResponseEntity.ok(CardDTO(cardOpt.get()))
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with $id not found")
        }
    }

    @GetMapping("ownersOfCard/{cardId}")
    fun getCardOwners(@PathVariable cardId: Long): List<UserWhoOwnsCardDTO>? {
        return cardService.getOwnersById(cardId)?.map {UserWhoOwnsCardDTO(it)}
    }

}