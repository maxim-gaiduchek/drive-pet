package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.code.FolderExceptionCode;
import cz.cvut.fit.ejk.gaidumax.drive.mapper.FolderMapper;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FolderRepository;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.FolderService;
import cz.cvut.fit.ejk.gaidumax.drive.service.interfaces.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class FolderServiceImpl implements FolderService {

    @Inject
    FolderRepository folderRepository;
    @Inject
    FolderMapper folderMapper;
    @Inject
    UserService userService;

    @Override
    public Optional<Folder> findById(Long id) {
        return folderRepository.findById(id);
    }

    @Override
    public Folder getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(FolderExceptionCode.FOLDER_DOES_NOT_EXIST, id));
    }

    @Override
    public Folder create(FolderDto folderDto) {
        var folder = folderMapper.toEntity(folderDto);
        enrichWithEntities(folder, folderDto);
        return folderRepository.save(folder);
    }

    private void enrichWithEntities(Folder folder, FolderDto folderDto) {
        var author = userService.getByIdOrThrow(1L);
        var parentFolder = getByIdOrThrow(folderDto.getParentFolder().getId());
        folder.setAuthor(author);
        folder.setParentFolder(parentFolder);
    }

    @Override
    public Folder update(Long id, FolderDto folderDto) {
        var folder = getByIdOrThrow(id);
        folder.setName(folderDto.getName());
        return folderRepository.save(folder);
    }

    @Override
    public void delete(Long id) {
        var folder = getByIdOrThrow(id);
        folderRepository.delete(folder);
    }
}
