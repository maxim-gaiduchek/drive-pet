package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UuidBaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.ValidationException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.FolderExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FolderMapper;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FolderRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
        enrichWithAuthor(folder);
        enrichWithParentFolder(folder, folderDto);
        return folderRepository.save(folder);
    }

    private void enrichWithAuthor(Folder folder) {
        var userId = securityContextProvider.getUserId();
        var author = userService.getByIdOrThrow(userId);
        folder.setAuthor(author);
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

    @Override
    public void delete(UUID id) {
        var folder = getByIdOrThrow(id);
        folderRepository.delete(folder);
    }
}
