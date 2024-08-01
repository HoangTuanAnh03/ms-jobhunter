package com.tuananh.resumeservice.dto.request;

import com.tuananh.resumeservice.util.constant.LevelEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateResumeRequest {
    @NotBlank(message = "Url cannot be blank")
    String url;
}