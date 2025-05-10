package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.UuidBaseEntity;

import java.util.UUID;

public class UuidBaseEntityRepository<T extends UuidBaseEntity> extends Repository<T, UUID> {

    public UuidBaseEntityRepository(Class<T> entityClass) {
        super(entityClass);
    }
}
