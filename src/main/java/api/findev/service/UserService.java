package api.findev.service;

import api.findev.dto.UserDto;
import api.findev.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User findByEmail(String email);
    User save(User user);

    User findById(UUID id);

    void deleteUser(UUID id);
    List<User> findAll();
}
