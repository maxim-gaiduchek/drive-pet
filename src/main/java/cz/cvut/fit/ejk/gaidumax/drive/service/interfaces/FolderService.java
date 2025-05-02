package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;

import java.util.List;
import java.util.Optional;

public interface FolderService {

    Optional<Folder> findById(Long id);

    Folder getByIdOrThrow(Long id);

    List<Folder> getAllParentFolders(Long folderId);
    
    Folder create(FolderDto folderDto);

    Folder update(Long id, FolderDto folderDto);

    void delete(Long id);
}
