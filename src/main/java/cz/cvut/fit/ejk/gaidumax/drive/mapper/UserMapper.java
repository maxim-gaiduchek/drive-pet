package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.security.RegistrationDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(RegistrationDto registrationDto);
}
