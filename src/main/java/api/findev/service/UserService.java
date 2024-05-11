package api.findev.service;

import api.findev.model.User;

import java.util.UUID;

public interface UserService {
    User findByEmail(String email);
    User save(User user);

    void deleteUser(UUID id);
}
