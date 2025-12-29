package com.testmaker.api.repository;

import com.testmaker.api.entity.Status;
import com.testmaker.api.utils.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByName(AccountStatus accountStatus);
    boolean existsByName(String name);
}
