package com.deshkar.services.impl;

import com.deshkar.model.Users;
import com.deshkar.repo.UserRepo;
import com.deshkar.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    @Override
    public List<Users> getAll() {
        return userRepo.findAll();
    }

    @Override
    public Users getUserById(String id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not Found!!!!"));
    }

    @Override
    public Users createUser(Users user) {
        return userRepo.save(user);
    }

    @Override
    public int employeeCount() {
        return getAll().size();
    }

    @Override
    public Users deactivateUser(String id) {
//        User user = getUserById(id);
//        user.setActive(false);
//        return userRepo.save(user);

        return null;
    }

    @Override
    public List<Users> getAllEmployee() {
        return userRepo.findAll();
    }

    @Override
    public Users updateEmployee(String id, Users updatedUser) {
        Optional<Users> optionalUser = userRepo.findById(id);

        if (optionalUser.isEmpty()) {
            return null;
        }

        Users existingUser = optionalUser.get();

        // ✅ Update only allowed fields (ignore password & role)
        if (updatedUser.getFirstName() != null)
            existingUser.setFirstName(updatedUser.getFirstName());

        if (updatedUser.getLastName() != null)
            existingUser.setLastName(updatedUser.getLastName());

        if (updatedUser.getEmail() != null)
            existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPhone() != null)
            existingUser.setPhone(updatedUser.getPhone());

        if (updatedUser.getSalary() != null)
            existingUser.setSalary(updatedUser.getSalary());

        // ✅ Active status
        existingUser.setIsActive(updatedUser.getIsActive());

        // ❌ Do not change password or role
        // existingUser.setPassword(existingUser.getPassword());
        // existingUser.setRole(existingUser.getRole());

        return userRepo.save(existingUser);
    }

}
