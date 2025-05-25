package cz.cvut.fit.ejk.gaidumax.drive.controller;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@QuarkusTest
class ItemControllerTest {

    @Inject
    EntityManager entityManager;

    @Test
    void findAll() {
        var result = entityManager.createQuery("""
                        select i
                        from File i
                        """)
                .getResultList();
        log.info(result.toString());
    }
}
