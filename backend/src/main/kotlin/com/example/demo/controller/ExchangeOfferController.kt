package com.example.demo.controller

import com.example.demo.controller.dto.*
import com.example.demo.model.ExchangeOffer
import com.example.demo.model.ExchangeOfferStatus
import com.example.demo.service.ExchangeOfferService
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
@RequestMapping("v1/exchangeOffer")
class ExchangeOfferController {

    @Autowired
    lateinit var exchangeOfferService: ExchangeOfferService

    @PostMapping
    fun createExchangeOffer(@RequestBody request: CreateExchangeOfferDTO): ResponseEntity<ExchangeOfferDTO> {
        return ResponseEntity.ok(ExchangeOfferDTO(exchangeOfferService.create(request)))
    }

    @PatchMapping
    fun updateExchangeOffer(@RequestBody request: UpdateExchangeOfferRequest) : ResponseEntity<ExchangeOfferDTO> {
        return ResponseEntity.ok(ExchangeOfferDTO(exchangeOfferService.updateExchangeOffer(request)))
    }

    @GetMapping
    fun getAllExchangeOffers(pageable: Pageable): List<ExchangeOfferDTO> {
        return exchangeOfferService.getAll(pageable).map { exchangeOffer -> ExchangeOfferDTO(exchangeOffer) }
    }

    @GetMapping("/{id}")
    fun getExchangeOfferById(@PathVariable id: Long): ResponseEntity<ExchangeOfferDTO> {
        val exchangeOfferOpt = exchangeOfferService.getById(id)
        if (exchangeOfferOpt.isPresent) {
            return ResponseEntity.ok(ExchangeOfferDTO(exchangeOfferOpt.get()))
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange offer with $id not found.")
        }
    }

    // Todos los EO creados por un usuario
    @GetMapping("/bidder/{id}")
    fun getExchangeOfferByBidder(@PathVariable id: Long): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByBidderId(id)
    }

    // Todos los EO recibidos por un usuario
    @GetMapping("/receiver/{id}")
    fun getExchangeOfferByReceiver(@PathVariable id: Long): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByEOReceiver(id)
    }

    // Todos los EO ofreciendo una barajita
    @GetMapping("/offered/{id}")
    fun getExchangeOfferByCard(@PathVariable id: Long): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByCardId(id)
    }

    // Todos los EO que ha recibido un Exchange Request
    @GetMapping("/ER/{id}")
    fun getExchangeOfferByER(@PathVariable id: Long): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByExchangeRequest(id)
    }

    // Todos los EO de un usuario ofreciendo una barajita particular
    @GetMapping("/bidder/{bidderId}/card/{cardId}")
    fun getExchangeOfferByBidderAndCard(@PathVariable bidderId: Long, @PathVariable cardId: Long): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByBidderIdAndCardId(bidderId, cardId)
    }

    // Todos los EO creados en cierto rango de tiempo
    @GetMapping("/start/{start}/end/{end}")
    fun getExchangeOfferByDateRange(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date
    ): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByDateRange(start, end)
    }

    // Todos los EO creados en cierto rango de tiempo por un usuario
    @GetMapping("/bidder/{bidderId}/start/{start}/end/{end}")
    fun getExchangeOfferByBidderAndDateRange(
            @PathVariable bidderId: Long,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date
    ): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByBidderAndDateRange(bidderId, start, end)
    }

    @GetMapping("/user/{receiverId}/start/{start}/end/{end}")
    fun getExchangeOfferByReceiverAndDateRange(
            @PathVariable receiverId: Long,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date
    ): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByReceiverAndDateRange(receiverId, start, end)
    }

    // *- los que no funcionan -* //

    // Todos los EO con un status particular
    @GetMapping("/status/{status}")
    fun getExchangeOfferByStatus(@PathVariable status: ExchangeOfferStatus): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByStatus(status)
    }

    // Todos los EO creados por un usuario y con un estatus
    @GetMapping("/bidder/{bidderId}/status/{status}")
    fun getExchangeOfferByBidderAndStatus(@PathVariable bidderId: Long, @PathVariable status: ExchangeOfferStatus): List<ExchangeOfferDTO> {
        return exchangeOfferService.getByBidderAndStatus(bidderId, status)
    }
}