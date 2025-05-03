package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.File;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileRepository extends UuidBaseEntityRepository<File> {

    public FileRepository() {
        super(File.class);
    }
}
