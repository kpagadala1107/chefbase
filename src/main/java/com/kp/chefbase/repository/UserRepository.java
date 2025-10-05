package com.kp.chefbase.repository;

import com.kp.chefbase.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    List<User> findByRole(User.Role role);
    List<User> findByAlertsForCategoriesContaining(String category);
}