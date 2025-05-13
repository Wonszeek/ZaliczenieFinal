package com.example.demo.Restaurant.Repository;

import com.example.demo.Restaurant.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegisterRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

}
