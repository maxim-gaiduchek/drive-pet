package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderService {

    Optional<Folder> findById(UUID id);

    Folder getByIdOrThrow(UUID id);

    List<Folder> getAllParentFolders(UUID childFolderId);
    
    Folder create(FolderDto folderDto);

    Folder update(UUID id, FolderDto folderDto);

    void delete(UUID id);
}
