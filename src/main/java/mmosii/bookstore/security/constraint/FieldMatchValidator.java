package mmosii.bookstore.security.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mmosii.bookstore.dto.user.UserRegistrationRequestDto;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext context) {
        UserRegistrationRequestDto user = (UserRegistrationRequestDto) candidate;
        return user.password().equals(user.repeatPassword());
    }
}
