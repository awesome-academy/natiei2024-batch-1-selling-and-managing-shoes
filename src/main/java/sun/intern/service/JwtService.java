package sun.intern.service;

import org.springframework.security.core.userdetails.UserDetails;
import sun.intern.bean.CredentialResponse;

public interface JwtService {
    UserDetails getAccountFromToken(String token);

    CredentialResponse generateToken(int accountId);

    CredentialResponse refreshToken(String refreshToken, boolean isAdmin);
}
