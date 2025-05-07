package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileForm;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UpdateFileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UuidBaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.FileException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.FileExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FileRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileStorage;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class FileServiceImpl implements FileService {

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
    public File create(FileForm fileForm) {
        try {
            var userId = securityContextProvider.getUserId();
            var fileUpload = fileForm.getFile();
            var fileDto = fileForm.getFileDto();
            var fileName = fileUpload.fileName();
            var filePath = buildFilePath(fileDto.getParentFolder(), fileName);
            var fis = new FileInputStream(fileUpload.uploadedFile().toFile());
            var storageFilePath = storage.upload(fis, userId, filePath);
            var file = File.builder()
                    .fileName(fileName)
                    .fileType(fileUpload.contentType())
                    .s3FilePath(storageFilePath)
                    .size(fileUpload.size())
                    .build();
            enrichWithAuthor(file);
            enrichWithParentFolder(file, fileDto.getParentFolder());
            return fileRepository.save(file);
        } catch (Exception e) {
            log.error("File upload error", e);
            throw new FileException(FileExceptionCode.FILE_UPLOAD_ERROR);
        }
    }

    private String buildFilePath(UuidBaseInfoDto parentFolder, String fileName) {
        var millis = new Date().getTime();
        if (parentFolder == null) {
            return "/%d/%s".formatted(millis, fileName);
        }
        var folderPath = folderService.getAllParentFolders(parentFolder.getId()).stream()
                .map(Folder::getName)
                .collect(Collectors.joining("/"));
        return "/%s/%d/%s".formatted(folderPath, millis, fileName);
    }

    private void enrichWithAuthor(File file) {
        var userId = securityContextProvider.getUserId();
        var author = userService.getByIdOrThrow(userId);
        file.setAuthor(author);
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
        fileRepository.delete(file);
    }
}
