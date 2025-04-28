package cz.cvut.fit.ejk.gaidumax.repository;

import cz.cvut.fit.ejk.gaidumax.entity.BaseEntity;

public class BaseEntityRepository<T extends BaseEntity> extends Repository<T, Long> {

    public BaseEntityRepository(Class<T> entityClass) {
        super(entityClass);
    }
}
