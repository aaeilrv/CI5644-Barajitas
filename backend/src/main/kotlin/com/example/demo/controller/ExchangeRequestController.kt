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
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
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
    fun createExchangeRequest(principal: JwtAuthenticationToken, @RequestBody request: CreateExchangeRequestDTO): ResponseEntity<ExchangeRequestDTO> {
        val sub = principal.tokenAttributes["sub"]?.toString() ?: return ResponseEntity.internalServerError().build()
        return ResponseEntity.ok(ExchangeRequestDTO(exchangeRequestService.create(request, sub)))
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
        val exchangeRequest = exchangeRequestService.getById(id).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange request with $id not found.")
        }
        return ResponseEntity.ok(ExchangeRequestDTO(exchangeRequest))
    }

    // Todos los Exchange Requests creados por un usuario
    @GetMapping("/user/me")
    fun getMyExchangeRequests(principal: JwtAuthenticationToken): ResponseEntity<List<ExchangeRequestDTO>> {
        val sub = principal.tokenAttributes["sub"]?.toString() ?: return ResponseEntity.internalServerError().build()
        return ResponseEntity.ok(exchangeRequestService.getByUserSub(sub))
    }

    // Todos los ER de una barajita en particular
    @GetMapping("/card/{id}")
    fun getExchangeRequestByCardId(@PathVariable id: Long): List<ExchangeRequestDTO> {
        return exchangeRequestService.getByCardId(id)
    }

    // Todos los ER de un usuario y de una barajita de ese usuario
    @GetMapping("/user/me/card/{cardId}")
    fun getMyExchangeRequestByCardId(principal: JwtAuthenticationToken,
                                     @PathVariable cardId: Long): ResponseEntity<List<ExchangeRequestDTO>> {
        val sub = principal.tokenAttributes["sub"]?.toString() ?: return ResponseEntity.internalServerError().build()
        return ResponseEntity.ok(exchangeRequestService.getByUserSubAndCardId(sub, cardId))
    }

    // Todos los ER por rango de fechas
    @GetMapping("/user/me/start/{start}/end/{end}")
    fun getMyExchangeRequestsByDateRange(principal: JwtAuthenticationToken, 
                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) start: Date, 
                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) end: Date): ResponseEntity<List<ExchangeRequestDTO>> {
        val sub = principal.tokenAttributes["sub"]?.toString() ?: return ResponseEntity.internalServerError().build()
        return ResponseEntity.ok(exchangeRequestService.getByUserSubWithinDateRange(sub, start, end))
    }

    // Todos los ER que piden barajitas que un usuario tiene
    @GetMapping("/my/hasCards")
    fun getMyExchangeRequestsByOwnership(principal: JwtAuthenticationToken): ResponseEntity<List<ExchangeRequestDTO>> {
        val sub = principal.tokenAttributes["sub"]?.toString() ?: return ResponseEntity.internalServerError().build()
        return ResponseEntity.ok(exchangeRequestService.getAllPossibleERbyUserSub(sub))
    }

    // Todos los ER creados por user que piden barajitas que owner tiene
    @GetMapping("/hasCards/{ownerSub}/user/me")
    fun getExchangeRequestsForMyCardsOwnedByOther(principal: JwtAuthenticationToken,
                                                  @PathVariable ownerSub: String): ResponseEntity<List<ExchangeRequestDTO>> {
        val sub = principal.tokenAttributes["sub"]?.toString() ?: return ResponseEntity.internalServerError().build()
        return ResponseEntity.ok(exchangeRequestService.getAllPossibleERbyCardsOwnerAndRequesterSub(ownerSub, sub))
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