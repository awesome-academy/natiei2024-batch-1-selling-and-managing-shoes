package sun.intern.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sun.intern.bean.CredentialResponse;
import sun.intern.config.JwtApplicationProperty;
import sun.intern.model.Account;
import sun.intern.model.Enum.AccountRole;
import sun.intern.repository.AccountRepository;
import sun.intern.repository.RefreshTokenRepository;
import sun.intern.service.JwtService;
import sun.intern.util.constant.ErrorMessageConstant;
import sun.intern.util.exception.ForbiddenException;
import sun.intern.util.exception.UnauthorizedException;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtApplicationProperty jwtAppProperty;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDetails getAccountFromToken(String token) {
        Claims jwtClaims = getJwtClaims(token, TokenType.ACCESS_TOKEN);
        int accountId = Integer.parseInt(jwtClaims.getSubject());

        return accountRepository.findById(accountId)
            .orElseThrow(() -> new ForbiddenException(ErrorMessageConstant.FORBIDDEN));
    }

    @Override
    public CredentialResponse generateToken(int accountId) {
        String accessToken = generateAccessToken(accountId);
        return CredentialResponse.builder()
            .accessToken(accessToken)
            .refreshToken(generateRefreshToken(accountId))
            .expiredAt(new Timestamp(getJwtClaims(accessToken, TokenType.ACCESS_TOKEN).getExpiration().getTime()).toString())
            .type("Bearer")
            .build();
    }

    @Override
    public CredentialResponse refreshToken(String refreshToken, boolean isAdmin) {
        Claims jwtClaims = getJwtClaims(refreshToken, TokenType.REFRESH_TOKEN);
        int accountId = Integer.parseInt(jwtClaims.getSubject());
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ForbiddenException(ErrorMessageConstant.FORBIDDEN));
        // Check if the user is an admin
        if (isAdmin && account.getRole() != AccountRole.ADMIN) {
            throw new ForbiddenException(ErrorMessageConstant.FORBIDDEN);
        }
        var accountRefreshToken = refreshTokenRepository.findByAccount_Id(accountId).orElse(null);
        if (accountRefreshToken != null && accountRefreshToken.getToken().equals(refreshToken)) {
            String accessToken = generateAccessToken(accountId);
            return CredentialResponse.builder()
                .accessToken(accessToken)
                .type("Bearer")
                .refreshToken(accountRefreshToken.getToken())
                .expiredAt(
                    new Timestamp(getJwtClaims(accessToken, TokenType.ACCESS_TOKEN).getExpiration().getTime()).toString())
                .build();
        }
        throw new ForbiddenException(ErrorMessageConstant.REFRESH_TOKEN_NOT_FOUND);
    }

    private String generateAccessToken(int accountId) {
        return Jwts.builder()
            .subject(String.valueOf(accountId))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtAppProperty.getAccessTokenExpirationMs()))
            .signWith(getSignInKey(jwtAppProperty.getAccessTokenSecret()))
            .compact();
    }

    private String generateRefreshToken(int accountId) {
        return Jwts.builder()
            .subject(String.valueOf(accountId))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtAppProperty.getRefreshTokenExpirationMs()))
            .signWith(getSignInKey(jwtAppProperty.getRefreshTokenSecret()))
            .compact();
    }

    private Claims getJwtClaims(String token, TokenType tokenType) {
        switch (tokenType) {
            case ACCESS_TOKEN:
                try {
                    return Jwts.parser()
                        .verifyWith(getSignInKey(jwtAppProperty.getAccessTokenSecret()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                } catch (ExpiredJwtException ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.EXPIRED_TOKEN);
                } catch (Exception ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.INVALID_TOKEN);
                }
            case REFRESH_TOKEN:
                try {
                    return Jwts.parser()
                        .verifyWith(getSignInKey(jwtAppProperty.getRefreshTokenSecret()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                } catch (ExpiredJwtException ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.EXPIRED_REFRESH_TOKEN);
                } catch (Exception ex) {
                    throw new UnauthorizedException(ErrorMessageConstant.INVALID_REFRESH_TOKEN);
                }
            default:
                throw new UnauthorizedException(ErrorMessageConstant.INVALID_TOKEN);
        }
    }

    private SecretKey getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    enum TokenType {
        ACCESS_TOKEN, REFRESH_TOKEN
    }
}
