package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.AuthService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.ZonedDateTime;

@Mapper(config = MapperConfiguration.class)
public abstract class FileMapper {

    @ConfigProperty(name = "drive-pet.item.access.token.age")
    long accessTokenAge;

    @Inject
    AuthService authService;

    @Mapping(target = "filePath", source = "s3FilePath")
    public abstract FileDto toDto(File file);

    public abstract File toEntity(FileDto fileDto);

    @AfterMapping
    protected void setupAccessToken(@MappingTarget FileDto fileDto, File file) {
        if (file.getAccessTokenCreatedAt() == null
                || file.getAccessTokenCreatedAt().plusMinutes(accessTokenAge).isBefore(ZonedDateTime.now())
                || !authService.isUserOwnerOfFile(file)) {
            fileDto.setAccessToken(null);
        }
    }
}
