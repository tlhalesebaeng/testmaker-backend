package com.testmaker.api.repository;

import com.testmaker.api.entity.Test;
import com.testmaker.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> getAllByUser(User user);
}
