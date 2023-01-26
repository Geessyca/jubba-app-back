package com.jubba.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.jubba.emails.ForgotPasswordWithToken;
import com.jubba.models.ERole;
import com.jubba.models.Role;
import com.jubba.models.User;
import com.jubba.payload.request.*;
import com.jubba.payload.response.MessageResponse;
import com.jubba.payload.response.UserInfoResponse;
import com.jubba.repository.RoleRepository;
import com.jubba.repository.UserRepository;
import com.jubba.security.jwt.JwtUtils;
import com.jubba.security.services.UserDetailsImpl;
import com.jubba.utils.CreateToken;
import com.jubba.utils.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 18000 )
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;
  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(), userDetails.getPhone()));
  }
  @GetMapping("/user-delete/{id}")
  public ResponseEntity<?> scheduleDelete(@PathVariable("id") String id){
    userRepository.deleteById(Long.valueOf((id)));
    return ResponseEntity.ok().body(new MessageResponse("Usuario removido"));
  }
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()),
                          "",
            signUpRequest.getPhone());

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @GetMapping("/user-account/{id}")
  public ResponseEntity<?> userAccount(@PathVariable("id") String id) {
    Long id_user = Long.valueOf(id);

    if (!userRepository.existsById(id_user)) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Id is already taken!"));
    }
    User userDetails = userRepository.getReferenceById(id_user);
    return ResponseEntity.ok().body(new UserInfoResponse(
            userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
            userDetails.getPhone()));
  }

  @PostMapping("/user-edit/{id_user}")
  public ResponseEntity<?> userAccountEdit(@PathVariable("id_user") String id_user, @Valid @RequestBody UserEditRequest userEditRequest) {
    Long id = Long.valueOf(id_user);
    final boolean[] editUser = {false};
    userRepository.findById(id).ifPresent(user1 -> {
        user1.setUsername(userEditRequest.getUsername());
        user1.setEmail(userEditRequest.getEmail());
        user1.setPhone(userEditRequest.getPhone());

        userRepository.save(user1);
      editUser[0] = true;
    });

    if(editUser[0]){
      return ResponseEntity.ok().body(new MessageResponse("Change password with sucess"));}

    return  ResponseEntity.badRequest().body(new MessageResponse("Algo deu errado"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
  @PostMapping("/forgotpassword")
  public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordEmailRequest userEmailRequest) {
    String email = userEmailRequest.getEmail();
    User user = userRepository.findByEmail(email);
    if (!userRepository.existsByEmail(email)) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email does not exist!"));
    }
    String token = new CreateToken().getAlphaNumericString(20)+"_id"+user.getId();
    user.setForgot(token);
    userRepository.save(user);
    boolean send = new SendEmail().message(email, "Recuperação de senha", new ForgotPasswordWithToken().messagem(user.getUsername(), token));
    if (send) {
      return ResponseEntity.ok().body(new MessageResponse("Recovery email sent"));
    }
    return ResponseEntity.badRequest().body(new MessageResponse("Error sending recovery email!"));
  }
  @PostMapping("/forgotpassword/{token}")
  public ResponseEntity<?> changePassword(@PathVariable("token") String token, @Valid @RequestBody ForgotPasswordRequest password) {
    Long id = Long.valueOf(token.split("_id")[1]);
    final boolean[] editPassword = {false};
            userRepository.findById(id).ifPresent(user1 -> {
                user1.setPassword(encoder.encode(password.getPassword()));
                if(user1.getForgot().equals(token)) {
                  user1.setForgot("");
                  userRepository.save(user1);
                  editPassword[0] = true;
                }
              else{
                editPassword[0] = false;
              }
            });

    if(editPassword[0]){
      return ResponseEntity.ok().body(new MessageResponse("Change password with sucess"));}

    return  ResponseEntity.badRequest().body(new MessageResponse("Token Invalido"));
  }

}
