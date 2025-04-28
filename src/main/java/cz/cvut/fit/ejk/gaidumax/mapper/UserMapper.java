package cz.cvut.fit.ejk.gaidumax.mapper;

import cz.cvut.fit.ejk.gaidumax.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.dto.UserDto;
import cz.cvut.fit.ejk.gaidumax.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface UserMapper {

    UserDto toDto(User user);
}
