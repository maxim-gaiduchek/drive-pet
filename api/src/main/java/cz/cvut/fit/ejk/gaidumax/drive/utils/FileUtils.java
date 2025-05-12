package cz.cvut.fit.ejk.gaidumax.drive.utils;

import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFileAccess;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class FileUtils {

    public User fetchOwner(File file) {
        return file.getAccesses().stream()
                .filter(access -> UserAccessType.OWNER.equals(access.getAccessType()))
                .findAny()
                .map(UserFileAccess::getUser)
                .orElse(null);
    }

    public UserFileAccess fetchAccess(File file, Long userId) {
        return file.getAccesses().stream()
                .filter(access -> Objects.equals(access.getUser().getId(), userId))
                .findAny()
                .orElse(null);
    }
}
