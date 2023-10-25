package mmosii.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.user.UserLoginRequestDto;
import mmosii.bookstore.dto.user.UserLoginResponseDto;
import mmosii.bookstore.dto.user.UserRegistrationRequestDto;
import mmosii.bookstore.dto.user.UserResponseDto;
import mmosii.bookstore.exception.RegistrationException;
import mmosii.bookstore.security.AuthenticationService;
import mmosii.bookstore.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authorisation/Registration",
        description = "Endpoints for registration and authentication for users")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(summary = "Authentication",
            description = "Authenticate user by email and password, "
                    + "returning JWT token if credentials are valid")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @Operation(summary = "Registration of new user", description = "Register new user by email,"
            + " password, repeatPassword, first name, last name and shipping address")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
