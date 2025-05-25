package cz.cvut.fit.ejk.gaidumax.drive.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class FileControllerTest {

    private static final UUID TEST_FILE_ID = UUID.fromString("08aed70e-1fe1-424a-969b-b7bf7b1303f9");
    private static final String TEST_TOKEN = "test-token";  // <--- заменить на валидный токен, если используется

    @Test
    void testGetFileById_authenticated() {
        given()
                .auth().oauth2(TEST_TOKEN)
                .when().get("/files/" + TEST_FILE_ID)
                .then()
                .statusCode(200)
                .body("id", is(TEST_FILE_ID.toString()));
    }

    @Test
    void testGetFileById_unauthenticated() {
        given()
                .when().get("/files/" + TEST_FILE_ID)
                .then()
                .statusCode(401);
    }

    @Test
    void testCreateFile_authenticated() {
        given()
                .auth().oauth2(TEST_TOKEN)
                .multiPart("name", "testfile.txt")
                .multiPart("file", "Hello World!", "text/plain")
                .when().post("/files")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", is("testfile.txt"));
    }

    @Test
    void testCreateFile_unauthenticated() {
        given()
                .multiPart("name", "testfile.txt")
                .multiPart("file", "Hello World!", "text/plain")
                .when().post("/files")
                .then()
                .statusCode(401);
    }

    @Test
    void testUpdateFile_authenticated() {
        given()
                .auth().oauth2(TEST_TOKEN)
                .contentType("application/json")
                .body("{ \"name\": \"updated-file.txt\" }")
                .when().put("/files/" + TEST_FILE_ID)
                .then()
                .statusCode(200)
                .body("name", is("updated-file.txt"));
    }

    @Test
    void testUpdateFile_unauthenticated() {
        given()
                .contentType("application/json")
                .body("{ \"name\": \"updated-file.txt\" }")
                .when().put("/files/" + TEST_FILE_ID)
                .then()
                .statusCode(401);
    }

    @Test
    void testDeleteFile_authenticated() {
        given()
                .auth().oauth2(TEST_TOKEN)
                .when().delete("/files/" + TEST_FILE_ID)
                .then()
                .statusCode(204);
    }

    @Test
    void testDeleteFile_unauthenticated() {
        given()
                .when().delete("/files/" + TEST_FILE_ID)
                .then()
                .statusCode(401);
    }
}
