package com.chat.evoltchatapp.rest.controller;

import com.chat.evoltchatapp.core.model.User;
import com.chat.evoltchatapp.core.service.UserService;
import com.chat.evoltchatapp.rest.dto.UserDTO;
import com.chat.evoltchatapp.rest.websockets.MainSocketHandler;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@SecurityRequirement(name = "JWT Security")
public class UserController {

    private final UserService userService;
    private final MainSocketHandler mainSocketHandler;

    public UserController(UserService userService, MainSocketHandler mainSocketHandler){
        this.userService = userService;
        this.mainSocketHandler = mainSocketHandler;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @RequestMapping(method = RequestMethod.GET, path = "email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) throws Exception {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @RequestMapping(method = RequestMethod.GET, path = "getme/{token}")
    public ResponseEntity<String> getUserByToken(@PathVariable String token) throws Exception {
        System.out.println("TOKEN: " + token);
        User user = mainSocketHandler.getUser(token);
        return ResponseEntity.ok(user.getUsername());
    }


    @RequestMapping(method = RequestMethod.GET, path = "username/{username}")
    public  boolean getUserByUsername(@PathVariable String username){
        return userService.usernameExists(username);
    }


}
