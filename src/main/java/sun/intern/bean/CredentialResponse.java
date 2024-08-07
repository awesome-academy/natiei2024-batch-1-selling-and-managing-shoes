package sun.intern.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private String expiredAt;
}
