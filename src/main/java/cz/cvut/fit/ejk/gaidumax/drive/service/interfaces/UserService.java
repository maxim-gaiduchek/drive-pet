package cz.cvut.fit.ejk.gaidumax.drive.service.interfaces;

import cz.cvut.fit.ejk.gaidumax.drive.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    User getByIdOrThrow(Long id);
}
