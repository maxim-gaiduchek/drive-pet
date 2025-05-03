package cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces;

import java.util.UUID;

public interface AuthService {

    void checkUserHasAccessToFile(UUID fileId);

    void checkUserHasAccessToFolder(UUID folderId);

    void checkUserHasAccessToUpdateUser(Long userId);
}
