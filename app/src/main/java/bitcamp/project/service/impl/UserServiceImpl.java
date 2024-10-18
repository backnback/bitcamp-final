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
    public boolean update(int no, User user) throws Exception {
        return userDao.update(no, user);
    }

    @Override
    public User findUser(int no) throws Exception {
        return userDao.findUser(no);
    }

    @Transactional
    @Override
    public boolean delete(int no) throws Exception {
        return userDao.delete(no);
    }

}
