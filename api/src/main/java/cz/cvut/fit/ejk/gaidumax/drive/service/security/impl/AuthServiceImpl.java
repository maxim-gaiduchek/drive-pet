package cz.cvut.fit.ejk.gaidumax.drive.service.security.impl;

import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.exception.AccessException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.AccessExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.AuthService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import cz.cvut.fit.ejk.gaidumax.drive.utils.FileUtils;
import cz.cvut.fit.ejk.gaidumax.drive.utils.FolderUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    private static final Set<UserAccessType> READ_ROLES = Set.of(UserAccessType.OWNER, UserAccessType.READ,
            UserAccessType.READ_WRITE);
    private static final Set<UserAccessType> WRITE_ROLES = Set.of(UserAccessType.OWNER, UserAccessType.READ_WRITE);

    @Inject
    SecurityContextProvider securityContextProvider;
    @Inject
    FileService fileService;
    @Inject
    FolderService folderService;

    @Override
    public void checkUserHasReadAccessToFile(UUID fileId) {
        var file = fileService.getByIdOrThrow(fileId);
        var userId = securityContextProvider.getUserId();
        var access = FileUtils.fetchAccess(file, userId);
        check(() -> access != null && READ_ROLES.contains(access.getAccessType()));
    }

    @Override
    public void checkUserHasWriteAccessToFile(UUID fileId) {
        var file = fileService.getByIdOrThrow(fileId);
        var userId = securityContextProvider.getUserId();
        var access = FileUtils.fetchAccess(file, userId);
        check(() -> access != null && WRITE_ROLES.contains(access.getAccessType()));
    }

    @Override
    public boolean isUserOwnerOfFile(UUID fileId) {
        var file = fileService.getByIdOrThrow(fileId);
        return isUserOwnerOfFile(file);
    }

    @Override
    public boolean isUserOwnerOfFile(File file) {
        var userId = securityContextProvider.getUserId();
        var access = FileUtils.fetchAccess(file, userId);
        return access != null && UserAccessType.OWNER.equals(access.getAccessType());
    }

    @Override
    public void checkUserIsOwnerOfFile(UUID fileId) {
        check(() -> isUserOwnerOfFile(fileId));
    }

    @Override
    public void checkUserIsOwnerOfFile(File file) {
        check(() -> isUserOwnerOfFile(file));
    }

    @Override
    public void checkUserHasReadAccessToFolder(UUID folderId) {
        var folder = folderService.getByIdOrThrow(folderId);
        var userId = securityContextProvider.getUserId();
        var access = FolderUtils.fetchAccess(folder, userId);
        check(() -> access != null && READ_ROLES.contains(access.getAccessType()));
    }

    @Override
    public void checkUserHasWriteAccessToFolder(UUID folderId) {
        var folder = folderService.getByIdOrThrow(folderId);
        var userId = securityContextProvider.getUserId();
        var access = FolderUtils.fetchAccess(folder, userId);
        check(() -> access != null && WRITE_ROLES.contains(access.getAccessType()));
    }

    @Override
    public boolean isUserOwnerOfFolder(UUID folderId) {
        var folder = folderService.getByIdOrThrow(folderId);
        return isUserOwnerOfFolder(folder);
    }

    @Override
    public boolean isUserOwnerOfFolder(Folder folder) {
        var userId = securityContextProvider.getUserId();
        var access = FolderUtils.fetchAccess(folder, userId);
        return access != null && UserAccessType.OWNER.equals(access.getAccessType());
    }

    @Override
    public void checkUserIsOwnerOfFolder(UUID folderId) {
        check(() -> isUserOwnerOfFolder(folderId));
    }

    @Override
    public void checkUserIsOwnerOfFolder(Folder folder) {
        check(() -> isUserOwnerOfFolder(folder));
    }

    @Override
    public void checkUserHasAccessToUpdateUser(Long userId) {
        var authUserId = securityContextProvider.getUserId();
        check(() -> Objects.equals(authUserId, userId));
    }

    private void check(Supplier<Boolean> predicate) {
        if (!isAdmin() && !predicate.get()) {
            throw new AccessException(AccessExceptionCode.ACCESS_DENIED);
        }
    }

    private boolean isAdmin() {
        return Role.ROLE_ADMIN.equals(securityContextProvider.getUserRole());
    }
}
