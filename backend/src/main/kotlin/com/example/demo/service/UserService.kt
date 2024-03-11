package com.example.demo.service

import com.example.demo.controller.dto.CreateUserRequest
import com.example.demo.controller.dto.LeaderboardResponse
import com.example.demo.controller.dto.UserDTO
import com.example.demo.model.User
import com.example.demo.model.Card
import com.example.demo.repo.UserRepository
import com.example.demo.model.Ownership
import com.example.demo.repo.CardRepository
import com.example.demo.repo.OwnershipRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import com.example.demo.service.CardService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.stream.Collectors

@Service
class UserService (@Autowired private val userRepository: UserRepository,
                   @Autowired private val ownershipRepository: OwnershipRepository) {
//    public fun findAll(): List<UserDTO> {
//        return userRepository.findAll()
//            .stream()
//            .map(this::fromEntityToDTO)
//            .collect(Collectors.toList())
//    }

    public fun create(user: User): User {
        return userRepository.save(user)
    }

    public fun getBySub(sub: String): Optional<User> {
        return userRepository.findByAuth0Sub(sub)
    }

        public fun getById(id: Long): Optional<User> {
        return userRepository.findById(id)
    }


    public fun getCardsOwnedBySub(sub: String, pageable: Pageable): Page<Ownership>?{
        val exists: Optional<User> = getBySub(sub)
        if(exists.isPresent) {
            val user: User = exists.get()
            return ownershipRepository.findByUserOrderByCardCountry(user, pageable)
        }
        else{
            return null
        }
    }

    public fun updateCardsOwnedList(sub: String, card : Card): User?{
        val exists: Optional<User> = getBySub(sub)
        if(exists.isPresent){
            val user: User = exists.get()
            //See if user already owns card
            val ownershipValue: Ownership? = user.getCardsOwned().firstOrNull {it.getCard().getId() == card.getId()}
            if (ownershipValue == null) {
                val newOwnershipValue = Ownership(
                    null,
                    user,
                    card,
                    1
                )
                user.getCardsOwned().add(newOwnershipValue)
                ownershipRepository.save(newOwnershipValue)
            } else {
                ownershipValue.setNumberOwned(ownershipValue.getNumberOwned() + 1)
                ownershipRepository.save(ownershipValue)
            }
            return userRepository.save(user)
        }
        return null
    }

    public fun getAll(pageable: Pageable): List<User> {
        val userEntities = userRepository.findAll(pageable)
        val users = userEntities.map { it }
        return users.content
    }

    fun getProgress(sub: String):String{
        val userOpt = getBySub(sub)
        if (userOpt.isPresent) {
            val cnt = userOpt.get().getCardsOwned().size
            val percent = cnt*100.00/670
            val decimal = BigDecimal(percent).setScale(2, RoundingMode.HALF_EVEN)
            return "$decimal%"
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with $sub not found")
        }
    }
    fun getLeaders(allUser: List<User>): List<LeaderboardResponse> {
        val listLeaders: MutableList<Pair<User, String>> = mutableListOf()
        var counter = 1
        for (user: User in allUser) {
            listLeaders.add(user to getProgress(user.getAuth0Sub()))
        }
        listLeaders.sortBy { it.second }
        listLeaders.reverse()
        val listLeadersForPrinting: MutableList<LeaderboardResponse> = mutableListOf()
        for (leaders: Pair<User, String> in listLeaders) {
            listLeadersForPrinting.add(
                    LeaderboardResponse(
                            counter,
                            leaders.first.getUsername(),
                            leaders.first.getCardsOwned().size
                    )
            )
            counter += 1
        }
        return listLeadersForPrinting
    }

    fun editUserData(user: User, body : Map<String, String>) : User{

        user.setFirstName(body.get("firstName")!!)
        user.setLastName(body.get("lastName")!!)
        user.setUsername(body.get("username")!!)
        user.setEmail(body.get("email")!!)

        return userRepository.save(user)
    }


//    private fun fromEntityToDTO(entity: User): UserDTO {
//        return UserDTO(
//            entity.getId(),
//            entity.getFirstName(),
//            entity.getLastName(),
//            entity.getBirthDay(),
//            entity.getUsername(),
//            entity.getEmailAddress()
//        )
//    }
}
