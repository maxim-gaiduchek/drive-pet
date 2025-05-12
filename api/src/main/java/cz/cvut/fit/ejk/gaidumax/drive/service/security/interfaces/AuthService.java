package cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;

import java.util.UUID;

public interface AuthService {

    void checkUserHasReadAccessToFile(UUID fileId);

    void checkUserHasWriteAccessToFile(UUID fileId);

    void checkUserIsOwnerOfFile(UUID fileId);

    void checkUserIsOwnerOfFile(File file);

    void checkUserHasReadAccessToFolder(UUID folderId);

    void checkUserHasWriteAccessToFolder(UUID folderId);

    void checkUserIsOwnerOfFolder(UUID folderId);

    void checkUserIsOwnerOfFolder(Folder folder);

    void checkUserHasAccessToUpdateUser(Long userId);
}
