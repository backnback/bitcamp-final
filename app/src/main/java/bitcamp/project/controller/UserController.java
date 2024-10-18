package bitcamp.project.controller;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("list")
    public List<User> list() throws Exception{
        List<User> users =  userService.list();

        return users;
    }

    @GetMapping("finduser")
    public User get(@RequestParam int no) throws Exception{
        User user = userService.findUser(no);
        user.setPassword(null);
        return user;
    }

    @PostMapping("update")
    public boolean update(@RequestParam("no") int no, @RequestBody User user) throws Exception{
        System.out.println("여기");
        System.out.println(user.getPassword());
        return userService.update(no, user);
    }

    @DeleteMapping("delete/{no}")
    public boolean delete(@PathVariable int no) throws Exception{
        return userService.delete(no);
    }
}
