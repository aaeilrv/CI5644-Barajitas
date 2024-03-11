package com.example.demo.service

import com.example.demo.controller.dto.*
import com.example.demo.model.*
import com.example.demo.repo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class ExchangeOfferService(@Autowired private val exchangeOfferRepository: ExchangeOfferRepository,
                           private val userService: UserService,
                           private val cardService: CardService,
                           private val exchangeRequestService: ExchangeRequestService) {

    public fun create(exchangeOffer: CreateExchangeOfferDTO): ExchangeOffer {
        val foundBidder = userService.getById(exchangeOffer.bidderId).orElseThrow{
            NoSuchElementException("User not found.")
        }
        val foundCard = cardService.getById(exchangeOffer.offeredCardId).orElseThrow{
            NoSuchElementException("Card not found.")
        }
        val foundExchangeRequest = exchangeRequestService.getById(exchangeOffer.exchangeRequestId).orElseThrow{
            NoSuchElementException("Exchange request not found.")
        }

        if (exchangeOffer.offeredCardId == foundExchangeRequest.getRequestedCard().getId()) {
            throw IllegalArgumentException("Can't bid the same card of the request.")
        }

        val newEO: CreateExchangeOfferRequest
        try {
            newEO = CreateExchangeOfferRequest(
                    bidder = foundBidder,
                    offeredCard = foundCard,
                    exchangeRequest = foundExchangeRequest,
                    status = "PENDING",
                    createdAt = Timestamp.from(Instant.now())
            )

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Error creating the exchange offer")
        }
        return exchangeOfferRepository.save(ExchangeOffer(newEO))
    }

    public fun updateExchangeOffer(request: UpdateExchangeOfferRequest): ExchangeOffer {
        val exchangeOfferToUpdate = exchangeOfferRepository.findById(request.id)

        if (exchangeOfferToUpdate.isPresent) {
            val currentEO = exchangeOfferToUpdate.get()
            currentEO.status = request.status
            return exchangeOfferRepository.save(currentEO)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange request not found.")
        }
    }

    public fun getById(id: Long): Optional<ExchangeOffer> {
        return exchangeOfferRepository.findById(id)
    }

    public fun getAll(pageable: Pageable): List<ExchangeOffer> {
        val exchangeOfferEntities = exchangeOfferRepository.findAll(pageable)
        val exchangeOffer = exchangeOfferEntities.map { it }
        return exchangeOffer.content
    }

    // Todos los EO creados por un usuario
    public fun getByBidderId(id: Long): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByCreatedUserId(id)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO recibidos por un usuario
    public fun getByEOReceiver(id: Long): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByReceiverUserid(id)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO recibidos por un exchange request
    public fun getByExchangeRequest(id: Long): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByExchangeRequest(id)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO ofreciendo una barajita
    public fun getByCardId(cardId: Long): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByCard(cardId)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO con un status
    public fun getByStatus(status: ExchangeOfferStatus): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByStatus(status)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO en cierto rango de tiempo
    public fun getByDateRange(start: Date, end: Date): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByDateRange(start, end)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO creados por un usuario ofreciendo una barajita
    public fun getByBidderIdAndCardId(userId: Long, cardId: Long): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByUserAndCardId(userId, cardId)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO creados por un usuario y con un status
    public fun getByBidderAndStatus(userId: Long, status: ExchangeOfferStatus): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByCreatedUserIdAndStatus(userId, status)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO creados por un usuario en cierto rango de tiempo
    public fun getByBidderAndDateRange(userId: Long, start: Date, end: Date): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByDateRangeAndCreatorId(userId, start, end)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }

    // Todos los EO recibidos por un usuario en cierto rango de tiempo
    public fun getByReceiverAndDateRange(userId: Long, start: Date, end: Date): List<ExchangeOfferDTO> {
        val exchangeOffers = exchangeOfferRepository.findByDateAndReceiverid(userId, start, end)
        return exchangeOffers.map { offers -> ExchangeOfferDTO(offers) }
    }
}