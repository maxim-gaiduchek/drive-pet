package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.BaseEntity;

import java.time.ZonedDateTime;

public class BaseEntityRepository<T extends BaseEntity> extends Repository<T, Long> {

    public BaseEntityRepository(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public T save(T entity) {
        var oldCreatedAt = entity.getCreatedAt();
        var oldUpdatedAt = entity.getUpdatedAt();
        try {
            var now = ZonedDateTime.now();
            if (entity.getId() == null) {
                entity.setCreatedAt(now);
            }
            entity.setUpdatedAt(now);
            return super.save(entity);
        } catch (Exception e) {
            entity.setCreatedAt(oldCreatedAt);
            entity.setUpdatedAt(oldUpdatedAt);
            throw e;
        }
    }
}
