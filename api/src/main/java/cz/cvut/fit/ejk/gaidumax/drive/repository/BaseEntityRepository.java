package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.BaseEntity;

public class BaseEntityRepository<T extends BaseEntity> extends Repository<T, Long> {

    public BaseEntityRepository(Class<T> entityClass) {
        super(entityClass);
    }
}
