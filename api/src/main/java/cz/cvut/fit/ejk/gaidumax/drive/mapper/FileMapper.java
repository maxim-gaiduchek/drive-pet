package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface FileMapper {

    @Mapping(target = "filePath", source = "s3FilePath")
    FileDto toDto(File file);

    File toEntity(FileDto fileDto);
}
