package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFolderAccess;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderService {

    Optional<Folder> findById(UUID id);

    Folder getByIdOrThrow(UUID id);

    List<Folder> getAllParentFolders(UUID childFolderId);

    Folder create(FolderDto folderDto);

    Folder update(UUID id, FolderDto folderDto);

    void setupUserInternalFileReadAccessForParentFolders(UUID childFolderId, Long userId);

    void delete(UUID id);

    @Transactional(Transactional.TxType.MANDATORY)
    void removeUserInternalFileReadAccessForParentFolders(UUID childFolderId, Long userId);

    List<UserFolderAccess> getAllAccessesByFolderId(UUID id);

    UserFolderAccess createAccess(UUID folderId, Long userId, UserAccessDto userAccessDto);

    UserFolderAccess updateAccess(UUID folderId, Long userId, UserAccessDto userAccessDto);

    void deleteAccess(UUID folderId, Long userId);
}
