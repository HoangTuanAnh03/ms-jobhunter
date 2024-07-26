package com.tuananh.jobservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateSkillRequest {
    String name;

    String description;

    String address;

    String url;

    String logo;

    String coverImage;

    long totalEmployee;
}