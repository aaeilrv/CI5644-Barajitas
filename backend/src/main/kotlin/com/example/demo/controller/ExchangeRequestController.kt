package com.example.demo.controller

import com.example.demo.controller.dto.*
import com.example.demo.model.ExchangeRequest
import com.example.demo.model.ExchangeRequestStatus
import com.example.demo.service.ExchangeRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*


@CrossOrigin(value = ["http://localhost:3000", "http://207.246.119.200:3000"])
@RestController
@RequestMapping("v1/exchangeRequest")
class ExchangeRequestController {

    @Autowired
    lateinit var exchangeRequestService: ExchangeRequestService

    @PostMapping
    fun createExchangeRequest(@RequestBody request: CreateExchangeRequestDTO): ResponseEntity<ExchangeRequestDTO> {
        return ResponseEntity.ok(ExchangeRequestDTO(exchangeRequestService.create(request)))
        //return exchangeRequestService.create((request))
    }

    @PatchMapping
    fun updateExchangeRequest(@RequestBody request: UpdateExchangeRequestRequest) : ResponseEntity<ExchangeRequestDTO> {
        return ResponseEntity.ok(ExchangeRequestDTO(exchangeRequestService.updateExchangeRequest(request)))
    }

    @GetMapping
    fun getAllExchangeRequests(pageable: Pageable): List<ExchangeRequestDTO> {
        return exchangeRequestService.getAll(pageable).map {exchangeRequest -> ExchangeRequestDTO(exchangeRequest)}
    }

    @GetMapping("/{id}")
    fun getExchangeRequestById(@PathVariable id: Long): ResponseEntity<ExchangeRequestDTO> {
        val exchangeRequestOpt = exchangeRequestService.getById(id)
        if (exchangeRequestOpt.isPresent) {
            return ResponseEntity.ok(ExchangeRequestDTO(exchangeRequestOpt.get()))
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange request with $id not found.")
        }
    }

    // Todos los Exchange Requests creados por un usuario
    @GetMapping("/user/{id}")
    fun getExchangeRequestByUserId(@PathVariable id: Long): List<ExchangeRequestDTO> {
        return exchangeRequestService.getByUserid(id)
    }

    // Todos los ER de una barajita en particular
    @GetMapping("/card/{id}")
    fun getExchangeRequestByCardId(@PathVariable id: Long): List<ExchangeRequestDTO> {
        return exchangeRequestService.getByCardId(id)
    }

    // Todos los ER de un usuario y de una barajita de ese usuario
    @GetMapping("/user/{userId}/card/{cardId}")
    fun getExchangeRequestByUserIdAndCardId(@PathVariable userId: Long, @PathVariable cardId: Long): List<ExchangeRequestDTO> {
        return exchangeRequestService.getByUserIdAndCardId(cardId, userId)
    }

    // Todos los ER por rango de fechas
    @GetMapping("/start/{start}/end/{end}")
    fun getExchangeRequestByDateRange(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date):
            List<ExchangeRequestDTO> {
        return exchangeRequestService.getByDateRange(start, end)
    }

    // Todos los ER de un usuario por rango de fechas
    @GetMapping("/user/{id}/start/{start}/end/{end}")
    fun getExchangeRequestByUserWithinDateRange(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date,
            @PathVariable id: Long): List<ExchangeRequestDTO> {
        return exchangeRequestService.getByUserIdWithinDateRange(id, start, end)
    }

    // Todos los ER que piden barajitas que un usuario tiene
    @GetMapping("/hasCards/{id}")
    fun getExchangeRequestByOwnership(@PathVariable id: Long): List<ExchangeRequestDTO> {
        return exchangeRequestService.getAllPossibleERbyUser(id)
    }

    // Todos los ER creados por user que piden barajitas que owner tiene
    @GetMapping("hasCards/{ownerId}/user/{creatorId}")
    fun getExchangeRequestByOwnerAndUser(@PathVariable ownerId: Long, @PathVariable creatorId: Long): List<ExchangeRequestDTO> {
        return exchangeRequestService.getAllPossibleERbyCardsOwnerAndRequester(ownerId, creatorId)
    }

    // *- los que no funcionan -* //

    // Todos los Exchange Requests con un estatus particular
    @GetMapping("/status/{status}")
    fun getExchangeRequestByStatus(@PathVariable status: ExchangeRequestStatus): List<ExchangeRequestDTO> {
        return exchangeRequestService.getByStatus(status)
    }

    // Todos los ER de un estatus creados por un usuario
    @GetMapping("/user/{id}/{status}")
    fun getExchangeRequestByUserIdAndStatus(@PathVariable id: Long, @PathVariable status: ExchangeRequestStatus): List<ExchangeRequestDTO> {
        return exchangeRequestService.getByUseridByStatus(id, status)
    }
}