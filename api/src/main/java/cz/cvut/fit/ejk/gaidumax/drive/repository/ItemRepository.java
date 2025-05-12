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
                with recursive file_folder_ancestors as (select f.folder_id
                                                         from file f
                                                                  join user_to_file_access fa on fa.file_id = f.id
                                                         where fa.user_id = :userId
                                                           and fa.access_type <> 'OWNER'
                
                                                         union
                
                                                         select fol.folder_id
                                                         from folder fol
                                                                  join file_folder_ancestors ffa on fol.id = ffa.folder_id
                                                         where fol.folder_id is not null)
                select *
                from (select f.id           as id,
                             f.file_name    as name,
                             'FILE'         as type,
                             f.size         as size,
                             f.folder_id    as folder_id,
                             ofa.user_id     as owner_id,
                             f.created_at   as created_at,
                             f.updated_at   as updated_at,
                             f.s3_file_path as path,
                             f.file_type    as file_type,
                             fa.access_type as user_access_type
                      from file f
                               join user_to_file_access fa on fa.file_id = f.id
                               join user_to_file_access ofa on ofa.file_id = f.id
                      where fa.user_id = :userId
                        and ofa.access_type = 'OWNER'
                
                      union
                
                      select fol.id         as id,
                             fol.name       as name,
                             'FOLDER'       as type,
                             null           as size,
                             fol.folder_id  as folder_id,
                             fa.user_id     as owner_id,
                             fol.created_at as created_at,
                             fol.updated_at as updated_at,
                             null           as path,
                             null           as file_type,
                             fa.access_type as user_access_type
                      from folder fol
                               join user_to_folder_access fa on fa.folder_id = fol.id
                      where fa.user_id = :userId
                        and fa.access_type = 'OWNER'
                
                      union
                
                      select fol.id         as id,
                             fol.name       as name,
                             'FOLDER'       as type,
                             null           as size,
                             fol.folder_id  as folder_id,
                             ofa.user_id    as owner_id,
                             fol.created_at as created_at,
                             fol.updated_at as updated_at,
                             null           as path,
                             null           as file_type,
                             fa.access_type as user_access_type
                      from folder fol
                               join user_to_folder_access fa on fa.folder_id = fol.id
                               join user_to_folder_access ofa on ofa.folder_id = fol.id
                      where fol.id in (select folder_id from file_folder_ancestors)
                        and ofa.access_type = 'OWNER') i
                where 1=1
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
            sql.append("and i.folder_id = cast(:parentFolderId as uuid)");
        } else {
            sql.append("and i.folder_id is null");
        }
        var sort = filter.buildSort();
        var pageable = filter.buildPageable();
        var authorId = securityContextProvider.getUserId();
        var query = createNativeQuery(sql.toString(), sort, pageable)
                .setParameter("userId", authorId);
        if (parentFolderId != null) {
            query.setParameter("parentFolderId", parentFolderId.toString());
        }
        if (CollectionUtils.isNotEmpty(filter.getTypes())) {
            var typesStr = filter.getTypes().stream().map(Enum::name).toList();
            query.setParameter("types", typesStr);
        }
        if (StringUtils.isNotEmpty(filter.getName())) {
            query.setParameter("name", "%" + filter.getName() + "%");
        }
        return query.getResultList();
    }
}
