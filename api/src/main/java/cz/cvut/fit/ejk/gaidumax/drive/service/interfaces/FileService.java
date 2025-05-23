package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileForm;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UpdateFileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFileAccess;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileService {

    Optional<File> findById(UUID id);

    File getByIdOrThrow(UUID id);

    File create(FileForm fileForm);

    File update(UUID id, UpdateFileDto fileDto);

    void delete(UUID id);

    List<UserFileAccess> getAllAccessesByFileId(UUID id);

    UserFileAccess createAccess(UUID fileId, Long userId, UserAccessDto userAccessDto);

    File createAccessToken(UUID id);

    UserFileAccess updateAccess(UUID fileId, Long userId, UserAccessDto userAccessDto);

    File addAccessByAccessToken(String accessToken);

    void deleteAccess(UUID fileId, Long userId);
}
