package com.licenta.domain.repository;

import com.licenta.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository extends JpaRepository<User, Long> {
}
