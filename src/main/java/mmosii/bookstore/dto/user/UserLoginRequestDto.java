package mmosii.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 100) String password) {
}
