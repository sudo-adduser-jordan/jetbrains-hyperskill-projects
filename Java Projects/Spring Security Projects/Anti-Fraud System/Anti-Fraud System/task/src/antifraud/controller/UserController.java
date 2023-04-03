package antifraud.controller;



import antifraud.model.User;
import antifraud.response.DeletedUserResponse;
import antifraud.request.UserRequest;
import antifraud.response.UserResponse;
import antifraud.service.UserService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/user")
    public ResponseEntity<UserResponse> addUser(@RequestBody @Valid UserRequest userRequest) {

        Optional<User> user = userService.addNewUser(userRequest);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserResponse userResponse = new UserResponse(user.get().getId(), user.get().getName(), user.get().getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userResponse);

    }

    @GetMapping("/api/auth/list")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsersAscending());
    }

    @DeleteMapping("/api/auth/user/{username}")
    public ResponseEntity<DeletedUserResponse> deleteUser(@PathVariable("username") String username) {
        boolean isDeleted = userService.deleteUser(username);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new DeletedUserResponse(username, "Deleted successfully!"));
    }
}