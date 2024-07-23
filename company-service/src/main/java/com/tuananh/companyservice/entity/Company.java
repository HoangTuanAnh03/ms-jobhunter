package com.tuananh.companyservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuananh.companyservice.util.constant.CompanyEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "company")
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    String address;

    String url;

    String logo;

    String coverImage;

    long totalEmployee;

    @Enumerated(EnumType.STRING)
    CompanyEnum status;

//    List<User> users;

//    List<Job> jobs;
}
