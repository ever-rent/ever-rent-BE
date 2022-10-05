package com.finalproject.everrent_be.domain.token.model;



import com.finalproject.everrent_be.domain.token.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKkey(String key);

}
