package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.UserService;
import web.dao.RoleRepository;

import java.security.Principal;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String getAllUsers(Model model, Principal principal) {
        User currentUser = userService.getUserByEmail(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }


    @GetMapping("/users/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        return "new_user";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute("user") User user, @RequestParam(value = "admin", required = false) String admin) {
        if ("true".equals(admin)) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            user.setRoles(Collections.singleton(adminRole));
        } else {
            Role defaultRole = roleRepository.findByName("ROLE_USER");
            user.setRoles(Collections.singleton(defaultRole));
        }
        userService.addUser(user);
        return "redirect:/admin";
    }


    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user_details"; // отображает информацию о пользователе
    }

    @GetMapping("/users/edit/{id}")
    public String showUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "edit_user";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User updatedUser) {
        User userFromDb = userService.getUserById(id);  // Загружаем пользователя из БД
        userFromDb.setName(updatedUser.getName());
        userFromDb.setLastName(updatedUser.getLastName());
        userFromDb.setEmail(updatedUser.getEmail());
        userService.updateUser(userFromDb);  // Сохраняем обновления
        return "redirect:/admin";

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/deleteAll")
    public String deleteAllUsers() {
        userService.deleteAllUsers();
        return "redirect:/users";
    }

}
