package com.phong.testsec.service;

import com.phong.testsec.dto.request.CreateUserRequest;
import com.phong.testsec.dto.response.UserResponse;
import com.phong.testsec.entity.User;
import com.phong.testsec.exception.DuplicateEmailException;
import com.phong.testsec.exception.UserNotFoundException;
import com.phong.testsec.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public UserResponse CreateUser(CreateUserRequest createUserRequest)
    {
        if(userRepository.existsByEmail(createUserRequest.getEmail()))
        {
            throw(new DuplicateEmailException("Email đã tồn tại"));
        }

        User user = new User();
        user.setName(createUserRequest.getName());
        user.setEmail(createUserRequest.getEmail());

        User savedUser = userRepository.save(user);
        System.out.println("Đã tạo thành công user mới với id: " + savedUser.getId());

        return toUserResponse(savedUser);
    }

    public UserResponse getUserById(long id)
    {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Không tìm thấy user với id " + id)
        );

        return toUserResponse(user);
    }

    public UserResponse toUserResponse(User user)
    {
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setId(user.getId());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setId(user.getId());
        return userResponse;
    }

    public Page<UserResponse> getAllUsers(Pageable pageable)
    {
        return userRepository.findAll(pageable).map(this::toUserResponse);
    }
}
