package com.kiran.league.maker.persist.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kiran.league.maker.persist.entity.PermissionEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String description;

	@JsonIgnore
	private boolean internal;

	@JsonIgnore
	private boolean selected;

	public Permission() {
	}

	public Permission(String name) {
		this.name = name;
	}

	public Permission(final PermissionEntity entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
	}

	public Permission(Long id, String name, String description) {
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
	public boolean equals(Object o) {

		if (o == this) return true;
		if (!(o instanceof Permission)) {
			return false;
		}

		Permission perm = (Permission) o;

		return new EqualsBuilder()
				.append(id, perm.id)
				.append(name, perm.name)
				.append(description, perm.description)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(id)
				.append(name)
				.append(description)
				.toHashCode();
	}

	@Override
	public String toString() {
		return "Permission{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
