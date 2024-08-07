package sun.intern.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sun.intern.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
