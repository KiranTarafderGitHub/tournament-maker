package com.kiran.league.maker.persist.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kiran.league.maker.persist.entity.UserEntity;
import com.kiran.league.maker.persist.entity.UserType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails, Serializable{

    private static final long            serialVersionUID   = 1L;

    private Long                         id;

    private String                       username;

    private String                       password;

    private UserType                     userType;

    private String                       firstName;

    private String                       lastName;

    private String                       email;

    private String                       phone;

    private Date                         lastAccessDate;


    private Date                         createdOn;

    private String                       createdBy;

    private Date                         updatedOn;

    private String                       updatedBy;

    private boolean                      enabled;

    @JsonIgnore
    private boolean                      internal;

    private List<Role>                   roles;

    private Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();


    public User() {

    }

    public User(final String username, final String password, final String firstName, final String lastName, final String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        
    }

    public User(final UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userType = user.getUserType();
        this.email = user.getEmail();
        this.lastAccessDate = user.getLastAccessDate();
        this.createdOn = user.getCreatedOn();
        this.createdBy = user.getCreatedBy();
        this.updatedOn = user.getUpdatedOn();
        this.updatedBy = user.getUpdatedBy();
        this.enabled = user.getEnabled();
        this.phone = user.getPhone();
        
        this.roles = new ArrayList<>();
        user.getRoles().forEach(role -> this.roles.add(new Role(role)));
        user.getAuthorities().forEach(authority -> {
            final GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
            grantedAuthorities.add(grantedAuthority);
        });
       
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserType getUserType()
    {
        return userType;
    }

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }

   

   
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(final Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

   

    public Date getCreatedOn() {
        return createdOn;
    }

    private void setCreatedOn(final Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    private void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

   
    public boolean getInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void addRole(final Role role) {
        if(roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }
    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    
    
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", userType=" + userType
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phone=" + phone
				+ ", lastAccessDate=" + lastAccessDate + ", createdOn=" + createdOn + ", createdBy=" + createdBy
				+ ", updatedOn=" + updatedOn + ", updatedBy=" + updatedBy + ", enabled=" + enabled + ", internal="
				+ internal + ", roles=" + roles + ", grantedAuthorities=" + grantedAuthorities + "]";
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return grantedAuthorities;
    }

    /**
     * This method will only be used for REST end point. where we don't want to send grantedAuthorities. But Roles info will be provided.
     * @param grantedAuthorities
     */
    public void setGrantedAuthorities(Collection<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public void addAuthority(GrantedAuthority grantedAuthority)
    {
        grantedAuthorities.add(grantedAuthority);
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

   
}

