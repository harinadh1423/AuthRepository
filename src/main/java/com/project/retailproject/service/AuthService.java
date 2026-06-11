package com.project.retailproject.service;

import com.project.retailproject.clients.AuditLogClient;
import com.project.retailproject.db.UserRepository;
import com.project.retailproject.dto.*;
import com.project.retailproject.exception.BadRequestException;
import com.project.retailproject.model.User;
import com.project.retailproject.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuditLogClient auditLogClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            sendAuditLog("AUTH.REGISTER_FAILED | Error: Email already registered: " + dto.getEmail(), null, null);
            throw new BadRequestException("Email already registered: " + dto.getEmail());
        }

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));   // plain text
        user.setRole(dto.getRole());
        user.setPhoneNumber(dto.getPhoneNumber());

        User saved = userRepository.save(user);

        sendAuditLog("AUTH.REGISTER_SUCCESS | UserID: " + saved.getUserId()
                        + " | Email: " + saved.getEmail() + " | Role: " + saved.getRole(),
                saved.getUserId(), saved.getUserName());

        return toResponse(saved);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        if (user == null) {
            sendAuditLog("AUTH.LOGIN_FAILED | Error: No account for email: " + dto.getEmail(), null, null);
            throw new BadRequestException("Invalid email or password");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            sendAuditLog(
                    "AUTH.LOGIN Wrong password for email: " + dto.getEmail(),
                    null,null);
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        sendAuditLog("AUTH.LOGIN_SUCCESS | Email: " + user.getEmail() + " | Role: " + user.getRole(),
                user.getUserId(), user.getUserName());

        LoginResponseDTO res = new LoginResponseDTO();
        res.setToken(token);
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setUserName(user.getUserName());
        return res;
    }

    private void sendAuditLog(String action, Long userId, String userName) {
        try {
            AuditLogRequestDTO log = new AuditLogRequestDTO();
            log.setAction(action);
            log.setUserId(userId);
            log.setUserName(userName);
            auditLogClient.createAuditLog(log);
            System.out.println("=== Audit log SENT: " + action);   // ← confirm it tried
        } catch (Exception e) {
            System.err.println("=== AUDIT FAILED: " + e.getMessage());   // ← see why
            e.printStackTrace();                                          // ← full trace
        }
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