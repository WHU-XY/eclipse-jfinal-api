package org.zhangfc.demo4ssh.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7425304725239042741L;
	private int id;
	private String role;
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
