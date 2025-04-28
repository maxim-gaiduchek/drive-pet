package cz.cvut.fit.ejk.gaidumax.repository;

import cz.cvut.fit.ejk.gaidumax.entity.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository extends BaseEntityRepository<User> {

    public UserRepository() {
        super(User.class);
    }
}
