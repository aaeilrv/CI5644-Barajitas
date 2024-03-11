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
import kotlin.NoSuchElementException

@Service
class ExchangeCounterofferService(@Autowired private val exchangeCounterofferRepository: ExchangeCounterofferRepository,
                                  private val exchangeOfferService: ExchangeOfferService,
                                  private val exchangeRequestService: ExchangeRequestService,
                                  private val cardService: CardService) {

    public fun create(exchangeCounteroffer: CreateExchangeCounterofferDTO): ExchangeCounteroffer {
        val foundCard = cardService.getById(exchangeCounteroffer.offeredCardId).orElseThrow {
            NoSuchElementException("Card not found.")
        }

        val foundExchangeOffer = exchangeOfferService.getById(exchangeCounteroffer.exchangeOfferId).orElseThrow{
            NoSuchElementException("Exchange offer not found.")
        }

        val foundExchangeRequest = exchangeRequestService.getById(foundExchangeOffer.getExchangeRequest().getId()).orElseThrow {
            NoSuchElementException("Exchange request not found.")
        }

        if (exchangeCounteroffer.exchangeRequestId != null &&
                foundExchangeRequest.getId() != exchangeCounteroffer.exchangeRequestId) {
            throw IllegalArgumentException("Wrong exchange request ID. Either use the right one or don't add it.")
        }

        if (exchangeCounteroffer.offeredCardId == foundExchangeOffer.getOfferedCard().getId()) {
            throw IllegalArgumentException("Can't counteroffer the same card you're receiving.")
        }

        if (exchangeCounteroffer.offeredCardId == foundExchangeOffer.getExchangeRequest().getRequestedCard().getId()) {
            throw IllegalArgumentException("Can't counteroffer the same card initially requested.")
        }

        val newECO: CreateExchangeCounterofferRequest
        try {
            newECO = CreateExchangeCounterofferRequest(
                    offeredCard = foundCard,
                    exchangeRequest = foundExchangeOffer.getExchangeRequest(),
                    exchangeOffer = foundExchangeOffer,
                    status = "PENDING",
                    createdAt = Timestamp.from(Instant.now())
            )

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Error creating the exchange offer")
        }
        return exchangeCounterofferRepository.save(ExchangeCounteroffer(newECO))
    }

    public fun updateExchangeCounteroffer(request: UpdateExchangeCounterofferRequest): ExchangeCounteroffer {
        val exchangeCounterofferToUpdate = exchangeCounterofferRepository.findById(request.id)

        if (exchangeCounterofferToUpdate.isPresent) {
            val currentECO = exchangeCounterofferToUpdate.get()
            currentECO.status = request.status
            return exchangeCounterofferRepository.save(currentECO)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange request not found.")
        }
    }

    public fun getAll(pageable: Pageable): List<ExchangeCounteroffer> {
        val exchangeCounterofferEntities = exchangeCounterofferRepository.findAll(pageable)
        val exchangeCounteroffer = exchangeCounterofferEntities.map { it }
        return exchangeCounteroffer.content
    }

    public fun getById(id: Long): Optional<ExchangeCounteroffer> {
        return exchangeCounterofferRepository.findById(id)
    }

    public fun getByCreator(id: Long): List<ExchangeCounterofferDTO> {
        val counterOffers = exchangeCounterofferRepository.findByCreatorId(id)
        return counterOffers.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByReceiver(id: Long): List<ExchangeCounterofferDTO> {
        val counterOffers = exchangeCounterofferRepository.findByReceiverId(id)
        return counterOffers.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByCard(id: Long): List<ExchangeCounterofferDTO> {
        val counterOffers = exchangeCounterofferRepository.findByCard(id)
        return counterOffers.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByEO(id: Long): List<ExchangeCounterofferDTO> {
        val counterOffers = exchangeCounterofferRepository.findByExchangeOffer(id)
        return counterOffers.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByER(id: Long): List<ExchangeCounterofferDTO> {
        val counterOffers = exchangeCounterofferRepository.findByExchangeRequest(id)
        return counterOffers.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByDateRange(start: Date, end: Date): List<ExchangeCounterofferDTO> {
        val counterOffers = exchangeCounterofferRepository.findByDateRange(start, end)
        return counterOffers.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByStatus(status: ExchangeRequestStatus): List<ExchangeCounterofferDTO> {
        val counterOffers = exchangeCounterofferRepository.findByStatus(status)
        return counterOffers.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByCreatorAndDateRange(id: Long, start: Date, end: Date): List<ExchangeCounterofferDTO> {
        val counteroffer = exchangeCounterofferRepository.findByDateRangeAndCreatorId(id, start, end)
        return counteroffer.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByReceiverAndDateRange(id: Long, start: Date, end: Date): List<ExchangeCounterofferDTO> {
        val counteroffer = exchangeCounterofferRepository.findByDateRangeAndReceiverId(id, start, end)
        return counteroffer.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByCreatorAndStatus(id: Long, status: ExchangeRequestStatus): List<ExchangeCounterofferDTO> {
        val counteroffer = exchangeCounterofferRepository.findByCreatorAndStatus(id, status)
        return counteroffer.map { co -> ExchangeCounterofferDTO(co) }
    }

    public fun getByReceiverAndStatus(id: Long, status: ExchangeRequestStatus): List<ExchangeCounterofferDTO> {
        val counteroffer = exchangeCounterofferRepository.findByReceiverAndStatus(id, status)
        return counteroffer.map { co -> ExchangeCounterofferDTO(co) }
    }
}