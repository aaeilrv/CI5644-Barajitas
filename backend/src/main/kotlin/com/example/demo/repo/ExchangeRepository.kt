package com.example.demo.repo

import com.example.demo.controller.dto.ExchangeRequestDTO
import com.example.demo.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Repository
interface ExchangeRequestRepository: JpaRepository<ExchangeRequest, Long> {

    // Todos los ER creados por cierto usuario
    @Query("SELECT er FROM ExchangeRequest er WHERE er.requester.id = :id")
    fun findByUserId(id: Long): List<ExchangeRequest>

    // Todos los ER de una barajita en particular
    @Query("SELECT er FROM ExchangeRequest er WHERE er.requestedCard.id = :id")
    fun findByCardId(id: Long): List<ExchangeRequest>

    // Todos los ER de una barajita en particular de un usuario (debería existir sólo uno)
    @Query("SELECT er FROM ExchangeRequest er WHERE er.requestedCard.id = :cardId AND er.requester.id = :userId")
    fun findByUserIdAndCardId(userId: Long, cardId: Long): List<ExchangeRequest>

    // Todos los ER creados en cierto rango de tiempo
    @Query("SELECT er FROM ExchangeRequest er WHERE er.createdAt BETWEEN :startDate AND :endDate")
    fun findByDateRange(startDate: Date, endDate: Date): List<ExchangeRequest>

    // Todos los ER de cierto usuario creados en cierto rango de tiempo
    @Query("SELECT er FROM ExchangeRequest er WHERE er.requester.id = :userId AND er.createdAt BETWEEN :startDate AND :endDate")
    fun findByUserIdWithinDateRange(userId: Long, startDate: Date, endDate: Date): List<ExchangeRequest>

    // Todos los ER que piden barajitas que un usuario tiene (es decir, todos los ER que le interesan a ese usuario)
    @Query("""
        SELECT er
        FROM ExchangeRequest er
        JOIN Ownership o ON er.requestedCard.id = o.card.id
        WHERE o.user.id = :id
    """)
    fun findAllERbyUserOwner(id: Long): List<ExchangeRequest>

    // Todos los ER de un usuario que piden barajitas que otro usuario tiene
    @Query("""
        SELECT er
        FROM ExchangeRequest er
        JOIN Ownership o ON er.requestedCard.id = o.card.id
        WHERE o.user.id = :ownerId
        AND er.requester.id = :requesterId
    """)
    fun findAllPossibleERbyCardsOwnerAndRequester(ownerId: Long, requesterId: Long): List<ExchangeRequest>

    // Todos los ER de cierto estatus
    @Query("SELECT er FROM ExchangeRequest er WHERE er.status = :status")
    fun findByStatus(status: ExchangeRequestStatus): List<ExchangeRequest>

    // Todos los ER con cierto estatus de un usuario en particular
    @Query("SELECT er FROM ExchangeRequest er WHERE er.requester.id = :id AND er.status = :status")
    fun findByUserIdAndStatus(id: Long, status: ExchangeRequestStatus): List<ExchangeRequest>
}

@Repository
interface ExchangeOfferRepository: JpaRepository<ExchangeOffer, Long> {

    // Todos los EO con cierto estatus
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.status = :status")
    fun findByStatus(status: ExchangeOfferStatus): List<ExchangeOffer>

    // Todos los EO creados por un usuario
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.bidder.id = :id")
    fun findByCreatedUserId(id: Long): List<ExchangeOffer>

    // Todos los EO recibidos por un usuario (ER.user_id)
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.exchangeRequest.requester.id = :id")
    fun findByReceiverUserid(id: Long): List<ExchangeOffer>

    // Todos los EO que ha recibido un exchange_request
    @Query("""
        SELECT eo
        FROM ExchangeOffer eo
        WHERE eo.exchangeRequest.id = :id
    """)
    fun findByExchangeRequest(id: Long): List<ExchangeOffer>

    // Todos los EO ofreciendo cierta barajita
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.offeredCard.id = :cardId")
    fun findByCard(cardId: Long): List<ExchangeOffer>

    // Todos los EO creados en cierto rango de tiempo
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.createdAt BETWEEN :startDate AND :endDate")
    fun findByDateRange(startDate: Date, endDate: Date): List<ExchangeOffer>

    // Todos los EO creados por un usuario y con un status
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.bidder.id = :id AND eo.status = :status")
    fun findByCreatedUserIdAndStatus(id: Long, status: ExchangeOfferStatus): List<ExchangeOffer>

    // Todos los EO creados por un usuario en cierto rango de tiempo
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.bidder.id = :bidderId AND eo.createdAt BETWEEN :startDate AND :endDate")
    fun findByDateRangeAndCreatorId(bidderId: Long, startDate: Date, endDate: Date): List<ExchangeOffer>

    // Todos los EO recibidos por un usuario en cierto rango de tiempo
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.exchangeRequest.requester.id = :receiverId AND eo.createdAt BETWEEN :startDate AND :endDate")
    fun findByDateAndReceiverid(receiverId: Long, startDate: Date, endDate: Date): List<ExchangeOffer>

    // Todos los EO de un usuario ofreciendo cierta barajita
    @Query("SELECT eo FROM ExchangeOffer eo WHERE eo.exchangeRequest.requester.id = :bidderId AND eo.offeredCard.id = :cardId")
    fun findByUserAndCardId(bidderId: Long, cardId: Long): List<ExchangeOffer>
}

@Repository
interface ExchangeCounterofferRepository: JpaRepository<ExchangeCounteroffer, Long> {
    // Todos los ECO creados por cierto usuario
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeRequest.requester.id = :id")
    fun findByCreatorId(id: Long): List<ExchangeCounteroffer>

    // Todos los ECO recibidos por cierto usuario
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeOffer.bidder.id = :id")
    fun findByReceiverId(id: Long): List<ExchangeCounteroffer>

    // Todos los ECO con cierto status
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.status = :status")
    fun findByStatus(status: ExchangeRequestStatus): List<ExchangeCounteroffer>

    // Todos los ECO de cierta barajita
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.card.id = :cardId")
    fun findByCard(cardId: Long): List<ExchangeCounteroffer>

    // Todos los ECO de un EO
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeOffer.id = :eoId")
    fun findByExchangeOffer(eoId: Long): List<ExchangeCounteroffer>

    // Todos los ECO de un ER
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeRequest.id = :erId")
    fun findByExchangeRequest(erId: Long): List<ExchangeCounteroffer>

    // Todos los ECO creados en cierto período de tiempo
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.createdAt BETWEEN :startDate AND :endDate")
    fun findByDateRange(startDate: Date, endDate: Date): List<ExchangeCounteroffer>

    // Todos los ECO creados por cierto usuario y con cierto estatus
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeRequest.requester.id = :id AND eco.status = :status")
    fun findByCreatorAndStatus(id: Long, status: ExchangeRequestStatus): List<ExchangeCounteroffer>

    // Todos los ECO recibidos por cierto usuario y con cierto estatus
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeOffer.bidder.id = :id AND eco.status = :status")
    fun findByReceiverAndStatus(id: Long, status: ExchangeRequestStatus): List<ExchangeCounteroffer>

    // Todos los ECO creados por cierto usuario en cierto período de tiempo
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeRequest.requester.id = :creatorId AND eco.createdAt BETWEEN :startDate AND :endDate")
    fun findByDateRangeAndCreatorId(creatorId: Long, startDate: Date, endDate: Date): List<ExchangeCounteroffer>

    // Todos los ECO recibidos por cierto usuario en cierto período de tiempo
    @Query("SELECT eco FROM ExchangeCounteroffer eco WHERE eco.exchangeOffer.bidder.id = :receiverId AND eco.createdAt BETWEEN :startDate AND :endDate")
    fun findByDateRangeAndReceiverId(receiverId: Long, startDate: Date, endDate: Date): List<ExchangeCounteroffer>
}
