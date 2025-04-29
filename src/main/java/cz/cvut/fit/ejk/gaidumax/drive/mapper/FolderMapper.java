package cz.cvut.fit.ejk.gaidumax.drive.mapper;

import cz.cvut.fit.ejk.gaidumax.drive.config.MapperConfiguration;
import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface FolderMapper {

    FolderDto toDto(Folder folder);

    Folder toEntity(FolderDto folderDto);
}
