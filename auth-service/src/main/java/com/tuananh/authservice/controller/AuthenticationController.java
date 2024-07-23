package com.tuananh.authservice.controller;

import com.nimbusds.jose.JOSEException;
import com.tuananh.authservice.advice.exception.DuplicateRecordException;
import com.tuananh.authservice.advice.exception.IdInvalidException;
import com.tuananh.authservice.dto.request.AuthenticationRequest;
import com.tuananh.authservice.dto.request.CreateUserRequest;
import com.tuananh.authservice.dto.request.IntrospectRequest;
import com.tuananh.authservice.dto.response.InfoAuthenticationDTO;
import com.tuananh.authservice.dto.response.IntrospectResponse;
import com.tuananh.authservice.dto.response.AuthenticationResponse;
import com.tuananh.authservice.dto.response.UserResponse;
import com.tuananh.authservice.service.AuthenticationService;
import com.tuananh.authservice.service.UserService;
import com.tuananh.authservice.util.CookieUtil;
import com.tuananh.authservice.util.CustomHeaders;
import com.tuananh.authservice.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;
    CookieUtil cookieUtil;

    @GetMapping("/test")
    ResponseEntity<?> test(@RequestHeader(CustomHeaders.X_AUTH_USER_ID) String id, @RequestHeader(CustomHeaders.X_AUTH_USER_AUTHORITIES) String authorities) {

        return ResponseEntity.ok(id + authorities);
    }

    @PostMapping("/outbound/authentication")
    ResponseEntity<AuthenticationResponse> outboundAuthenticate(
            @RequestParam("code") String code
    ){
        InfoAuthenticationDTO infoAuthenticationDTO = authenticationService.outboundAuthenticate(code);

        // set refreshToken Cookie
        ResponseCookie resCookies = cookieUtil.createRefreshToken(infoAuthenticationDTO.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(infoAuthenticationDTO.getAuthenticationResponse());
    }


    @PostMapping("/introspect")
    ResponseEntity<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request);
        return ResponseEntity.ok().body(result);
    }

    @ApiMessage("User login")
    @PostMapping("/login")
    ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        InfoAuthenticationDTO infoAuthenticationDTO = authenticationService.authenticate(request);

        // set refreshToken Cookie
        ResponseCookie resCookies = cookieUtil.createRefreshToken(infoAuthenticationDTO.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(infoAuthenticationDTO.getAuthenticationResponse());
    }

    @ApiMessage("Register a new user")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createNewUser(@Valid @RequestBody CreateUserRequest postManUser) {
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new DuplicateRecordException("USER ", "Email", postManUser.getEmail());}

        UserResponse userResponse = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @ApiMessage("Refresh Token")
    @PostMapping("/refresh")
    ResponseEntity<AuthenticationResponse> refreshToken(@CookieValue(name = "refresh_token", defaultValue = "defaultToken") String refresh_token) throws IdInvalidException, ParseException, JOSEException {
        if (refresh_token.equals("defaultToken")) {
            throw new IdInvalidException("You do not have a refresh token in the cookie");
        }

        InfoAuthenticationDTO infoAuthenticationDTO = authenticationService.refreshToken(refresh_token);

        ResponseCookie resCookies = cookieUtil.removeRefreshToken();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(infoAuthenticationDTO.getAuthenticationResponse());
    }

    @PostMapping("/logout")
    @ApiMessage("User logout")
    ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", defaultValue = "defaultToken") String refresh_token) throws IdInvalidException, ParseException, JOSEException {
        if (refresh_token.equals("defaultToken")) {
            throw new IdInvalidException("You do not have a refresh token in the cookie");
        }
        authenticationService.logout(refresh_token);

        // Remove cookie
        ResponseCookie deleteCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(null);
    }
}