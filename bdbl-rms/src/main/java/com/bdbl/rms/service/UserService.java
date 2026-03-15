package com.bdbl.rms.service;

import com.bdbl.rms.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO dto);

    UserDTO updateUser(Long id, UserDTO dto);

    UserDTO getUser(Long id);

    UserDTO getUserByUsername(String username);

    List<UserDTO> getAllUsers();

    void changePassword(Long id, String oldPassword, String newPassword);

    void updateLastLogin(Long id);
}
