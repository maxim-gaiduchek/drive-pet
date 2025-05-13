package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileForm;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UpdateFileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UserAccessDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UuidBaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.BaseEntity;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFileAccess;
import cz.cvut.fit.ejk.gaidumax.drive.exception.AbstractException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.AccessException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.FileException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.FileExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FileRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileStorage;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import cz.cvut.fit.ejk.gaidumax.drive.utils.FileUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class FileServiceImpl implements FileService {

    private static final String FILE_PATH_TEMPLATE = "%s/%d/%s";

    @Inject
    FileRepository fileRepository;
    @Inject
    UserService userService;
    @Inject
    FolderService folderService;
    @Inject
    SecurityContextProvider securityContextProvider;
    @Inject
    FileStorage storage;

    @Override

    public Optional<File> findById(UUID id) {
        return fileRepository.findById(id);
    }

    @Override
    public File getByIdOrThrow(UUID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(FileExceptionCode.FILE_DOES_NOT_EXIST, id));
    }

    @Override
    @Transactional
    public File create(FileForm fileForm) {
        try {
            var userId = securityContextProvider.getUserId();
            var fileUpload = fileForm.getFile();
            var fileDto = fileForm.getFileDto();
            var fileName = fileUpload.fileName();
            var file = File.builder()
                    .fileName(fileName)
                    .fileType(fileUpload.contentType())
                    .size(fileUpload.size())
                    .build();
            var savedFile = fileRepository.save(file);
            var filePath = buildFilePath(savedFile.getId(), fileName);
            var fis = new FileInputStream(fileUpload.uploadedFile().toFile());
            var storageFilePath = storage.upload(fis, userId, filePath);
            savedFile.setS3FilePath(storageFilePath);
            enrichWithOwner(savedFile);
            enrichWithParentFolder(savedFile, fileDto.getParentFolder());
            return fileRepository.save(savedFile);
        } catch (AbstractException e) {
            throw e;
        } catch (Exception e) {
            log.error("File upload error", e);
            throw new FileException(FileExceptionCode.FILE_UPLOAD_ERROR);
        }
    }

    private String buildFilePath(UUID fileId, String fileName) {
        var millis = new Date().getTime();
        return FILE_PATH_TEMPLATE.formatted(fileId, millis, fileName);
    }

    private void enrichWithOwner(File file) {
        var userId = securityContextProvider.getUserId();
        var owner = userService.getByIdOrThrow(userId);
        var access = buildUserAccess(file, owner, UserAccessType.OWNER);
        file.getAccesses().add(access);
    }

    private UserFileAccess buildUserAccess(File file, User user, UserAccessType accessType) {
        return UserFileAccess.builder()
                .user(user)
                .file(file)
                .accessType(accessType)
                .build();
    }

    private void enrichWithParentFolder(File file, UuidBaseInfoDto parentFolderDto) {
        var parentFolder = fetchFolder(parentFolderDto);
        file.setParentFolder(parentFolder);
    }

    private Folder fetchFolder(UuidBaseInfoDto folderDto) {
        return Optional.ofNullable(folderDto)
                .map(UuidBaseInfoDto::getId)
                .map(folderService::getByIdOrThrow)
                .orElse(null);
    }

    @Override
    public File update(UUID id, UpdateFileDto fileDto) {
        var file = getByIdOrThrow(id);
        file.setFileName(fileDto.getFileName());
        enrichWithParentFolder(file, fileDto.getParentFolder());
        return fileRepository.save(file);
    }

    @Override
    public void delete(UUID id) {
        var file = getByIdOrThrow(id);
        storage.delete(file.getS3FilePath());
        fileRepository.delete(file);
    }

    @Override
    public List<UserFileAccess> getAllAccessesByFileId(UUID id) {
        var file = getByIdOrThrow(id);
        var accesses = file.getAccesses();
        accesses.sort(Comparator.comparing(UserFileAccess::getAccessType)
                .thenComparing(BaseEntity::getCreatedAt));
        return accesses;
    }

    // TODO create folder accesses
    @Override
    public UserFileAccess createAccess(UUID fileId, Long userId, UserAccessDto userAccessDto) {
        var file = getByIdOrThrow(fileId);
        var existedAccess = FileUtils.fetchAccess(file, userId);
        if (existedAccess != null) {
            return existedAccess;
        }
        var user = userService.getByIdOrThrow(userId);
        var access = buildUserAccess(file, user, userAccessDto.getAccessType());
        file.getAccesses().add(access);
        var savedFile = fileRepository.save(file);
        return FileUtils.fetchAccess(savedFile, user.getId());
    }

    @Override
    public UserFileAccess updateAccess(UUID fileId, Long userId, UserAccessDto userAccessDto) {
        var file = getByIdOrThrow(fileId);
        var access = FileUtils.fetchAccess(file, userId);
        checkAccessUpdatePossibility(access, file, userId, userAccessDto);
        access.setAccessType(userAccessDto.getAccessType());
        var savedFile = fileRepository.save(file);
        return FileUtils.fetchAccess(savedFile, userId);
    }

    private void checkAccessUpdatePossibility(UserFileAccess access, File file, Long userId,
                                              UserAccessDto userAccessDto) {
        if (access == null) {
            throw new AccessException(FileExceptionCode.USER_HAS_NO_ACCESS_TO_FILE, userId, file.getId());
        }
        if (UserAccessType.OWNER.equals(userAccessDto.getAccessType())) {
            throw new AccessException(FileExceptionCode.ONLY_ONE_USER_CAN_BE_OWNER);
        }
    }

    // TODO delete folder accesses
    @Override
    public void deleteAccess(UUID fileId, Long userId) {
        var file = getByIdOrThrow(fileId);
        file.getAccesses()
                .removeIf(access -> Objects.equals(access.getUser().getId(), userId));
        fileRepository.save(file);
    }
}
