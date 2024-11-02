package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import web.dao.RoleRepository;
import web.model.User;
import web.service.UserService;
import web.service.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;

    }


//    @GetMapping("/")
//    public String getCurrentUserInfo(@AuthenticationPrincipal UserDetails currentUser, Model model) {
//        Long userId = ((User) currentUser).getId();
//        User user = userService.getUserById(userId);
//        if (user == null) {
//            throw new IllegalArgumentException("User not found");
//        }
//        model.addAttribute("user", user);
//        return "user";
//    }

    @GetMapping()
    public String userInfo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        return "user_info";
    }



}
