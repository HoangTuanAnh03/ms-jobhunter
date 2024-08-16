package com.tuananh.authservice.controller;

import com.nimbusds.jose.JOSEException;
import com.tuananh.authservice.advice.exception.DuplicateRecordException;
import com.tuananh.authservice.advice.exception.IdInvalidException;
import com.tuananh.authservice.dto.ApiResponse;
import com.tuananh.authservice.dto.request.AuthenticationRequest;
import com.tuananh.authservice.dto.request.CreateUserRequest;
import com.tuananh.authservice.dto.request.IntrospectRequest;
import com.tuananh.authservice.dto.response.AuthenticationResponse;
import com.tuananh.authservice.dto.response.InfoAuthenticationDTO;
import com.tuananh.authservice.dto.response.IntrospectResponse;
import com.tuananh.authservice.dto.response.UserResponse;
import com.tuananh.authservice.service.AuthenticationService;
import com.tuananh.authservice.service.UserService;
import com.tuananh.authservice.util.CookieUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    CookieUtil cookieUtil;

    @PostMapping("/outbound/authentication")
    ResponseEntity<ApiResponse<AuthenticationResponse>> outboundAuthenticate(
            @RequestParam("code") String code
    ){
        InfoAuthenticationDTO infoAuthenticationDTO = authenticationService.outboundAuthenticate(code);

        // set refreshToken Cookie
        ResponseCookie resCookies = cookieUtil.createRefreshToken(infoAuthenticationDTO.getRefreshToken());

        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(infoAuthenticationDTO.getAuthenticationResponse())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(apiResponse);
    }


    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        InfoAuthenticationDTO infoAuthenticationDTO = authenticationService.authenticate(request);

        // set refreshToken Cookie
        ResponseCookie resCookies = cookieUtil.createRefreshToken(infoAuthenticationDTO.getRefreshToken());

        logger.debug("fetchCustomerDetails method start");
        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.OK.value())
                .message("User login")
                .data(infoAuthenticationDTO.getAuthenticationResponse())
                .build();
        logger.debug("fetchCustomerDetails method end");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(apiResponse);
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> createNewUser(@Valid @RequestBody CreateUserRequest postManUser) {
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new DuplicateRecordException("USER ", "Email", postManUser.getEmail());}

        UserResponse userResponse = this.userService.handleCreateUser(postManUser);
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Register a new user")
                .data(userResponse)
                .build();
    }

    @PostMapping("/refresh")
    ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(@CookieValue(name = "refresh_token", defaultValue = "defaultToken") String refresh_token) throws IdInvalidException, ParseException, JOSEException {
        if (refresh_token.equals("defaultToken")) {
            throw new IdInvalidException("You do not have a refresh token in the cookie");
        }

        InfoAuthenticationDTO infoAuthenticationDTO = authenticationService.refreshToken(refresh_token);

        ResponseCookie resCookies = cookieUtil.removeRefreshToken();

        ApiResponse<AuthenticationResponse> apiResponse = ApiResponse.<AuthenticationResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Refresh Token")
                .data(infoAuthenticationDTO.getAuthenticationResponse())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(apiResponse);
    }

    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(@CookieValue(name = "refresh_token", defaultValue = "defaultToken") String refresh_token) throws IdInvalidException, ParseException, JOSEException {
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

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("User logout")
                .data(null)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(apiResponse);
    }
}