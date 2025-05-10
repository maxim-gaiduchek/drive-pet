package cz.cvut.fit.ejk.gaidumax.drive.repository;

import cz.cvut.fit.ejk.gaidumax.drive.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

import java.util.Optional;

@ApplicationScoped
public class UserRepository extends BaseEntityRepository<User> {

    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findByEmail(String email) {
        try {
            var user = entityManager.createQuery("""
                                        select u
                                        from User u
                                        where u.email = :email
                            """, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email) {
        return entityManager.createQuery("""
                                    select count(u) > 0
                                    from User u
                                    where u.email = :email
                        """, Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
