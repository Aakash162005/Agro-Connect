package com.agro.userservice.services;


import com.agro.userservice.repository.UserRepository;
import com.agro.userservice.dto.LoginRequest;
import com.agro.userservice.dto.LoginResponse;
import com.agro.userservice.dto.RegisterRequest;
import com.agro.userservice.dto.UserResponse;
import com.agro.userservice.models.User;
import com.agro.userservice.services.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {



    private final PasswordEncoder passwordEncoder;

    private final UserRepository repository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }


    public UserResponse register(RegisterRequest request) {

        if(repository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("Email already exist");
        }

        User user = new User();

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);

        User savedUser = repository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    }

    public UserResponse getUserProfile(String userId) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    public LoginResponse login(LoginRequest request)
    {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("================================");
        System.out.println("Email : " + user.getEmail());
        System.out.println("Role  : " + user.getRole());
        System.out.println("================================");


        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }


        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );


        return new LoginResponse(token, "Login successful");

    }


    public UserResponse getUserByEmail(String email) {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setRole(user.getRole());

        return response;
    }

    public long getUserCount() {

        return repository.count();

    }
}
