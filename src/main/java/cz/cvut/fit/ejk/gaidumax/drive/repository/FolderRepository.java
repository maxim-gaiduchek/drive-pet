package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FolderRepository extends BaseEntityRepository<Folder> {

    public FolderRepository() {
        super(Folder.class);
    }
}
