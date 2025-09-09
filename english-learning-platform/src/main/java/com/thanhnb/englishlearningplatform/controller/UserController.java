package com.thanhnb.englishlearningplatform.controller;

import com.thanhnb.englishlearningplatform.dto.request.ChangePasswordRequest;
import com.thanhnb.englishlearningplatform.dto.request.RegisterRequest;
import com.thanhnb.englishlearningplatform.dto.request.UpdateUserRequest;
import com.thanhnb.englishlearningplatform.dto.response.ApiResponse;
import com.thanhnb.englishlearningplatform.dto.response.UserResponse;
import com.thanhnb.englishlearningplatform.entity.User;
import com.thanhnb.englishlearningplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * Lấy tất cả users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(userResponses));
    }
    
    /**
     * Lấy user theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        return ResponseEntity.ok(ApiResponse.success(UserResponse.fromEntity(user)));
    }
    
    /**
     * Tạo user mới
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .build();
        
        User savedUser = userService.createUser(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo người dùng thành công", UserResponse.fromEntity(savedUser)));
    }
    
    /**
     * Cập nhật user
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateUserRequest request) {
        
        User existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        if (request.getUsername() != null) {
            existingUser.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getFullName() != null) {
            existingUser.setFullName(request.getFullName());
        }
        
        User updatedUser = userService.updateUser(id, existingUser);
        
        return ResponseEntity.ok(ApiResponse.success("Cập nhật người dùng thành công", UserResponse.fromEntity(updatedUser)));
    }
    
    /**
     * Đổi mật khẩu
     */
    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        // Kiểm tra mật khẩu cũ
        if (!userService.checkPassword(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }
        
        userService.changePassword(id, request.getNewPassword());
        
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công", null));
    }
    
    /**
     * Kích hoạt/vô hiệu hóa user
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<UserResponse>> toggleUserStatus(@PathVariable Long id) {
        User user = userService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái thành công", UserResponse.fromEntity(user)));
    }
    
    /**
     * Xóa user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa người dùng thành công", null));
    }
    
    /**
     * Kiểm tra username có tồn tại không
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
    
    /**
     * Kiểm tra email có tồn tại không
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
