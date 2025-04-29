package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;

import java.util.Optional;

public interface FileService {

    Optional<File> findById(Long id);

    File getByIdOrThrow(Long id);

    File create(FileDto fileDto);

    File update(Long id, FileDto fileDto);

    void delete(Long id);
}
