package com.tuananh.jobservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tuananh.jobservice.util.constant.LevelEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "jobs")
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

    String location;

    long salary;

    int quantity;

    @Enumerated(EnumType.STRING)
    LevelEnum level;

    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    LocalDate startDate;

    LocalDate endDate;

    boolean active;

    long companyId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"jobs"})
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    List<Skill> skills;
}
