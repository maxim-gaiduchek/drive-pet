package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Folder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

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

    @Transactional(Transactional.TxType.MANDATORY)
    public void setupUserInternalFileReadAccessForParentFolders(UUID childFolderId, Long userId) {
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
                        insert into user_to_folder_access (created_at, updated_at, folder_id, user_id, access_type)
                        select now(), now(), f.id, :userId, 'INTERNAL_FILE_READ'
                        from folder_hierarchy f
                        where f.id not in (select fol.id
                                           from folder fol
                                                    join user_to_folder_access a on a.folder_id = fol.id
                                           where a.user_id = :userId)
                        """)
                .setParameter("folderId", childFolderId)
                .setParameter("userId", userId);
        query.executeUpdate();
    }

    @Transactional(Transactional.TxType.MANDATORY)
    public void removeUserInternalFileReadAccessForParentFolders(UUID childFolderId, Long userId) {
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
                        delete from user_to_folder_access utfa
                        where utfa.id in (select utfa2.id
                                          from folder_hierarchy f
                                                 join user_to_folder_access utfa2 on utfa2.folder_id = f.id
                                          where utfa2.access_type = 'INTERNAL_FILE_READ'
                                            and utfa2.user_id = :userId)
                        """)
                .setParameter("folderId", childFolderId)
                .setParameter("userId", userId);
        query.executeUpdate();
    }
}
