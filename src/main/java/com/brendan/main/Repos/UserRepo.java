package com.brendan.main.Repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.brendan.main.Models.User;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
