package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FileDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
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

    @Override
    public File create(FileDto fileDto) {
        var file = fileMapper.toEntity(fileDto);
        enrichWithEntities(file, fileDto);
        return fileRepository.save(file);
    }

    private void enrichWithEntities(File file, FileDto fileDto) {
        var author = userService.getByIdOrThrow(1L);
        var parentFolder = Optional.ofNullable(fileDto.getParentFolder().getId())
                .map(folderService::getByIdOrThrow)
                .orElse(null);
        file.setAuthor(author);
        file.setParentFolder(parentFolder);
    }

    @Override
    public File update(Long id, FileDto fileDto) {
        var file = getByIdOrThrow(id);
        file.setFileName(fileDto.getFileName());
        return fileRepository.save(file);
    }

    @Override
    public void delete(Long id) {
        var file = getByIdOrThrow(id);
        fileRepository.delete(file);
    }
}
