package bitcamp.project.service.impl;

import bitcamp.project.dao.UserDao;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Override
    public List<User> list(){
        return userDao.findAll();
    }

    @Transactional
    @Override
    public boolean update(int id, User user) throws Exception {
        return userDao.update(id, user);
    }

    @Override
    public User findUser(int id) throws Exception {
        return userDao.findUser(id);
    }

    @Transactional
    @Override
    public boolean delete(int id) throws Exception {
        return userDao.delete(id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws Exception {
        System.out.println("여기");
        return userDao.findByEmailAndPassword(email, password);
    }

}