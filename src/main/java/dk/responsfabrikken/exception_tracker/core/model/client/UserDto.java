package dk.responsfabrikken.exception_tracker.core.model.client;

import dk.responsfabrikken.exception_tracker.core.model.server.User;

import java.io.Serializable;

public class UserDto implements Serializable {
    private long id;
    private String email;
    private String password;
    private String fullName;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.fullName = user.getFullName();
        userDto.email = user.getEmail();
        return userDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
