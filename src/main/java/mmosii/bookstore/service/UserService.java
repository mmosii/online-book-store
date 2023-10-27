package mmosii.bookstore.service;

import mmosii.bookstore.dto.user.UserRegistrationRequestDto;
import mmosii.bookstore.dto.user.UserResponseDto;
import mmosii.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException;
}
