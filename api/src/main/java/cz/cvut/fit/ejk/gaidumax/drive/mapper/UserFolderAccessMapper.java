package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFolderAccess;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface UserFolderAccessMapper {

    UserAccessDto toDto(UserFolderAccess userFolderAccess);

    List<UserAccessDto> toDtos(List<UserFolderAccess> userFolderAccesses);
}
