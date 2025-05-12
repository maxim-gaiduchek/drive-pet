package cz.cvut.fit.ejk.gaidumax.drive.utils;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFolderAccess;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class FolderUtils {

    public User fetchOwner(Folder folder) {
        return folder.getAccesses().stream()
                .filter(access -> UserAccessType.OWNER.equals(access.getAccessType()))
                .findAny()
                .map(UserFolderAccess::getUser)
                .orElse(null);
    }

    public UserFolderAccess fetchAccess(Folder folder, Long userId) {
        return folder.getAccesses().stream()
                .filter(access -> Objects.equals(access.getUser().getId(), userId))
                .findAny()
                .orElse(null);
    }
}
