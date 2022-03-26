package com.kiran.league.maker.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kiran.league.maker.persist.dto.User;

public interface UserService extends UserDetailsService
{
    String ROLE_CLUB_ITC_MEMBER = "club_itc_member";

    String ROLE_GUEST_USER      = "guest_user";

    List<User> getUsers();

    List<User> getAdminPortalUsers();

    User getUserById(final Long userId);

    User getUserByUsername(final String username);

    User addUser(User user);

    User updateUserById(Long id, User user);

    User updateUserByUsername(String username, User user);

    void updateProfile(final User user);

    void deleteUserById(Long id);

    void deleteUserByUsername(String username);
    
    

}
