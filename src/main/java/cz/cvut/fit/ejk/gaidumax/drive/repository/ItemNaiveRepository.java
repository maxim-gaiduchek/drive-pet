package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class ItemNaiveRepository {

    @Inject
    EntityManager entityManager;

    public List<ItemDto> findAll(ItemFilter filter) {
        return entityManager.createNativeQuery("""
                        select item.*
                        from (
                            select fil.file_name as name, 'FILE' as type, fil.size as size, fil.folder_id as parentFolderId
                            from file fil
                            union
                            select fol.name as name, 'FOLDER' as type, null as size, fol.folder_id as parentFolderId
                            from folder fol
                        ) item
                        where coalesce(:parentFolderId, -1) = coalesce(item.parentFolderId, -1)
                        """, ItemDto.class)
                .setParameter("parentFolderId", 1L)
                .getResultList();
    }
}
