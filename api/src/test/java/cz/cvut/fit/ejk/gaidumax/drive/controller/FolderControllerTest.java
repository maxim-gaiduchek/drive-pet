package cz.cvut.fit.ejk.gaidumax.drive.controller;

import cz.cvut.fit.ejk.gaidumax.drive.dto.FolderDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.UuidBaseInfoDto;
import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserAccessType;
import cz.cvut.fit.ejk.gaidumax.drive.entity.UserFolderAccess;
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
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@QuarkusTest
class FolderControllerTest {

    @InjectMock
    SecurityContextProvider securityContextProvider;

    @Inject
    FolderRepository folderRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    EntityManager entityManager;

    private Folder savedFolder;
    private Folder savedFolderWithParentFolder;
    private User savedOwner;
    private User savedUser;

    @BeforeEach
    @Transactional
    void setUp() {
        setUpUsers();
        setUpFolders();
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
        entityManager.createQuery("""
                        delete from User
                        """)
                .executeUpdate();
    }

    @Test
    void testEndpoints_withoutAuthentication() {
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/folders/%s".formatted(savedFolder.getId()))
                .then()
                .statusCode(401);
        given()
                .accept(ContentType.JSON)
                .when()
                .get("/folders/%s/parents".formatted(savedFolder.getId()))
                .then()
                .statusCode(401);
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .accept(ContentType.JSON)
                .when()
                .post("/folders")
                .then()
                .statusCode(401);
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .accept(ContentType.JSON)
                .when()
                .put("/folders/%s".formatted(savedFolder.getId()))
                .then()
                .statusCode(401);
        given()
                .when()
                .delete("/folders/%s".formatted(savedFolder.getId()))
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testGet_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/folders/%s".formatted(savedFolder.getId()))
                .then()
                .statusCode(200)
                .body("id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testGet_withUserAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/folders/%s".formatted(savedFolder.getId()))
                .then()
                .statusCode(200)
                .body("id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_ADMIN"})
    void testGet_withAdminAuthorization() {
        when(securityContextProvider.getUserRole()).thenReturn(Role.ROLE_ADMIN);

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/folders/%s".formatted(savedFolder.getId()))
                .then()
                .statusCode(200)
                .body("id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testCreate_withAuthentication() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .contentType(ContentType.JSON)
                .body(folderDto)
                .accept(ContentType.JSON)
                .when()
                .post("/folders")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(folderDto.getName()))
                .body("parentFolder.id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testUpdate_withOwnerAuthorization() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .contentType(ContentType.JSON)
                .body(folderDto)
                .accept(ContentType.JSON)
                .when()
                .put("/folders/%s".formatted(savedFolderWithParentFolder.getId()))
                .then()
                .statusCode(200)
                .body("id", equalTo(savedFolderWithParentFolder.getId().toString()))
                .body("name", equalTo(folderDto.getName()))
                .body("parentFolder.id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testUpdate_withUserAuthorization() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        given()
                .contentType(ContentType.JSON)
                .body(folderDto)
                .accept(ContentType.JSON)
                .when()
                .put("/folders/%s".formatted(savedFolderWithParentFolder.getId()))
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_ADMIN"})
    void testUpdate_withAdminAuthorization() {
        var parentFolderDto = UuidBaseInfoDto.builder()
                .id(savedFolder.getId())
                .build();
        var folderDto = FolderDto.builder()
                .name("New test folder")
                .parentFolder(parentFolderDto)
                .build();

        when(securityContextProvider.getUserRole()).thenReturn(Role.ROLE_ADMIN);

        given()
                .contentType(ContentType.JSON)
                .body(folderDto)
                .accept(ContentType.JSON)
                .when()
                .put("/folders/%s".formatted(savedFolderWithParentFolder.getId()))
                .then()
                .statusCode(200)
                .body("id", equalTo(savedFolderWithParentFolder.getId().toString()))
                .body("name", equalTo(folderDto.getName()))
                .body("parentFolder.id", equalTo(savedFolder.getId().toString()));
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testDelete_withOwnerAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedOwner.getId());

        given()
                .when()
                .delete("/folders/%s".formatted(savedFolderWithParentFolder.getId()))
                .then()
                .statusCode(204);
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_USER"})
    void testDelete_withUserAuthorization() {
        when(securityContextProvider.getUserId()).thenReturn(savedUser.getId());

        given()
                .when()
                .delete("/folders/%s".formatted(savedFolderWithParentFolder.getId()))
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "test-user", roles = {"ROLE_ADMIN"})
    void testDelete_withAdminAuthorization() {
        when(securityContextProvider.getUserRole()).thenReturn(Role.ROLE_ADMIN);

        given()
                .when()
                .delete("/folders/%s".formatted(savedFolderWithParentFolder.getId()))
                .then()
                .statusCode(204);
    }
}
