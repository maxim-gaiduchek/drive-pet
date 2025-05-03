package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UpdateFileDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;

import java.util.Optional;
import java.util.UUID;

public interface FileService {

    Optional<File> findById(UUID id);

    File getByIdOrThrow(UUID id);

    File create(FileDto fileDto);

    File update(UUID id, UpdateFileDto fileDto);

    void delete(UUID id);
}
