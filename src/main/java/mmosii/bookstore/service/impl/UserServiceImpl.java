package mmosii.bookstore.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import mmosii.bookstore.dto.user.UserRegistrationRequestDto;
import mmosii.bookstore.dto.user.UserResponseDto;
import mmosii.bookstore.exception.RegistrationException;
import mmosii.bookstore.mapper.UserMapper;
import mmosii.bookstore.model.RoleName;
import mmosii.bookstore.model.User;
import mmosii.bookstore.repository.user.RoleRepository;
import mmosii.bookstore.repository.user.UserRepository;
import mmosii.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RegistrationException("Unable to complete registration.");
        }
        User savedUser = new User();
        savedUser.setEmail(request.email());
        savedUser.setRoles(new HashSet<>(Arrays.asList(
                roleRepository.findAllByName(RoleName.ROLE_USER))));
        savedUser.setFirstName(request.firstName());
        savedUser.setLastName(request.lastName());
        savedUser.setShippingAddress(request.shippingAddress());
        savedUser.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(savedUser);
        return userMapper.toUserResponseDto(savedUser);
    }
}
