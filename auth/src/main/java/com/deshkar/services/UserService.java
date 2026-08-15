package com.deshkar.services;

import com.deshkar.model.Users;

import java.util.List;

public interface UserService {

    List<Users> getAll();
    Users getUserById(String id);
    Users createUser(Users user);
    int employeeCount();
    Users deactivateUser(String id);
    List<Users> getAllEmployee();
    Users updateEmployee(String id, Users updatedUser);

}
