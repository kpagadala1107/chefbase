package com.kp.chefbase.service;

import com.kp.chefbase.model.User;
import com.kp.chefbase.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());
            user.setPhone(userDetails.getPhone());
            user.setAddress(userDetails.getAddress());
            user.setCity(userDetails.getCity());
            user.setState(userDetails.getState());
            user.setZipCode(userDetails.getZipCode());
            user.setCountry(userDetails.getCountry());
            user.setDateOfBirth(userDetails.getDateOfBirth());
            user.setProfilePictureUrl(userDetails.getProfilePictureUrl());
            user.setBio(userDetails.getBio());
            user.setAlertsForCategories(userDetails.getAlertsForCategories());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }
}