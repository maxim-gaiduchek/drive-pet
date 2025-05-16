package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import org.hibernate.NonUniqueResultException;

import java.util.Optional;

@ApplicationScoped
public class FileRepository extends UuidBaseEntityRepository<File> {

    public FileRepository() {
        super(File.class);
    }

    public Optional<File> findByAccessToken(String accessToken) {
        try {
            var sql = createQuery("""
                    select f
                    from File f
                    where f.accessToken = :token
                    """);
            sql.setParameter("token", accessToken);
            var result = sql.getSingleResult();
            return Optional.ofNullable(result);
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }
}
