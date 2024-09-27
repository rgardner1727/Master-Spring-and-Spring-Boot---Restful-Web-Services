package com.in28minutes.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    private static int usersCount = 0;

    static {
        users.add(new User(++usersCount, "Ryan", LocalDate.now().minusYears(30)));
        users.add(new User(++usersCount, "Jacob", LocalDate.now().minusYears(31)));
        users.add(new User(++usersCount, "Matthew", LocalDate.now().minusYears(32)));
    }

    public List<User> findAllUsers(){
        return UserDaoService.users;
    }

    public User findUserById(Integer id){
        return UserDaoService.users.stream().filter(user -> user.getId().equals(id)).findFirst().get();
    }

    public User save(User user){
        user.setId(++usersCount);
        UserDaoService.users.add(user);
        return user;
    }
}
