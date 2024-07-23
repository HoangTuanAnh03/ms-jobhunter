package com.tuananh.authservice.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.tuananh.authservice.advice.AppException;
import com.tuananh.authservice.advice.ErrorCode;
import com.tuananh.authservice.dto.request.AuthenticationRequest;
import com.tuananh.authservice.dto.request.IntrospectRequest;
import com.tuananh.authservice.dto.response.InfoAuthenticationDTO;
import com.tuananh.authservice.dto.response.IntrospectResponse;
import com.tuananh.authservice.dto.response.AuthenticationResponse;
import com.tuananh.authservice.entity.InvalidatedToken;
import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.repository.UserRepository;
import com.tuananh.authservice.service.AuthenticationService;
import com.tuananh.authservice.service.InvalidatedTokenService;
import com.tuananh.authservice.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    AuthenticationManagerBuilder authenticationManagerBuilder;
    UserRepository userRepository;
    InvalidatedTokenService invalidatedTokenService;
    SecurityUtil securityUtil;

    /**
     * @param introspectRequest -IntrospectRequest Object
     * @return IntrospectResponse Object if the token is valid or not
     */
    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        var token = introspectRequest.getToken();
        try {
            SignedJWT signedJWT = securityUtil.verifyToken(token);
            String uid = signedJWT.getJWTClaimsSet().getClaim("uid").toString();
            String authorities = signedJWT.getJWTClaimsSet().getClaim("scope").toString();
            return IntrospectResponse.builder().valid(true).uid(uid).authorities(authorities).build();
        } catch (AppException | JOSEException | ParseException e) {
            return IntrospectResponse.builder().valid(false).uid("").authorities("").build();
        }
    }

    /**
     * @param request -AuthenticationRequest Object
     * @return User Details based on a given email and password
     */
    @Override
    public InfoAuthenticationDTO authenticate(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());
        // authentication user => override loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return createInfoAuthenticationDTO(user);
    }

    /**
     * @param refreshToken - refreshToken get from cookie
     */
    @Override
    public void logout(String refreshToken) throws ParseException, JOSEException {
        var signToken = securityUtil.verifyToken(refreshToken);

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenService.createInvalidatedToken(invalidatedToken);
    }

    /**
     * @param refreshToken - refreshToken get from cookie
     * @return User Details based on a given refreshToken
     */
    @Override
    public InfoAuthenticationDTO refreshToken(String refreshToken) throws ParseException, JOSEException {
        var signedJWT = securityUtil.verifyToken(refreshToken);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenService.createInvalidatedToken(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        return createInfoAuthenticationDTO(user);
    }

    /**
     * @param user - User Object
     * @return Convert User Object to InfoAuthenticationDTO Object
     */
    @Override
    public InfoAuthenticationDTO createInfoAuthenticationDTO(User user) {
        var accessToken = securityUtil.generateToken(user, false);
        var refreshToken = securityUtil.generateToken(user, true);

        AuthenticationResponse.UserLogin userLogin = AuthenticationResponse.UserLogin.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role("ROLE_" + user.getRole().getName())
                .build();

        AuthenticationResponse resLoginDTO = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .user(userLogin)
                .build();

        return InfoAuthenticationDTO.builder().refreshToken(refreshToken).authenticationResponse(resLoginDTO).build();
    }
}
