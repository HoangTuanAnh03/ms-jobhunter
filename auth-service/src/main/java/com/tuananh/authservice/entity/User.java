package com.tuananh.authservice.entity;

import com.tuananh.authservice.util.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    String email;

    String password;

    LocalDate dob;

    @Enumerated(EnumType.STRING)
    GenderEnum gender;

    String address;

    Boolean active;

    @Column(name = "company_id")
    long companyId;

    @Column(name = "mobile_number")
    String mobileNumber;
//
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    @JsonIgnore
//    List<Resume> resumes;
//
    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
}
