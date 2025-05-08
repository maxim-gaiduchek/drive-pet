package cz.cvut.fit.ejk.gaidumax.drive.service.security.impl;

import cz.cvut.fit.ejk.gaidumax.drive.exception.AccessException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.AccessExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.AuthService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    SecurityContextProvider securityContextProvider;
    @Inject
    FileService fileService;
    @Inject
    FolderService folderService;

    @Override
    public void checkUserHasAccessToFile(UUID fileId) {
        var file = fileService.getByIdOrThrow(fileId);
        var userId = securityContextProvider.getUserId();
        check(() -> Objects.equals(file.getAuthor().getId(), userId));
    }

    private void check(Supplier<Boolean> predicate) {
        if (!isAdmin() && !predicate.get()) {
            throw new AccessException(AccessExceptionCode.ACCESS_DENIED);
        }
    }

    private boolean isAdmin() {
        return Role.ROLE_ADMIN.equals(securityContextProvider.getUserRole());
    }

    @Override
    public void checkUserHasAccessToFolder(UUID folderId) {
        var folder = folderService.getByIdOrThrow(folderId);
        var userId = securityContextProvider.getUserId();
        check(() -> Objects.equals(folder.getAuthor().getId(), userId));
    }

    @Override
    public void checkUserHasAccessToUpdateUser(Long userId) {
        var authUserId = securityContextProvider.getUserId();
        check(() -> Objects.equals(authUserId, userId));
    }
}
