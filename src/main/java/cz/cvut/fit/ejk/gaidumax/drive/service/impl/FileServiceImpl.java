package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UpdateFileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UuidBaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.FileExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FileMapper;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FileRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FileServiceImpl implements FileService {

    @Inject
    FileRepository fileRepository;
    @Inject
    FileMapper fileMapper;
    @Inject
    UserService userService;
    @Inject
    FolderService folderService;
    @Inject
    SecurityContextProvider securityContextProvider;

    @Override
    public Optional<File> findById(UUID id) {
        return fileRepository.findById(id);
    }

    @Override
    public File getByIdOrThrow(UUID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(FileExceptionCode.FILE_DOES_NOT_EXIST, id));
    }

    // TODO change it when file system will be released
    @Override
    public File create(FileDto fileDto) {
        var file = fileMapper.toEntity(fileDto);
        file.setPath("image/png");
        enrichWithAuthor(file);
        enrichWithParentFolder(file, fileDto.getParentFolder());
        return fileRepository.save(file);
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
