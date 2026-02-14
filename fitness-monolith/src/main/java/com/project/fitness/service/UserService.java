package com.project.fitness.service;

import com.project.fitness.DTO.LoginRequest;
import com.project.fitness.DTO.LoginResponse;
import com.project.fitness.DTO.RegisterRequest;
import com.project.fitness.DTO.UserResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Users;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
//    private final Users users;

    public UserResponse getUserById(String id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToResponse(users);
    }

    public UserResponse register(RegisterRequest request) {
         Users user = Users.builder()
                 .email(request.getEmail())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .firstName(request.getFirstName())
                 .lastName(request.getLastName())
                 .build();

//           ^^  use builder instand of constructor made easy to handle multiple objects
//                 new Users(
//                 null,
//                 request.getEmail(),
//                 request.getPassword(),
//                 request.getFirstName(),
//                 request.getLastName(),
//                 Instant.parse("2026-01-04T14:49:23.930Z").atZone(ZoneOffset.UTC).toLocalDateTime(),
//                 Instant.parse("2026-01-04T14:49:23.930Z").atZone(ZoneOffset.UTC).toLocalDateTime(),
//                 List.of(),
//                 List.of()
//         );

         Users savedUser = userRepository.save(user);
         return mapToResponse(savedUser);
    }
         private UserResponse mapToResponse(Users savedUser){
            UserResponse response = new UserResponse();
            response.setId(savedUser.getId());
            response.setEmail(savedUser.getEmail());
//            response.setPassword(savedUser.getPassword());
            response.setFirstName(savedUser.getFirstName());
            response.setLastName(savedUser.getLastName());
            response.setCreatedAt(savedUser.getCreatedAt());
            response.setUpdatedAt(savedUser.getUpdatedAt());
            return response;
         }

    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // 2️⃣ get authenticated user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3️⃣ generate JWT
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token);

    }
}
