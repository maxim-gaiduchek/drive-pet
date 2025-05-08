package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FolderRepository extends UuidBaseEntityRepository<Folder> {

    public FolderRepository() {
        super(Folder.class);
    }

    public List<Folder> findAllParents(UUID childFolderId) {
        var query = entityManager.createNativeQuery("""
                            with recursive folder_hierarchy as (
                                select f.*
                                from folder f
                                where f.id = :folderId
                                union all
                                select p.*
                                from folder p
                                join folder_hierarchy fh on fh.folder_id = p.id
                            )
                            select * from folder_hierarchy
                        """, Folder.class)
                .setParameter("folderId", childFolderId);
        var result = query.getResultList();
        Collections.reverse(result);
        return result;
    }
}
