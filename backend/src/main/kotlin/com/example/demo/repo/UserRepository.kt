package com.example.demo.repo

import com.example.demo.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByAuth0Sub(sub: String): Optional<User>
}