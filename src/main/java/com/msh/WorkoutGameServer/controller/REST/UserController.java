package com.msh.WorkoutGameServer.controller.REST;

import com.msh.WorkoutGameServer.model.message.ApiResponse;
import com.msh.WorkoutGameServer.model.user.LoginUser;
import com.msh.WorkoutGameServer.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public ApiResponse saveUser(@RequestBody LoginUser user) {
        userService.save(user);
        return new ApiResponse(true, "User saved successfully.", null);
    }

    @GetMapping
    public ApiResponse listUser() {
        return new ApiResponse(true, "User list fetched successfully.", userService.findAll());
    }

    @DeleteMapping("/{name}")
    public ApiResponse delete(@PathVariable String name) {
        userService.deleteByName(name);
        return new ApiResponse(true, "User deleted successfully.", null);
    }

    @DeleteMapping("/all")
    public ApiResponse deleteAll() {
        userService.deleteAll();
        return new ApiResponse(true, "All users deleted successfully.", null);
    }


}
