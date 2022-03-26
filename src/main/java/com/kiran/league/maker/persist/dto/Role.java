package com.kiran.league.maker.persist.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kiran.league.maker.persist.entity.RoleEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String description;

	@JsonIgnore
	private boolean internal;

	private List<Permission> permissions;

	@JsonIgnore
	private boolean selected;

	public Role() {

	}

	public Role(String name) {
		this.name = name;
	}

	public Role(final RoleEntity role) {
		this.id = role.getId();
		this.name = role.getName();
		this.description = role.getDescription();
	}
	public Role(Long id, String name, String description) {
		this.id = id;
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

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void addPermission(Permission permission) {
		if(permissions == null) {
			permissions = new ArrayList<>();
		}
		permissions.add(permission);
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean getInternal() {
		return internal;
	}

	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	@Override
	public String toString() {
		return "Role{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", permissions=" + permissions +
				'}';
	}
}
