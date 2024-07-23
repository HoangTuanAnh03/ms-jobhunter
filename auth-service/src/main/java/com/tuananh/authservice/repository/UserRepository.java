package com.tuananh.authservice.repository;

import com.tuananh.authservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.tuananh.authservice.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByCompanyIdAndRole(long companyId, Role role);

//    Optional<User> findById(int email);
//
    boolean existsByEmail(String email);


//    List<User> findByCompany(Company company);
}
