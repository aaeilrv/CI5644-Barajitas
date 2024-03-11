package com.example.demo.service

import com.example.demo.controller.dto.CreateExchangeRequestDTO
import com.example.demo.controller.dto.CreateExchangeRequestRequest
import com.example.demo.controller.dto.ExchangeRequestDTO
import com.example.demo.controller.dto.UpdateExchangeRequestRequest
import com.example.demo.model.*
import com.example.demo.repo.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.NoSuchElementException

@Service
class ExchangeRequestService(@Autowired private val exchangeRequestRepository: ExchangeRequestRepository,
                             private val userService: UserService,
                             private val cardService: CardService) {

    public fun create(exchangeRequest: CreateExchangeRequestDTO): ExchangeRequest {
        val foundUser = userService.getById(exchangeRequest.userId).orElseThrow{
            NoSuchElementException("User not found.")
        }
        val foundCard = cardService.getById(exchangeRequest.requestedCardId).orElseThrow{
            NoSuchElementException("Card not found.")
        }

        val newER:CreateExchangeRequestRequest
        try {
            newER = CreateExchangeRequestRequest(
                    user = foundUser,
                    requestedCard = foundCard,
                    requestStatus = "PENDING",
                    createdAt = Timestamp.from(Instant.now())
            )

        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Error creating the exchange request")
        }
        return exchangeRequestRepository.save(ExchangeRequest(newER))
    }

    public fun updateExchangeRequest(request: UpdateExchangeRequestRequest): ExchangeRequest {
        val exchangeRequestToUpdate = exchangeRequestRepository.findById(request.id)

        if (exchangeRequestToUpdate.isPresent) {
            val currentER = exchangeRequestToUpdate.get()
            currentER.status = request.status
            return exchangeRequestRepository.save(currentER)
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange request not found.")
        }
    }

    public fun getAll(pageable: Pageable): List<ExchangeRequest> {
        val exchangeRequestEntities = exchangeRequestRepository.findAll(pageable)
        val exchangeRequests = exchangeRequestEntities.map { it }
        return exchangeRequests.content
    }

    public fun getById(id: Long): Optional<ExchangeRequest> {
        return exchangeRequestRepository.findById(id)
    }

    fun getByUserid(userId: Long): List<ExchangeRequestDTO> {
        val exchangeRequests = exchangeRequestRepository.findByUserId(userId)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }

    public fun getByCardId(cardId: Long): List<ExchangeRequestDTO> {
        val exchangeRequests = exchangeRequestRepository.findByCardId(cardId)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }

    //// NO FUNCIONAN AÚN POR EL ENUM
    public fun getByStatus(status: ExchangeRequestStatus): List<ExchangeRequestDTO> {
        val exchangeRequests =  exchangeRequestRepository.findByStatus(ExchangeRequestStatus.ACCEPTED)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }


    public fun getByUseridByStatus(userId: Long, status: ExchangeRequestStatus): List<ExchangeRequestDTO> {
        val exchangeRequests = exchangeRequestRepository.findByUserIdAndStatus(userId, status)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }
    /////

    public fun getByUserIdAndCardId(userId: Long, cardId: Long): List<ExchangeRequestDTO>  {
        val exchangeRequests = exchangeRequestRepository.findByUserIdAndCardId(userId, cardId)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }

    public fun getByDateRange(startDate: Date, endDate: Date): List<ExchangeRequestDTO> {
        val exchangeRequests = exchangeRequestRepository.findByDateRange(startDate, endDate)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }

    public fun getByUserIdWithinDateRange(userId: Long, startDate: Date, endDate: Date): List<ExchangeRequestDTO> {
        val exchangeRequests = exchangeRequestRepository.findByUserIdWithinDateRange(userId, startDate, endDate)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }

    public fun getAllPossibleERbyUser(id: Long): List<ExchangeRequestDTO> {
        val exchangeRequests = exchangeRequestRepository.findAllERbyUserOwner(id)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }

    public fun getAllPossibleERbyCardsOwnerAndRequester(ownerId: Long, requesterId: Long): List<ExchangeRequestDTO> {
        val exchangeRequests = exchangeRequestRepository.findAllPossibleERbyCardsOwnerAndRequester(ownerId, requesterId)
        return exchangeRequests.map { requests -> ExchangeRequestDTO(requests) }
    }
}