package cz.cvut.fit.ejk.gaidumax.drive.service.impl;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UuidBaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFolderAccess;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UuidBaseEntity;
import cz.cvut.fit.ejk.gaidumax.drive.exception.EntityNotFoundException;
import cz.cvut.fit.ejk.gaidumax.drive.exception.ValidationException;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FolderRepository;
import cz.cvut.fit.ejk.gaidumax.drive.repository.UserRepository;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@QuarkusTest
class FolderServiceImplTest {

    @InjectMock
    SecurityContextProvider securityContextProvider;

    @Inject
    FolderRepository folderRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    EntityManager entityManager;
    @Inject
    FolderServiceImpl folderService;

    private Folder savedFolder;
    private Folder savedParentFolder;
    private Folder savedFolderWithParentFolder;
    private User savedUser;

    @BeforeEach
    @Transactional
    void setUp() {
        var user = User.builder()
                .firstName("Max")
                .lastName("Test")
                .email("test@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        savedUser = userRepository.save(user);

        var folder = Folder.builder()
                .name("Test folder 1")
                .build();
        var folderAccess = UserFolderAccess.builder()
                .user(savedUser)
                .folder(folder)
                .accessType(UserAccessType.OWNER)
                .build();
        folder.getAccesses().add(folderAccess);
        savedFolder = folderRepository.save(folder);

        var parentFolder = Folder.builder()
                .name("Test folder 2")
                .build();
        var parentFolderAccess = UserFolderAccess.builder()
                .user(savedUser)
                .folder(parentFolder)
                .accessType(UserAccessType.OWNER)
                .build();
        parentFolder.getAccesses().add(parentFolderAccess);
        savedParentFolder = folderRepository.save(parentFolder);

        var folderWithParentFolder = Folder.builder()
                .name("Test folder 3")
                .parentFolder(savedParentFolder)
                .build();
        var folderWithParentFolderAccess = UserFolderAccess.builder()
                .user(savedUser)
                .folder(folderWithParentFolder)
                .accessType(UserAccessType.OWNER)
                .build();
        folderWithParentFolder.getAccesses().add(folderWithParentFolderAccess);
        savedFolderWithParentFolder = folderRepository.save(folderWithParentFolder);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        entityManager.createQuery("""
                        delete from UserFolderAccess
                        """)
                .executeUpdate();
        entityManager.createQuery("""
                        delete from Folder
                        """)
                .executeUpdate();
    }

    @Test
    void testFindById_withPresentFolder() {
        var result = folderService.findById(savedFolder.getId());
        assertTrue(result.isPresent());
        assertEquals(savedFolder.getId(), result.get().getId());
        assertEquals(savedFolder.getName(), result.get().getName());
        assertNull(savedFolder.getParentFolder());
    }

    @Test
    void testFindById_withEmptyFolder() {
        var id = generateNewUuid();
        var result = folderService.findById(id);
        assertTrue(result.isEmpty());
    }

    private UUID generateNewUuid() {
        var ids = Set.of(savedFolder, savedParentFolder, savedFolderWithParentFolder).stream()
                .map(UuidBaseEntity::getId)
                .collect(Collectors.toSet());
        UUID id;
        do {
            id = UUID.randomUUID();
        } while (ids.contains(id));
        return id;
    }

    @Test
    void testGetByIdOrThrow_withPresentFolder() {
        var result = folderService.getByIdOrThrow(savedFolderWithParentFolder.getId());
        assertEquals(savedFolderWithParentFolder.getId(), result.getId());
        assertEquals(savedFolderWithParentFolder.getName(), result.getName());
        assertEquals(savedFolderWithParentFolder.getParentFolder().getId(), savedParentFolder.getId());
    }

    @Test
    void testGetByIdOrThrow_withEmptyFolder() {
        var id = generateNewUuid();
        assertThrows(EntityNotFoundException.class, () -> folderService.getByIdOrThrow(id));
    }

    @Test
    void testCreate_withoutParentFolder() {
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .build();

        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        var result = folderService.create(folderDto);
        assertNotNull(result.getId());
        assertEquals(folderDto.getName(), result.getName());
        assertNull(result.getParentFolder());
        assertEquals(1, CollectionUtils.size(result.getAccesses()));
        var resultAccess = result.getAccesses().getFirst();
        assertEquals(savedUser.getId(), resultAccess.getUser().getId());
        assertEquals(result.getId(), resultAccess.getFolder().getId());
        assertEquals(UserAccessType.OWNER, resultAccess.getAccessType());
    }

    @Test
    void testCreate_withParentFolder() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        var result = folderService.create(folderDto);
        assertNotNull(result.getId());
        assertEquals(folderDto.getName(), result.getName());
        assertEquals(savedFolder.getId(), result.getParentFolder().getId());
        assertEquals(1, CollectionUtils.size(result.getAccesses()));
        var resultAccess = result.getAccesses().getFirst();
        assertEquals(savedUser.getId(), resultAccess.getUser().getId());
        assertEquals(result.getId(), resultAccess.getFolder().getId());
        assertEquals(UserAccessType.OWNER, resultAccess.getAccessType());
    }

    @Test
    void testCreate_withNoUserFound() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        when(securityContextProvider.getUserId()).thenReturn(0L);

        assertThrows(EntityNotFoundException.class, () -> folderService.create(folderDto));
    }

    @Test
    void testCreate_withNoParentFolderFound() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(generateNewUuid())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        assertThrows(EntityNotFoundException.class, () -> folderService.create(folderDto));
    }

    @Test
    void testUpdate_withoutParentFolder() {
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .build();

        var result = folderService.update(savedFolder.getId(), folderDto);
        assertEquals(savedFolder.getId(), result.getId());
        assertEquals(folderDto.getName(), result.getName());
        assertNull(result.getParentFolder());
        assertEquals(1, CollectionUtils.size(result.getAccesses()));
        var resultAccess = result.getAccesses().getFirst();
        assertEquals(savedUser.getId(), resultAccess.getUser().getId());
        assertEquals(result.getId(), resultAccess.getFolder().getId());
        assertEquals(UserAccessType.OWNER, resultAccess.getAccessType());
    }

    @Test
    void testUpdate_withParentFolderMoving() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedParentFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        var result = folderService.update(savedFolder.getId(), folderDto);
        assertEquals(savedFolder.getId(), result.getId());
        assertEquals(folderDto.getName(), result.getName());
        assertEquals(savedParentFolder.getId(), result.getParentFolder().getId());
        assertEquals(1, CollectionUtils.size(result.getAccesses()));
        var resultAccess = result.getAccesses().getFirst();
        assertEquals(savedUser.getId(), resultAccess.getUser().getId());
        assertEquals(result.getId(), resultAccess.getFolder().getId());
        assertEquals(UserAccessType.OWNER, resultAccess.getAccessType());
    }

    @Test
    void testUpdate_withParentFolderRemoval() {
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .build();

        var result = folderService.update(savedFolderWithParentFolder.getId(), folderDto);
        assertEquals(savedFolderWithParentFolder.getId(), result.getId());
        assertEquals(folderDto.getName(), result.getName());
        assertNull(result.getParentFolder());
        assertEquals(1, CollectionUtils.size(result.getAccesses()));
        var resultAccess = result.getAccesses().getFirst();
        assertEquals(savedUser.getId(), resultAccess.getUser().getId());
        assertEquals(result.getId(), resultAccess.getFolder().getId());
        assertEquals(UserAccessType.OWNER, resultAccess.getAccessType());
    }

    @Test
    void testUpdate_withParentFolderEqualsFolderToUpdate() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        assertThrows(ValidationException.class, () -> folderService.update(savedFolder.getId(), folderDto));
    }

    @Test
    void testGetAllAccessesByFolderId() {
        var result = folderService.getAllAccessesByFolderId(savedFolder.getId());
        assertEquals(1, CollectionUtils.size(result));
        var resultAccess = result.getFirst();
        assertEquals(savedUser.getId(), resultAccess.getUser().getId());
        assertEquals(savedFolder.getId(), resultAccess.getFolder().getId());
        assertEquals(UserAccessType.OWNER, resultAccess.getAccessType());
    }

    @Test
    @Transactional
    void testDelete_withExistedFolder() {
        var id = savedFolder.getId();
        folderService.delete(id);
        var result = folderService.findById(id);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete_withoutExistedFolder() {
        var id = generateNewUuid();
        assertThrows(EntityNotFoundException.class, () -> folderService.delete(id));
    }

    @Test
    @Transactional
    void testSetupUserInternalFileReadAccessForParentFolders() {
        var user = User.builder()
                .firstName("New")
                .lastName("Access")
                .email("new_access@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        var savedUser = userRepository.save(user);

        folderService.setupUserInternalFileReadAccessForParentFolders(savedFolderWithParentFolder.getId(), savedUser.getId());
    }
}
