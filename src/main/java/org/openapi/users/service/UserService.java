package org.openapi.users.service;

import org.openapi.users.api.UsersApiDelegate;
import org.openapi.users.model.User;
import org.openapi.users.model.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService implements UsersApiDelegate{
    // Simple in-memory store (replace later with DB)
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // ---------------- CREATE ----------------

    @Override
    public ResponseEntity<User> createUser(UserRequest userRequest) {

        Long id = idGenerator.getAndIncrement();

        User user = new User()
                .id(id)
                .name(userRequest.getName())
                .email(userRequest.getEmail());

        users.put(id, user);

        return ResponseEntity.ok(user);
    }

    // ---------------- READ (by id) ----------------

    @Override
    public ResponseEntity<User> getUserById(Long id) {

        User user = users.get(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    // ---------------- READ (all) ----------------

    @Override
    public ResponseEntity<List<User>> getUsers() {

        return ResponseEntity.ok(new ArrayList<>(users.values()));
    }

    // ---------------- UPDATE ----------------

    @Override
    public ResponseEntity<User> updateUser(Long id, UserRequest userRequest) {

        User existingUser = users.get(id);

        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser
                .name(userRequest.getName())
                .email(userRequest.getEmail());

        users.put(id, existingUser);

        return ResponseEntity.ok(existingUser);
    }

    // ---------------- DELETE ----------------

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {

        if (!users.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }

        users.remove(id);
        idGenerator.decrementAndGet();
        return ResponseEntity.noContent().build();
    }
}

