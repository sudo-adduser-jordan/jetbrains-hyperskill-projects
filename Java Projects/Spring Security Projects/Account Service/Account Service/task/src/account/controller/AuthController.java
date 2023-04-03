package account.controller;

import account.dto.user.UpdatePasswordUserDto;
import account.dto.user.CreateUserDto;
import account.dto.user.GetUserDto;
import account.model.user.User;
import account.service.AuthService;
import account.util.ResponseStatus;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("signup")
    public GetUserDto register(@RequestBody @Valid CreateUserDto createUserDto) {
        return authService.register(createUserDto);
    }

    @PostMapping("changepass")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UpdatePasswordUserDto updatePasswordUserDto,
                                            @AuthenticationPrincipal User user) {
        User changedUser = authService.changePassword(user, updatePasswordUserDto.getNewPassword());
        return ResponseStatus.builder()
                .add("email", changedUser.getEmail())
                .add("status", "The password has been updated successfully")
                .build();
    }

    @GetMapping("login")
    public GetUserDto login(@AuthenticationPrincipal User user) {
        return authService.getCurrentUser(user);
    }
}
