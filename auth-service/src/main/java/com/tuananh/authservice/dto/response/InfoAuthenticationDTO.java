package com.tuananh.authservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InfoAuthenticationDTO {
    String refreshToken;
    AuthenticationResponse authenticationResponse;
}