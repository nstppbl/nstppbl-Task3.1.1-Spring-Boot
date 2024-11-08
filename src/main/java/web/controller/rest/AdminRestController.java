package web.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.UserService;
import web.dao.RoleRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminRestController(UserService userService, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Add new user
    @PostMapping("/users/new")
    public ResponseEntity<Void> addUser(@RequestBody User user, @RequestParam(value = "admin", required = false) String admin) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if ("true".equals(admin)) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            user.setRoles(Collections.singleton(adminRole));
        } else {
            Role defaultRole = roleRepository.findByName("ROLE_USER");
            user.setRoles(Collections.singleton(defaultRole));
        }

        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Update user information
    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User userFromDb = userService.getUserById(id);
        userFromDb.setName(updatedUser.getName());
        userFromDb.setLastName(updatedUser.getLastName());
        userFromDb.setEmail(updatedUser.getEmail());

        Set<Role> roles = updatedUser.getRoles();
        if (roles != null) {
            userFromDb.setRoles(roles);
        }

        userService.updateUser(userFromDb);
        return ResponseEntity.ok().build();
    }

    // Delete user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // Delete all users
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok().build();
    }
}
