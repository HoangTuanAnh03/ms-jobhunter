package com.tuananh.authservice.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.tuananh.authservice.advice.AppException;
import com.tuananh.authservice.advice.ErrorCode;
import com.tuananh.authservice.advice.exception.ResourceNotFoundException;
import com.tuananh.authservice.dto.request.AuthenticationRequest;
import com.tuananh.authservice.dto.request.ExchangeTokenRequest;
import com.tuananh.authservice.dto.request.IntrospectRequest;
import com.tuananh.authservice.dto.response.IntrospectResponse;
import com.tuananh.authservice.dto.response.AuthenticationResponse;
import com.tuananh.authservice.entity.InvalidatedToken;
import com.tuananh.authservice.entity.Role;
import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.repository.RoleRepository;
import com.tuananh.authservice.repository.UserRepository;
import com.tuananh.authservice.service.AuthenticationService;
import com.tuananh.authservice.service.InvalidatedTokenService;
import com.tuananh.authservice.service.client.OutboundIdentityClient;
import com.tuananh.authservice.service.client.OutboundUserClient;
import com.tuananh.authservice.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    AuthenticationManagerBuilder authenticationManagerBuilder;
    UserRepository userRepository;
    InvalidatedTokenService invalidatedTokenService;
    SecurityUtil securityUtil;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    RoleRepository roleRepository;

    @NonFinal
    @Value("${auth.outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${auth.outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${auth.outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    /**
     * @param code
     * @return
     */
    @Override
    public AuthenticationResponse outboundAuthenticate(String code) {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());

        log.info("TOKEN RESPONSE {}", response);

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        log.info("User Info {}", userInfo);

//        Set<Role> roles = new HashSet<>();
//        roles.add(Role.builder().name(PredefinedRole.USER_ROLE).build());

        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleName", "USER" )
        );

        var user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                () -> userRepository.save(User.builder()
                                .name(userInfo.getName())
                                .email(userInfo.getEmail())
                                .password("")
                                .active(true)
                                .role(role)
                                .build()));

        return createAuthenticationResponse(user);
    }

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
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());
        // authentication user => override loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return createAuthenticationResponse(user);
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
    public AuthenticationResponse refreshToken(String refreshToken) throws ParseException, JOSEException {
        var signedJWT = securityUtil.verifyToken(refreshToken);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenService.createInvalidatedToken(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        return createAuthenticationResponse(user);
    }

    /**
     * @param user - User Object
     * @return Convert User Object to InfoAuthenticationDTO Object
     */
    @Override
    public AuthenticationResponse createAuthenticationResponse(User user) {
        var accessToken = securityUtil.generateToken(user, false);
        var refreshToken = securityUtil.generateToken(user, true);

        AuthenticationResponse.UserLogin userLogin = AuthenticationResponse.UserLogin.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role("ROLE_" + user.getRole().getName())
                .noPassword(!StringUtils.hasText(user.getPassword()))
                .build();

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userLogin)
                .build();
    }

}
