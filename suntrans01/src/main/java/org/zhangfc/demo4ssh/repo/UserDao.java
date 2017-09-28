package org.zhangfc.demo4ssh.repo;

import java.util.List;

import org.zhangfc.demo4ssh.domain.User;

public interface UserDao {
	public int save(User u);
	public List<User> findAll();
}
