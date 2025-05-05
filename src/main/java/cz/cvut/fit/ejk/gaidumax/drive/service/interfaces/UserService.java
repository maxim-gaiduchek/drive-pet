package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.UserDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.JwtResponseDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.LoginDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.RegistrationDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    User getByIdOrThrow(Long id);

    User update(Long id, UserDto userDto);

    JwtResponseDto login(LoginDto loginDto);

    JwtResponseDto register(RegistrationDto registrationDto);
}
