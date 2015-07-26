package dk.responsfabrikken.exception_tracker.core.api;

import dk.responsfabrikken.exception_tracker.core.model.client.UserDto;
import dk.responsfabrikken.exception_tracker.core.model.server.User;
import dk.responsfabrikken.exception_tracker.core.model.server.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    @Autowired UserRepository userRepository;

    @RequestMapping(method = RequestMethod.POST)
    public UserDto save(@RequestBody UserDto userDto) {
        userRepository.save(User.fromUserDto(userDto));
        return userDto;
    }

    @RequestMapping
    public List<UserDto> query() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::fromUser).collect(toList());
    }

    @RequestMapping("/me")
    public HttpEntity<UserDto> me(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/login")
    public HttpEntity<UserDto> login(@RequestBody UserDto userDto, HttpServletRequest request) {
        User byEmailAndPassword = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        if (byEmailAndPassword != null) {
            UserDto fromUser = UserDto.fromUser(byEmailAndPassword);
            request.getSession().setAttribute("user", fromUser);
            return new ResponseEntity<>(fromUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/logout")
    public HttpEntity<UserDto> logout(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        request.getSession().removeAttribute("user");
        return new ResponseEntity<UserDto>(user, HttpStatus.OK);
    }
}
