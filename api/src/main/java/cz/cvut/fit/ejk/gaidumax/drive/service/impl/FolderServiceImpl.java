package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UuidBaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.BaseEntity;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFolderAccess;
import cz.cvut.fit.ejk.gaidumax.drive.exception.AccessException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.ValidationException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.FolderExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FolderMapper;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FolderRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import cz.cvut.fit.ejk.gaidumax.drive.utils.FolderUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FolderServiceImpl implements FolderService {

    @Inject
    FolderRepository folderRepository;
    @Inject
    FolderMapper folderMapper;
    @Inject
    UserService userService;
    @Inject
    SecurityContextProvider securityContextProvider;

    @Override
    public Optional<Folder> findById(UUID id) {
        return folderRepository.findById(id);
    }

    @Override
    public Folder getByIdOrThrow(UUID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(FolderExceptionCode.FOLDER_DOES_NOT_EXIST, id));
    }

    @Override
    public List<Folder> getAllParentFolders(UUID folderId) {
        var folder = getByIdOrThrow(folderId);
        return folderRepository.findAllParents(folder.getId());
    }

    @Override
    public Folder create(FolderDto folderDto) {
        var folder = folderMapper.toEntity(folderDto);
        enrichWithOwner(folder);
        enrichWithParentFolder(folder, folderDto);
        return folderRepository.save(folder);
    }

    private void enrichWithOwner(Folder folder) {
        var userId = securityContextProvider.getUserId();
        var owner = userService.getByIdOrThrow(userId);
        var access = buildUserAccess(folder, owner, UserAccessType.OWNER);
        folder.getAccesses().add(access);
    }

    private UserFolderAccess buildUserAccess(Folder folder, User user, UserAccessType accessType) {
        return UserFolderAccess.builder()
                .user(user)
                .folder(folder)
                .accessType(accessType)
                .build();
    }

    private void enrichWithParentFolder(Folder folder, FolderDto folderDto) {
        var parentFolder = fetchFolder(folderDto.getParentFolder());
        folder.setParentFolder(parentFolder);
    }

    private Folder fetchFolder(UuidBaseInfoDto folderDto) {
        return Optional.ofNullable(folderDto)
                .map(UuidBaseInfoDto::getId)
                .map(this::getByIdOrThrow)
                .orElse(null);
    }

    @Override
    public Folder update(UUID id, FolderDto folderDto) {
        checkUpdatePossibility(id, folderDto);
        var folder = getByIdOrThrow(id);
        folder.setName(folderDto.getName());
        enrichWithParentFolder(folder, folderDto);
        return folderRepository.save(folder);
    }

    private void checkUpdatePossibility(UUID id, FolderDto folderDto) {
        var parentFolderDto = folderDto.getParentFolder();
        if (parentFolderDto != null && Objects.equals(parentFolderDto.getId(), id)) {
            throw new ValidationException(FolderExceptionCode.FOLDER_AND_ITS_PARENT_FOLDER_MUST_NOT_BE_EQUALS, id);
        }
    }

    @Transactional(Transactional.TxType.MANDATORY)
    @Override
    public void setupUserInternalFileReadAccessForParentFolders(UUID childFolderId, Long userId) {
        folderRepository.setupUserInternalFileReadAccessForParentFolders(childFolderId, userId);
    }

    @Override
    public void delete(UUID id) {
        var folder = getByIdOrThrow(id);
        folderRepository.delete(folder);
    }

    @Transactional(Transactional.TxType.MANDATORY)
    @Override
    public void removeUserInternalFileReadAccessForParentFolders(UUID childFolderId, Long userId) {
        folderRepository.removeUserInternalFileReadAccessForParentFolders(childFolderId, userId);
    }

    @Override
    public List<UserFolderAccess> getAllAccessesByFolderId(UUID id) {
        var folder = getByIdOrThrow(id);
        var accesses = folder.getAccesses();
        accesses.sort(Comparator.comparing(UserFolderAccess::getAccessType)
                .thenComparing(BaseEntity::getCreatedAt));
        return accesses;
    }

    @Override
    public UserFolderAccess createAccess(UUID folderId, Long userId, UserAccessDto userAccessDto) {
        var file = getByIdOrThrow(folderId);
        var existedAccess = FolderUtils.fetchAccess(file, userId);
        if (existedAccess != null) {
            return existedAccess;
        }
        var user = userService.getByIdOrThrow(userId);
        var access = buildUserAccess(file, user, userAccessDto.getAccessType());
        file.getAccesses().add(access);
        var savedFolder = folderRepository.save(file);
        return FolderUtils.fetchAccess(savedFolder, user.getId());
    }

    @Override
    public UserFolderAccess updateAccess(UUID folderId, Long userId, UserAccessDto userAccessDto) {
        var file = getByIdOrThrow(folderId);
        var access = FolderUtils.fetchAccess(file, userId);
        checkAccessUpdatePossibility(access, file, userId, userAccessDto);
        access.setAccessType(userAccessDto.getAccessType());
        var savedFolder = folderRepository.save(file);
        return FolderUtils.fetchAccess(savedFolder, userId);
    }

    private void checkAccessUpdatePossibility(UserFolderAccess access, Folder folder, Long userId,
                                              UserAccessDto userAccessDto) {
        if (access == null) {
            throw new AccessException(FolderExceptionCode.USER_HAS_NO_ACCESS_TO_FOLDER, userId, folder.getId());
        }
        if (UserAccessType.OWNER.equals(userAccessDto.getAccessType())) {
            throw new AccessException(FolderExceptionCode.ONLY_ONE_USER_CAN_BE_OWNER);
        }
    }

    @Override
    public void deleteAccess(UUID folderId, Long userId) {
        var folder = getByIdOrThrow(folderId);
        folder.getAccesses()
                .removeIf(access -> Objects.equals(access.getUser().getId(), userId));
        folderRepository.save(folder);
    }
}
