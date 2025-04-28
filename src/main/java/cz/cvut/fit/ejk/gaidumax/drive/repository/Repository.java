package cz.cvut.fit.ejk.gaidumax.drive.repository;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class Repository<E, T> {

    @Inject
    EntityManager entityManager;

    private final Class<E> entityClass;

    public Optional<E> findById(T id) {
        try {
            var user = entityManager.find(entityClass, id);
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
