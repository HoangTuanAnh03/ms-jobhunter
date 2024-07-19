package com.tuananh.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuananh.authservice.util.constant.GenderEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    String name;

    @Past(message = "Date of birth must be before current date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dob;

    @Pattern(regexp = "^0\\d{9}$", message = "Invalid phone number")
    @JsonProperty("mobile_number")
    String mobileNumber;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    String address;

    long companyId;
}