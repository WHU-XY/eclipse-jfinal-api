package org.zhangfc.demo4ssh.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zhangfc.demo4ssh.domain.User;
import org.zhangfc.demo4ssh.repo.UserDao;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	public void saveUsers(List<User> us) {
		for (User u : us) {
			userDao.save(u);
		}
	}

	public List<User> getAllUsers() {
		return userDao.findAll();
	}

}
