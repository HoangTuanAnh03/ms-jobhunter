package com.tuananh.resumeservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateResumeRequest {
    @NotBlank(message = "Url cannot be blank")
    String url;

    long jobId;
}