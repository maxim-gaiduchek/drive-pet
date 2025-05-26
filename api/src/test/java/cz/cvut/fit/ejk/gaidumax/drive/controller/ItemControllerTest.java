package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFileAccess;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFolderAccess;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FileRepository;
import cz.cvut.fit.ejk.gaidumax.drive.repository.FolderRepository;
import cz.cvut.fit.ejk.gaidumax.drive.repository.UserRepository;
import cz.cvut.fit.ejk.gaidumax.drive.security.Role;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

@QuarkusTest
class ItemControllerTest {

    @InjectMock
    SecurityContextProvider securityContextProvider;

    @Inject
    FileRepository fileRepository;
    @Inject
    FolderRepository folderRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    EntityManager entityManager;

    private File savedFile;
    private File savedFileWithParentFolder;
    private Folder savedFolder;
    private Folder savedFolderWithParentFolder;
    private User savedOwner;
    private User savedUser;

    @BeforeEach
    @Transactional
    void setUp() {
        setUpUsers();
        setUpFolders();
        setUpFiles();
    }

    private void setUpUsers() {
        var owner = User.builder()
                .firstName("Max")
                .lastName("Test")
                .email("test@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        savedOwner = userRepository.save(owner);

        var user = User.builder()
                .firstName("John")
                .lastName("Test")
                .email("test@test.com")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        savedUser = userRepository.save(user);
    }

    private void setUpFolders() {
        var folder = Folder.builder()
                .name("Test folder 1")
                .build();
        var folderOwnerAccess = UserFolderAccess.builder()
                .user(savedOwner)
                .folder(folder)
                .accessType(UserAccessType.OWNER)
                .build();
        folder.getAccesses().add(folderOwnerAccess);
        var folderUserAccess = UserFolderAccess.builder()
                .user(savedUser)
                .folder(folder)
                .accessType(UserAccessType.INTERNAL_FILE_READ)
                .build();
        folder.getAccesses().add(folderUserAccess);
        savedFolder = folderRepository.save(folder);

        var folderWithParentFolder = Folder.builder()
                .name("Test folder 2")
                .parentFolder(savedFolder)
                .build();
        var folderWithParentFolderAccess = UserFolderAccess.builder()
                .user(savedOwner)
                .folder(folderWithParentFolder)
                .accessType(UserAccessType.OWNER)
                .build();
        folderWithParentFolder.getAccesses().add(folderWithParentFolderAccess);
        savedFolderWithParentFolder = folderRepository.save(folderWithParentFolder);
    }

    private void setUpFiles() {
        var file = File.builder()
                .fileName("fileName1.png")
                .fileType("image/png")
                .size(1000L)
                .s3FilePath("/s3/file/path/1")
                .build();
        var fileAccess = UserFileAccess.builder()
                .user(savedOwner)
                .file(file)
                .accessType(UserAccessType.OWNER)
                .build();
        file.getAccesses().add(fileAccess);
        savedFile = fileRepository.save(file);

        var fileWithParentFolder = File.builder()
                .fileName("fileName2.png")
                .fileType("image/png")
                .size(2000L)
                .s3FilePath("/s3/file/path/2")
                .parentFolder(savedFolder)
                .build();
        var fileWithParentFolderOwnerAccess = UserFileAccess.builder()
                .user(savedOwner)
                .file(fileWithParentFolder)
                .accessType(UserAccessType.OWNER)
                .build();
        fileWithParentFolder.getAccesses().add(fileWithParentFolderOwnerAccess);
        var fileWithParentFolderUserAccess = UserFileAccess.builder()
                .user(savedUser)
                .file(fileWithParentFolder)
                .accessType(UserAccessType.READ)
                .build();
        fileWithParentFolder.getAccesses().add(fileWithParentFolderUserAccess);
        savedFileWithParentFolder = fileRepository.save(fileWithParentFolder);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        entityManager.createQuery("""
                        delete from UserFileAccess
                        """)
                .executeUpdate();
        entityManager.createQuery("""
                        delete from File
                        """)
                .executeUpdate();
        entityManager.createQuery("""
                        delete from UserFolderAccess
                        """)
                .executeUpdate();
        entityManager.createQuery("""
                        delete from Folder
                        """)
                .executeUpdate();
        entityManager.createQuery("""
                        delete from User
                        """)
                .executeUpdate();
    }

    @Test
    void testFindAll_withoutAuthentication() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/items")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withoutFilters_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(savedFile.getId().toString()))
                .body("[0].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[0].userAccessType", equalTo(UserAccessType.OWNER.name()))
                .body("[0].parentFolder", nullValue())
                .body("[1].id", equalTo(savedFolder.getId().toString()))
                .body("[1].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[1].userAccessType", equalTo(UserAccessType.OWNER.name()))
                .body("[1].parentFolder", nullValue());
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withParentFolderFilter_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("parentFolderId", savedFolder.getId().toString())
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(savedFileWithParentFolder.getId().toString()))
                .body("[0].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[0].userAccessType", equalTo(UserAccessType.OWNER.name()))
                .body("[0].parentFolder.id", equalTo(savedFolder.getId().toString()))
                .body("[1].id", equalTo(savedFolderWithParentFolder.getId().toString()))
                .body("[1].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[1].userAccessType", equalTo(UserAccessType.OWNER.name()))
                .body("[1].parentFolder.id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withAnotherParentFolderFilter_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("parentFolderId", savedFileWithParentFolder.getId().toString())
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(0));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withSortByName_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("sortBy", "name")
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(savedFile.getId().toString()))
                .body("[1].id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withSortByNameDesc_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "desc")
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(savedFile.getId().toString()))
                .body("[1].id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withSortByNameAsc_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "asc")
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(savedFolder.getId().toString()))
                .body("[1].id", equalTo(savedFile.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withParentFolderFilterSortByNameAsc_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("parentFolderId", savedFolder.getId().toString())
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "asc")
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(savedFolderWithParentFolder.getId().toString()))
                .body("[1].id", equalTo(savedFileWithParentFolder.getId().toString()));
    }

    @Disabled
    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withParentFolderFilterSortByNameAscPageSize1_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("parentFolderId", savedFolder.getId().toString())
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "asc")
                .queryParam("pageSize", 1)
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(savedFolderWithParentFolder.getId().toString()));
    }

    @Disabled
    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withParentFolderFilterSortByNameAscPage1PageSize1_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("parentFolderId", savedFolder.getId().toString())
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "asc")
                .queryParam("pageSize", 1)
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(savedFolderWithParentFolder.getId().toString()));
    }

    @Disabled
    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withParentFolderFilterSortByNameAscPage2PageSize1_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("parentFolderId", savedFolder.getId().toString())
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "asc")
                .queryParam("pageSize", 1)
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(savedFileWithParentFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withTypesFilters_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("types", List.of("FOLDER"))
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(savedFolder.getId().toString()))
                .body("[0].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[0].userAccessType", equalTo(UserAccessType.OWNER.name()))
                .body("[0].parentFolder", nullValue());
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withEmptyTypesFilters_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("types", List.of())
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(savedFile.getId().toString()))
                .body("[0].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[0].userAccessType", equalTo(UserAccessType.OWNER.name()))
                .body("[0].parentFolder", nullValue())
                .body("[1].id", equalTo(savedFolder.getId().toString()))
                .body("[1].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[1].userAccessType", equalTo(UserAccessType.OWNER.name()))
                .body("[1].parentFolder", nullValue());
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withoutFilters_withUserAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(savedFolder.getId().toString()))
                .body("[0].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[0].userAccessType", equalTo(UserAccessType.INTERNAL_FILE_READ.name()))
                .body("[0].parentFolder", nullValue());
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testFindAll_withParentFolderFilter_withUserAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        given()
                .accept(ContentType.JSON)
                .queryParam("parentFolderId", savedFolder.getId().toString())
                .when()
                .get("/items")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(savedFileWithParentFolder.getId().toString()))
                .body("[0].owner.id", equalTo(savedOwner.getId().intValue()))
                .body("[0].userAccessType", equalTo(UserAccessType.READ.name()))
                .body("[0].parentFolder.id", equalTo(savedFolder.getId().toString()));
    }
}
