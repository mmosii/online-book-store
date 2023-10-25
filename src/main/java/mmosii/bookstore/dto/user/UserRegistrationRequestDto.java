package mmosii.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mmosii.bookstore.security.constraint.FieldMatch;

@FieldMatch(message = "Passwords are not equal")
public record UserRegistrationRequestDto(
        @NotBlank @Email String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String shippingAddress,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Size(min = 6, max = 100) String repeatPassword) {
}
