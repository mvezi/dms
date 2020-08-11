package qa.qdb.dms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qa.qdb.dms.model.User;
import qa.qdb.dms.service.UserService;

import java.util.List;

@RestController
@Api(value = "User API")
@RequestMapping("/credit/dms/users")
public class UserController {

    private static Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation("This operation is for getting all the users")
    @GetMapping(value = "/findall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        List users = userService.findAll();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @ApiOperation("This operation is used to find a particular user")
    @GetMapping(value = "/findone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@RequestParam("userId") String userId) {
        User user = userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation("This operation is used to create a new user")
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestParam("firstName") String firstName) {
        User user = new User();
        user.setFirstName(firstName);
        if(userService.save(user) != null) {
            return ResponseEntity.ok()
                    .body("User created successfully");
        } else
            return ResponseEntity.ok().body("User not created!!!");
    }

}
