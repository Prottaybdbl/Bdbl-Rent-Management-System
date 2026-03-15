package com.bdbl.rms.service;

import com.bdbl.rms.dto.UserDTO;
import com.bdbl.rms.entity.User;
import com.bdbl.rms.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
        }

        User user = User.builder()
                .username(dto.getUsername())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .role(dto.getRole())
                .status("ACTIVE")
                .build();

        return mapToUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());

        return mapToUserDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getUser(Long id) {
        return mapToUserDTO(userRepository.findById(id).orElseThrow());
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return mapToUserDTO(userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToUserDTO).collect(Collectors.toList());
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow();
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateLastLogin(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    private UserDTO mapToUserDTO(User entity) {
        UserDTO d = new UserDTO();
        d.setId(entity.getId());
        d.setUsername(entity.getUsername());
        d.setFullName(entity.getFullName());
        d.setEmail(entity.getEmail());
        d.setPhone(entity.getPhone());
        d.setRole(entity.getRole());
        d.setStatus(entity.getStatus());
        d.setLastLogin(entity.getLastLogin());
        d.setCreatedAt(entity.getCreatedAt());
        return d;
    }
}
