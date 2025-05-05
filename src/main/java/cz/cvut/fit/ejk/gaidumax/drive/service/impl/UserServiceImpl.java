package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.UserDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.JwtResponseDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.LoginDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.RegistrationDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.exception.AccessException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.ValidationException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.AccessExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.UserExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.UserMapper;
import cz.cvut.fit.ejk.gaidumax.drive.repository.UserRepository;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.JwtProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private static final String TOKEN_TYPE = "Bearer";

    @Inject
    UserRepository userRepository;
    @Inject
    UserMapper userMapper;
    @Inject
    JwtProvider jwtProvider;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UserExceptionCode.USER_DOES_NOT_EXIST, id));
    }

    @Override
    public User update(Long id, UserDto userDto) {
        var user = getByIdOrThrow(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        return userRepository.save(user);
    }

    @Override
    public JwtResponseDto login(LoginDto loginDto) {
        var user = userRepository.findByEmail(loginDto.getLogin())
                .orElseThrow(() -> new AccessException(AccessExceptionCode.INVALID_LOGIN_OR_PASSWORD));
        checkUserPassword(user.getPassword(), loginDto.getPassword());
        return buildJwtResponse(user);
    }

    private void checkUserPassword(String userPassword, String password) {
        if (!Objects.equals(userPassword, password)) {
            throw new AccessException(AccessExceptionCode.INVALID_LOGIN_OR_PASSWORD);
        }
    }

    private JwtResponseDto buildJwtResponse(User user) {
        var accessToken = jwtProvider.generateAccessToken(user);
        var refreshToken = jwtProvider.generateRefreshToken(user);
        return JwtResponseDto.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type(TOKEN_TYPE)
                .build();
    }

    @Override
    public JwtResponseDto register(RegistrationDto registrationDto) {
        checkUserEmailAvailability(registrationDto.getEmail());
        var user = userMapper.toEntity(registrationDto);
        user.setRole(Role.ROLE_USER);
        var savedUser = userRepository.save(user);
        return buildJwtResponse(savedUser);
    }

    private void checkUserEmailAvailability(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException(UserExceptionCode.EMAIL_IS_ALREADY_EXISTS, email);
        }
    }
}
