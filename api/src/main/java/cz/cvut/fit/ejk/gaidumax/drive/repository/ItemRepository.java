package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Item;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@ApplicationScoped
public class ItemRepository extends UuidBaseEntityRepository<Item> {

    @Inject
    SecurityContextProvider securityContextProvider;

    public ItemRepository() {
        super(Item.class);
    }

    public List<Item> findAll(ItemFilter filter) {
        var sql = new StringBuilder("""
                select i
                from Item i
                where i.author.id = :authorId
                """);
        if (CollectionUtils.isNotEmpty(filter.getTypes())) {
            sql.append('\n');
            sql.append("and i.type in (:types)");
        }
        sql.append('\n');
        var parentFolderId = filter.getParentFolderId();
        if (parentFolderId != null) {
            sql.append("i.parentFolder.id = cast(:parentFolderId as uuid)");
        } else {
            sql.append("and i.parentFolder.id is null");
        }
        var sort = filter.buildSort();
        var pageable = filter.buildPageable();
        var authorId = securityContextProvider.getUserId();
        var query = createQuery(sql.toString(), sort, pageable)
                .setParameter("authorId", authorId);
        if (parentFolderId != null) {
            query.setParameter("parentFolderId", parentFolderId.toString());
        }
        return query.getResultList();
    }
}
