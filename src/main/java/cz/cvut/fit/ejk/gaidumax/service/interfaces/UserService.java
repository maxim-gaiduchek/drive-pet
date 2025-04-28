package cz.cvut.fit.ejk.gaidumax.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    User getByIdOrThrow(Long id);
}
