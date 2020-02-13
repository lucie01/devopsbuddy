package com.devopsbuddy.backend.persistence.repositories;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    boolean existsByToken(String token);

    @Query("select ptr from PasswordResetToken ptr inner join ptr.user u where ptr.user = ?1")
    Set<PasswordResetToken> findAllByUserId(long userId);
}
