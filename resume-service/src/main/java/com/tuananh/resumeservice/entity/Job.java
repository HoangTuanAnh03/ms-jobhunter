package com.tuananh.resumeservice.entity;

import com.tuananh.resumeservice.util.constant.LevelEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Job {
    long id;

    String name;

    String location;

    long salary;

    int quantity;

    @Enumerated(EnumType.STRING)
    LevelEnum level;

    String description;

    LocalDate startDate;

    LocalDate endDate;

    boolean active;

    long companyId;

    List<Skill> skills;

    LocalDateTime createdAt;

    String createdBy;

    LocalDateTime updatedAt;

    String updatedBy;
}
