package com.thanhnb.englishlearningplatform.service;

import com.thanhnb.englishlearningplatform.entity.User;
import com.thanhnb.englishlearningplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Tạo user mới
     */
    public User createUser(User user) {
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Tên tài khoản đã tồn tại");
        }
        
        // Kiểm tra email đã tồn tại  
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        
        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default values
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        
        User savedUser = userRepository.save(user);
        log.info("Created new user: {}", savedUser.getUsername());
        
        return savedUser;
    }
    
    /**
     * Tìm user theo ID
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Tìm user theo username
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Tìm user theo email
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Lấy tất cả users
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    /**
     * Lấy users đang hoạt động
     */
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    /**
     * Cập nhật user
     */
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        // Update fields (không update password ở đây)
        if (updatedUser.getUsername() != null && 
            !updatedUser.getUsername().equals(existingUser.getUsername())) {
            
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Tên tài khoản đã tồn tại");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }
        
        if (updatedUser.getEmail() != null && 
            !updatedUser.getEmail().equals(existingUser.getEmail())) {
            
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email đã tồn tại");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }
        
        if (updatedUser.getFullName() != null) {
            existingUser.setFullName(updatedUser.getFullName());
        }
        
        User savedUser = userRepository.save(existingUser);
        log.info("Updated user: {}", savedUser.getUsername());
        
        return savedUser;
    }
    
    /**
     * Đổi mật khẩu
     */
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        log.info("Password changed for user: {}", user.getUsername());
    }
    
    /**
     * Kiểm tra mật khẩu
     */
    @Transactional(readOnly = true)
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * Kích hoạt/vô hiệu hóa user
     */
    public User toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        user.setIsActive(!user.getIsActive());
        User savedUser = userRepository.save(user);
        
        log.info("Toggled status for user {}: {}", user.getUsername(), user.getIsActive());
        return savedUser;
    }
    
    /**
     * Xóa user (soft delete - chỉ deactivate)
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        user.setIsActive(false);
        userRepository.save(user);
        
        log.info("Soft deleted user: {}", user.getUsername());
    }
    
    /**
     * Kiểm tra username có tồn tại không
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Kiểm tra email có tồn tại không
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Tìm kiếm users
     */
    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findActiveUsers();
        }
        return userRepository.searchByNameOrUsername(keyword.trim());
    }
    
    /**
     * Đếm tổng số users đang hoạt động
     */
    @Transactional(readOnly = true)
    public Long countActiveUsers() {
        return userRepository.countActiveUsers();
    }
}