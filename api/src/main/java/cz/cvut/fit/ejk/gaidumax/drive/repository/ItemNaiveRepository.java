package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemDto;
import cz.cvut.fit.ejk.gaidumax.drive.dto.ItemSearchDto;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@ApplicationScoped
public class ItemNaiveRepository {

    @Inject
    EntityManager entityManager;
    @Inject
    SecurityContextProvider securityContextProvider;

    public List<ItemDto> findAll(ItemSearchDto searchDto) {
        var sql = new StringBuilder("""
                select item.id, item.name, item.type, item.size, item.parentFolderId
                from (
                    select fil.id as id,
                           fil.file_name as name,
                           'FILE' as type,
                           fil.size as size,
                           fil.folder_id as parentFolderId,
                           fil.user_id as authorId
                    from file fil
                    union
                    select fol.id as id,
                           fol.name as name,
                           'FOLDER' as type,
                           null as size,
                           fol.folder_id as parentFolderId,
                           fol.user_id as authorId
                    from folder fol
                ) item
                where (item.parentFolderId is null and cast(:parentFolderId as uuid) is null
                       or item.parentFolderId = cast(:parentFolderId as uuid))
                  and item.authorId = :authorId
                """);

        if (CollectionUtils.isNotEmpty(searchDto.getTypes())) {
            sql.append('\n');
            sql.append("and item.type in (:types)");
        }

        sql.append('\n');
        sql.append("offset :offset limit :limit");

        var authorId = securityContextProvider.getUserId();
        var page = searchDto.getPage();
        var pageSize = searchDto.getPageSize();
        var query = entityManager.createNativeQuery(sql.toString(), ItemDto.class)
                .setParameter("parentFolderId", searchDto.getParentFolderId())
                .setParameter("authorId", authorId)
                .setParameter("offset", (page - 1) * pageSize)
                .setParameter("limit", pageSize);

        if (CollectionUtils.isNotEmpty(searchDto.getTypes())) {
            var typeStrings = searchDto.getTypes().stream().map(Enum::name).toList();
            query.setParameter("types", typeStrings);
        }

        return query.getResultList();
    }
}
