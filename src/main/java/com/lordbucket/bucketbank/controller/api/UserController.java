package com.lordbucket.bucketbank.controller.api;

import com.lordbucket.bucketbank.dto.UserDTO;
import com.lordbucket.bucketbank.dto.UserSummaryDTO;
import com.lordbucket.bucketbank.dto.requests.ChangePinRequest;
import com.lordbucket.bucketbank.dto.requests.ChangePrimaryAccountRequest;
import com.lordbucket.bucketbank.dto.requests.ChangeUsernameRequest;
import com.lordbucket.bucketbank.dto.requests.CreateUserRequest;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/u")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping("/summary/{id}")
    public ResponseEntity<UserSummaryDTO> getUserSummary(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserSummaryDTO(user));
    }

    @GetMapping(params = {"username"})
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping(value = "/summary", params = {"username"})
    public ResponseEntity<UserSummaryDTO> getUserSummaryByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new UserSummaryDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request.getUsername(), request.getPin());
        return ResponseEntity.ok(new UserDTO(user));
    }

    @PostMapping("/{id}/change-pin")
    public ResponseEntity<Void> changePin(@PathVariable int id, @RequestBody ChangePinRequest changePinRequest) {
        userService.changePIN(id, changePinRequest.getOldPin(), changePinRequest.getNewPin());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<UserDTO> changeUsername(@PathVariable int id, @RequestBody ChangeUsernameRequest changeUsernameRequest) {
        userService.changeUsername(id, changeUsernameRequest.getUsername());
        return ResponseEntity.ok(new UserDTO(userService.getUserById(id)));
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<UserDTO> suspend(@PathVariable int id) {
        userService.suspend(id);
        return ResponseEntity.ok(new UserDTO(userService.getUserById(id)));
    }

    @PostMapping("/{id}/reinstate")
    public ResponseEntity<UserDTO> reinstate(@PathVariable int id) {
        userService.reinstate(id);
        return ResponseEntity.ok(new UserDTO(userService.getUserById(id)));
    }

    @PostMapping("/{id}/primary-account")
    public ResponseEntity<UserDTO> changePrimaryAccount(@PathVariable int id, @RequestBody ChangePrimaryAccountRequest changePrimaryAccountRequest) {
        userService.changePrimaryAccount(id, changePrimaryAccountRequest.getAccountId());
        return ResponseEntity.ok(new UserDTO(userService.getUserById(id)));
    }
}
