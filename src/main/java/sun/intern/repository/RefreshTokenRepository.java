package sun.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sun.intern.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByAccount_Id(int accountId);
}
