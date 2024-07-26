package com.tuananh.jobservice.dto.request;

import com.tuananh.jobservice.util.constant.LevelEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateJobRequest {
    String name;

    String location;

    long salary;

    int quantity;

    @Enumerated(EnumType.STRING)
    LevelEnum level;

    String description;

    LocalDate startDate;

    LocalDate endDate;

    Set<Long> skills;
}