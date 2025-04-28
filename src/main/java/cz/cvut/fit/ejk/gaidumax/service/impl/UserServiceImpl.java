package cz.cvut.fit.ejk.gaidumax.service.impl;

import cz.cvut.fit.ejk.gaidumax.entity.User;
import cz.cvut.fit.ejk.gaidumax.repository.UserRepository;
import cz.cvut.fit.ejk.gaidumax.service.interfaces.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow();
    }
}
