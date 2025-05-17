package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFileAccess;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface UserFileAccessMapper {

    UserAccessDto toDto(UserFileAccess userFileAccess);

    List<UserAccessDto> toDtos(List<UserFileAccess> userFileAccesses);
}
