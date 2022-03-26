package com.kiran.league.maker.persist.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.kiran.league.maker.persist.dto.Permission;

@Entity
@Table(name = "permission")
public class PermissionEntity implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private boolean internal;

	public PermissionEntity() {
	}

	public PermissionEntity(String name) {
		this.name = name;
	}

	public PermissionEntity(Permission permission) {
		this.name = permission.getName();
		this.description = permission.getDescription();
		this.id = permission.getId();
	}

	public PermissionEntity(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	@Override
	public String getAuthority() {
		return getName();
	}

	@Override
	public String toString() {
		return "PermissionEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}