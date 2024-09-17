package com.mycoolcar.repositories;


import com.mycoolcar.entities.User;
import com.mycoolcar.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;


public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional <VerificationToken> findByToken(String token);

    Optional <VerificationToken> findByUser(User user);

    Stream<VerificationToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(LocalDateTime now);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(LocalDateTime now);
}
