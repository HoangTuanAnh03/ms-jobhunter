package com.tuananh.resumeservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tuananh.resumeservice.entity.Skill;
import com.tuananh.resumeservice.util.constant.LevelEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobResponse {
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

    Company company;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    List<Skill> skills;
}