package com.tuananh.resumeservice.entity;

import com.tuananh.resumeservice.util.constant.ResumeStateEnum;
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
@Table(name = "resume")
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String email;

    String url;

    @Enumerated(EnumType.STRING)
    ResumeStateEnum status;

    String userId;

    long jobId;
}
