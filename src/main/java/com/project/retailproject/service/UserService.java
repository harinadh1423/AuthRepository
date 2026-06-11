package com.project.retailproject.service;



import com.project.retailproject.db.UserRepository;
import com.project.retailproject.dto.UserResponseDTO;
import com.project.retailproject.exception.ResourceNotFoundException;
import com.project.retailproject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    public UserResponseDTO getUserByEmail(String email) {
        return toResponse(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email)));
    }

    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    private UserResponseDTO toResponse(User u) {
        UserResponseDTO r = new UserResponseDTO();
        r.setUserId(u.getUserId());
        r.setUserName(u.getUserName());
        r.setEmail(u.getEmail());
        r.setRole(u.getRole());
        r.setPhoneNumber(u.getPhoneNumber());
        return r;
    }
}
