package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.repository.data.PageRequest;
import cz.cvut.fit.ejk.gaidumax.drive.repository.data.Sort;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Repository<E, T> {

    private static final String ORDER_BY_QUERY_TEMPLATE = "order by %s";
    private static final String OFFSET_LIMIT_QUERY_TEMPLATE = "offset %d first %d rows only";

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

    public E save(E entity) {
        return saveTransactional(entity);
    }

    @Transactional
    E saveTransactional(E entity) {
        return entityManager.merge(entity);
    }

    public void delete(E entity) {
        deleteTransactional(entity);
    }

    @Transactional
    void deleteTransactional(E entity) {
        E entityToDelete = entityManager.merge(entity);
        entityManager.remove(entityToDelete);
    }

    TypedQuery<E> createQuery(String sql) {
        return entityManager.createQuery(sql, entityClass);
    }

    <R> TypedQuery<R> createQuery(String sql, Class<R> resultClass) {
        return entityManager.createQuery(sql, resultClass);
    }

    TypedQuery<E> createQuery(String sql, Sort sort) {
        var sqlWithSort = appendSortToQuery(sql, sort);
        return createQuery(sqlWithSort);
    }

    TypedQuery<E> createQuery(String sql, PageRequest pageRequest) {
        return createQuery(sql)
                .setFirstResult(pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize());
    }

    TypedQuery<E> createQuery(String sql, Sort sort, PageRequest pageRequest) {
        var sqlWithSort = appendSortToQuery(sql, sort);
        return createQuery(sqlWithSort);
    }

    Query createNativeQuery(String sql) {
        return entityManager.createNativeQuery(sql, entityClass);
    }

    Query createNativeQuery(String sql, Sort sort) {
        var sqlWithSort = appendSortToQuery(sql, sort);
        return createNativeQuery(sqlWithSort);
    }

    Query createNativeQuery(String sql, PageRequest pageRequest) {
        return createNativeQuery(sql)
                .setFirstResult(pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize());
    }

    Query createNativeQuery(String sql, Sort sort, PageRequest pageRequest) {
        var sqlWithSort = appendSortToQuery(sql, sort);
        return createNativeQuery(sqlWithSort, pageRequest);
    }

    private String appendSortToQuery(String sql, Sort sort) {
        if (sort == null) {
            return sql;
        }
        var query = Arrays.stream(sort.getSortBy())
                .map(sortBy -> sortBy + " " + sort.getSortDirection().getSql())
                .collect(Collectors.joining(", "));
        return sql + "\n" +
               ORDER_BY_QUERY_TEMPLATE.formatted(query);
    }

    private String appendPageableToQuery(String sql, PageRequest pageRequest) {
        if (pageRequest == null) {
            return sql;
        }
        return sql + "\n" +
               OFFSET_LIMIT_QUERY_TEMPLATE.formatted(pageRequest.getOffset(), pageRequest.getPageSize());
    }
}
