package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.Item;
import cz.cvut.fit.ejk.gaidumax.drive.filter.ItemFilter;
import cz.cvut.fit.ejk.gaidumax.drive.service.security.interfaces.SecurityContextProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
                where i.user.id = :userId
                """);
        if (CollectionUtils.isNotEmpty(filter.getTypes())) {
            sql.append('\n');
            sql.append("and i.type in (:types)");
        }
        if (StringUtils.isNotEmpty(filter.getName())) {
            sql.append('\n');
            sql.append("and i.name ilike :name");
        }
        sql.append('\n');
        var parentFolderId = filter.getParentFolderId();
        if (parentFolderId != null) {
            sql.append("and i.parentFolder.Id = cast(:parentFolderId as uuid)");
        } else {
            sql.append("and i.parentFolder.id is null");
        }
        var sort = filter.buildSort();
        var pageable = filter.buildPageable();
        var userId = securityContextProvider.getUserId();
        var query = createQuery(sql.toString(), sort, pageable)
                .setParameter("userId", userId);
        if (parentFolderId != null) {
            query.setParameter("parentFolderId", parentFolderId.toString());
        }
        if (CollectionUtils.isNotEmpty(filter.getTypes())) {
            var typesStr = filter.getTypes().stream().map(Enum::name).toList();
            query.setParameter("types", typesStr);
        }
        if (StringUtils.isNotEmpty(filter.getName())) {
            query.setParameter("name", "%%%s%%".formatted(filter.getName()));
        }
        return query.getResultList();
    }
}
