package com.thoughtworks.bbs.service;

import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.model.UserRole;

import java.util.List;
import java.util.Map;

public interface UserService {
    User get(long id);

    List<User> getAll();

    ServiceResult<User> save(User user);

    void delete(User user);

    User getByUsername(String username);

    ServiceResult<User> update(User user);

    boolean passwordVerify(User user, String password);

    boolean password(User user, String newpass);

    UserRole getUserRoleById(long id);

    Map<User,String> getAllUsersWithRole();

    void updateUserRole(UserRole userRole);

    void authoriseUser(long userId);

    boolean disable(User user);
}
