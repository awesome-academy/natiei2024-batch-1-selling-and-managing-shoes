package sun.intern.util.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sun.intern.model.Account;
import sun.intern.model.Enum.AccountRole;
import sun.intern.repository.AccountRepository;

import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
@Component
@Order(1)
public class AccountSeeder implements CommandLineRunner {
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Customer account
        var customerEmail = "customer@gmail.com";
        if (!repository.existsByEmail(customerEmail)) {
            var customerAccount = Account.builder()
                .email(customerEmail)
                .address("Viet Nam")
                .displayName("Customer")
                .fullName("Customer")
                .gender(true)
                .isActivated(true)
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("0123456789")
                .role(AccountRole.CUSTOMER)
                .dateOfBirth(LocalDate.of(2003, 1, 1))
                .build();
            try {
                repository.save(customerAccount);
            } catch (Exception e) {
                log.error("Error seeding customer account: {}", e.getMessage());
            }
        }

        // Seller account
        var sellerEmail = "seller@gmail.com";
        if (!repository.existsByEmail(sellerEmail)) {
            var sellerAccount = Account.builder()
                .email(sellerEmail)
                .address("Viet Nam")
                .displayName("Seller")
                .fullName("Seller")
                .gender(true)
                .isActivated(true)
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("0123456789")
                .role(AccountRole.SELLER)
                .dateOfBirth(LocalDate.of(2003, 1, 1))
                .build();
            try {
                repository.save(sellerAccount);
            } catch (Exception e) {
                log.error("Error seeding seller account: {}", e.getMessage());
            }
        }

        // Admin account
        var adminEmail = "admin@gmail.com";
        if (!repository.existsByEmail(adminEmail)) {
            var adminAccount = Account.builder()
                .email(adminEmail)
                .address("Viet Nam")
                .displayName("Admin")
                .fullName("Admin")
                .gender(true)
                .isActivated(true)
                .password(passwordEncoder.encode("12345678"))
                .phoneNumber("0123456789")
                .role(AccountRole.ADMIN)
                .dateOfBirth(LocalDate.of(2003, 1, 1))
                .build();
            try {
                repository.save(adminAccount);
            } catch (Exception e) {
                log.error("Error seeding admin account: {}", e.getMessage());
            }
        }
    }
}
