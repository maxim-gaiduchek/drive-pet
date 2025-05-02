package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.BaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UpdateFileDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.FileExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FileMapper;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FileRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FileService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

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

    @Override
    public Optional<File> findById(Long id) {
        return fileRepository.findById(id);
    }

    @Override
    public File getByIdOrThrow(Long id) {
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
        var author = userService.getByIdOrThrow(1L); // TODO fetch from security context
        file.setAuthor(author);
    }

    private void enrichWithParentFolder(File file, BaseInfoDto parentFolderDto) {
        var parentFolder = fetchFolder(parentFolderDto);
        file.setParentFolder(parentFolder);
    }

    private Folder fetchFolder(BaseInfoDto folderDto) {
        return Optional.ofNullable(folderDto)
                .map(BaseInfoDto::getId)
                .map(folderService::getByIdOrThrow)
                .orElse(null);
    }

    @Override
    public File update(Long id, UpdateFileDto fileDto) {
        var file = getByIdOrThrow(id);
        file.setFileName(fileDto.getFileName());
        enrichWithParentFolder(file, fileDto.getParentFolder());
        return fileRepository.save(file);
    }

    @Override
    public void delete(Long id) {
        var file = getByIdOrThrow(id);
        fileRepository.delete(file);
    }
}
