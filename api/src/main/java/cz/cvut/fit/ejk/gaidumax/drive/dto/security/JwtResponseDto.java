package cz.cvut.fit.ejk.gaidumax.drive.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String type;
}
