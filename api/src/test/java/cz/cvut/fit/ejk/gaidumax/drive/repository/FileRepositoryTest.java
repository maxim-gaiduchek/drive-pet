package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class FileRepositoryTest {

    @Inject
    FileRepository fileRepository;

    private File savedFile;
    private File savedFileWithAccessToken;

    @BeforeEach
    @Transactional
    void setUp() {
        var file = File.builder()
                .fileName("fileName1.png")
                .fileType("image/png")
                .size(1000L)
                .s3FilePath("/s3/file/path/1")
                .build();
        var fileWithAccessToken = File.builder()
                .fileName("fileName2.png")
                .fileType("image/png")
                .size(2000L)
                .s3FilePath("/s3/file/path/2")
                .accessToken("accessToken1")
                .build();
        savedFile = fileRepository.save(file);
        savedFileWithAccessToken = fileRepository.save(fileWithAccessToken);
    }

    @AfterEach
    @Transactional
    void tearDown() {
        fileRepository.delete(savedFile);
        fileRepository.delete(savedFileWithAccessToken);
    }

    @Test
    void testFindByAccessToken_withPresentFile() {
        var result = fileRepository.findByAccessToken(savedFileWithAccessToken.getAccessToken());
        assertTrue(result.isPresent());
        assertEquals(savedFileWithAccessToken.getId(), result.get().getId());
        assertEquals(savedFileWithAccessToken.getFileName(), result.get().getFileName());
        assertEquals(savedFileWithAccessToken.getFileType(), result.get().getFileType());
        assertEquals(savedFileWithAccessToken.getSize(), result.get().getSize());
        assertEquals(savedFileWithAccessToken.getS3FilePath(), result.get().getS3FilePath());
        assertEquals(savedFileWithAccessToken.getAccessToken(), result.get().getAccessToken());
    }

    @Test
    void testFindByAccessToken_withEmptyFile() {
        var result = fileRepository.findByAccessToken("accessToken666");
        assertTrue(result.isEmpty());
    }

    @Test
    void testExistsByEmail() {
        var resultExists = fileRepository.existsByAccessToken(savedFileWithAccessToken.getAccessToken());
        var resultNotExists = fileRepository.existsByAccessToken("accessToken666");
        assertTrue(resultExists);
        assertFalse(resultNotExists);
    }
}
