package com.example.demo.controller

import com.example.demo.controller.dto.*
import com.example.demo.model.ExchangeCounteroffer
import com.example.demo.model.ExchangeRequestStatus
import com.example.demo.service.ExchangeCounterofferService
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
@RequestMapping("v1/exchangeCounteroffer")
class ExchangeCounterofferController {

    @Autowired
    lateinit var exchangeCounterofferService: ExchangeCounterofferService

    @PostMapping
    fun createExchangeOffer(@RequestBody request: CreateExchangeCounterofferDTO): ResponseEntity<ExchangeCounterofferDTO> {
        return ResponseEntity.ok(ExchangeCounterofferDTO(exchangeCounterofferService.create(request)))
    }

    @PatchMapping
    fun updateExchangeCounteroffer(@RequestBody request: UpdateExchangeCounterofferRequest) : ResponseEntity<ExchangeCounterofferDTO> {
        return ResponseEntity.ok(ExchangeCounterofferDTO(exchangeCounterofferService.updateExchangeCounteroffer(request)))
    }

    @GetMapping
    fun getAllExchangeCounteroffers(pageable: Pageable): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getAll(pageable).map {ExchangeCounterofferDTO(it)}
    }

    @GetMapping("/{id}")
    fun getExchangeCounterofferById(@PathVariable id: Long): ResponseEntity<ExchangeCounterofferDTO> {
        val exchangeCounterOfferOpt = exchangeCounterofferService.getById(id)
        if (exchangeCounterOfferOpt.isPresent) {
            return ResponseEntity.ok(ExchangeCounterofferDTO(exchangeCounterOfferOpt.get()))
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange counteroffer with $id not found.")
        }
    }

    // Todos los ECO creados por un usuario
    @GetMapping("/creator/{id}")
    fun getExchangeCounterofferByCreator(@PathVariable id: Long): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByCreator(id)
    }

    // Todos los ECO recibidos por un usuario
    @GetMapping("/receiver/{id}")
    fun getExchangeCounterofferByReceiver(@PathVariable id: Long): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByReceiver(id)
    }

    // Todos los ECO de una barajita específica
    @GetMapping("/card/{id}")
    fun getExchangeCounterofferByCard(@PathVariable id: Long): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByCard(id)
    }

    // Todos los ECO que ha recibido un Exchange Offer
    @GetMapping("/EO/{id}")
    fun getExchangeCounterofferByEO(@PathVariable id: Long): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByEO(id)
    }

    // Todos los ECO de un Exchange Request
    @GetMapping("/ER/{id}")
    fun getExchangeCounterofferByER(@PathVariable id: Long): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByER(id)
    }

    // Todos los ECO creados en un rango de tiempo
    @GetMapping("/start/{start}/end/{end}")
    fun getExchangeCounterofferByDateRange(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date
    ): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByDateRange(start, end)
    }

    // Todos los ECO creados por un usuario en un rango de tiempo
    @GetMapping("/creator/{creatorId}/start/{start}/end/{end}")
    fun getExchangeCounterofferByCreatorAndDateRange(
            @PathVariable creatorId: Long,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date
    ): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByCreatorAndDateRange(creatorId, start, end)
    }

    // Todos los ECO recibidos por un usuario en un rango de tiempo
    @GetMapping("/receiver/{receiverId}/start/{start}/end/{end}")
    fun getExchangeCounterofferByReceiverAndDateRange(
            @PathVariable receiverId: Long,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date
    ): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByReceiverAndDateRange(receiverId, start, end)
    }

    // *- los que no funcionan -* //

    // Todos los ECO con un estatus
    @GetMapping("/status/{status}")
    fun getExchangeCounterofferByStatus(@PathVariable status: ExchangeRequestStatus): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByStatus(status)
    }

    // Todos los ECO con cierto estatus creados por un usuario
    @GetMapping("/creator/{creatorId}/status/{status}")
    fun getExchangeCounterofferByCreatorAndStatus(@PathVariable creatorId: Long, @PathVariable status: ExchangeRequestStatus): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByCreatorAndStatus(creatorId, status)
    }

    // Todos los ECO con cierto estatus creados por un usuario
    @GetMapping("/receiver/{receiverId}/status/{status}")
    fun getExchangeCounterofferByReceiverAndStatus(@PathVariable receiverId: Long, @PathVariable status: ExchangeRequestStatus): List<ExchangeCounterofferDTO> {
        return exchangeCounterofferService.getByReceiverAndStatus(receiverId, status)
    }
}