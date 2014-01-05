package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.UserMapper;
import com.thoughtworks.bbs.mappers.UserRoleMapper;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.model.UserRole;
import com.thoughtworks.bbs.model.UserValidator;
import com.thoughtworks.bbs.service.ServiceResult;
import com.thoughtworks.bbs.service.UserService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.*;

public class UserServiceImpl implements UserService {
    private UserValidator validator;
    private SqlSessionFactory factory;
    String ROLE_ADMIN = "ROLE_ADMIN";
    String ROLE_REGULAR = "ROLE_REGULAR";

    public UserServiceImpl(SqlSessionFactory factory) {
        this.factory = factory;
        validator = new UserValidator();
    }

    @Override
    public User get(long id) {
        SqlSession session = factory.openSession();
        User user = null;

        try{
            UserMapper mapper = session.getMapper(UserMapper.class);
            user = mapper.findByUserId(id);
        } finally {
            session.close();
        }

        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<User>();
        SqlSession session = factory.openSession();
            try{
                UserMapper userMapper = session.getMapper(UserMapper.class);
                users = userMapper.findAllUsers();
            } finally {
                session.close();
            }

        return users;
    }

    @Override
    public ServiceResult<User> save(User user) {
        Map<String, String> errors = validator.validate(user);
        SqlSession session = factory.openSession();
        if(errors.isEmpty()) {
            try{
                UserMapper userMapper = session.getMapper(UserMapper.class);
                User duplicateUser = userMapper.findByUsername(user.getUserName());
                if(duplicateUser!=null){
                    errors.put("username", "This username <span class='span-name'>"+user.getUserName()+"</span> is so popular, please change another!");
                }else{
                    userMapper.insert(user);
                    UserRoleMapper userRoleMapper = session.getMapper(UserRoleMapper.class);
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getId());
                    userRole.setRoleName(ROLE_REGULAR);
                    userRoleMapper.insert(userRole);
                    session.commit();
                }

            } finally {
                session.close();
            }
        }
        return new ServiceResult<User>(errors, user);
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User getByUsername(String username) {
        SqlSession session = factory.openSession();
        User user = null;

        try{
            UserMapper mapper = session.getMapper(UserMapper.class);
            user = mapper.findByUsername(username);
        } finally {
            session.close();
        }

        return user;
    }

    @Override
    public ServiceResult<User> update(User user) {
        SqlSession session = factory.openSession();
        Map<String, String> errors = validator.validate(user);

        if(errors.isEmpty()) {
            try {
                UserMapper mapper = session.getMapper(UserMapper.class);
                mapper.update(user);
                session.commit();
            }
            finally {
                session.close();
            }
        }
        return new ServiceResult<User>(errors, user);
    }

    @Override
    public boolean passwordVerify(User user, String password) {
        return user.getPasswordHash().equals(password);
    }

    @Override
    public boolean password(User user, String newpass) {
        user.setPasswordHash(newpass);
        if(update(user).hasErrors())
            return false;
        return true;
    }

    @Override
    public UserRole getUserRoleById(long id){
        SqlSession session = factory.openSession();
        UserRole userRole = null;

        try{
             UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
             userRole = mapper.get(id);

        } finally {
            session.close();
        }
        return  userRole;
    }

    @Override
    public Map<User,String> getAllUsersWithRole(){
        List<User> users = getAll();
        Map <User,String> userWithRole= new TreeMap<User,String>(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return (int)(user1.getId()-user2.getId());
            }
        });
        SqlSession session = factory.openSession();

        try{
            for (User user:users){
                UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);
                UserRole userRole = mapper.get(user.getId());
                userWithRole.put(user,userRole.getRoleName());
            }
        } finally {
            session.close();
        }
        return  userWithRole;
    }

    @Override
    public void updateUserRole(UserRole userRole) {
        SqlSession session = factory.openSession();
        ServiceResult<User> serviceResult = null;

            try {
                UserRoleMapper mapper = session.getMapper(UserRoleMapper.class);;
                mapper.update(userRole);
                session.commit();
            }
            finally {
                session.close();
            }
    }

    @Override
    public void authoriseUser(long userId) {
        UserRole userRole = getUserRoleById(userId);
        userRole.setRoleName(ROLE_ADMIN);
        updateUserRole(userRole);
    }

    @Override
    public boolean disable(User user) {
        if( user != null && user.isEnabled()) {
            user.setEnabled(false);
            if(!update(user).hasErrors())
                return true;
        }
        return false;
    }


}
