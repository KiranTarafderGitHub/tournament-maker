package com.kiran.league.maker.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.exception.DuplicateDataException;
import com.kiran.league.maker.common.exception.InvalidDataException;
import com.kiran.league.maker.common.exception.UserNotEnabledException;
import com.kiran.league.maker.persist.dao.RoleRepository;
import com.kiran.league.maker.persist.dao.UserRepository;
import com.kiran.league.maker.persist.dto.User;
import com.kiran.league.maker.persist.entity.RoleEntity;
import com.kiran.league.maker.persist.entity.UserEntity;
import com.kiran.league.maker.service.UserService;

@Service
public class UserServiceImpl implements UserService
{
    private static final Logger       logger = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository      userRepository;

    private final RoleRepository      roleRepository;


    private final HttpServletRequest  request;


    @Autowired
    public UserServiceImpl(final UserRepository userRepository, final RoleRepository roleRepository,
             final HttpServletRequest request)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(final String username)
    {
        final String ip = getClientIP();
        
        UserEntity account = userRepository.findByUsername(username);

        if (account == null)
        {
            logger.warn("NO SUCH USER!!");
            throw new UsernameNotFoundException("User " + username + " has no authorities");
        }

        else if (!account.getEnabled())
        {
            throw new UserNotEnabledException("User (" + username + ") was not enabled");
        }

        account.setLastAccessDate(Calendar.getInstance().getTime());
        userRepository.save(account);
        return new User(account);
    }

    @Override
    public List<User> getUsers()
    {
        logger.debug("Finding users");
        try
        {
	        final List<UserEntity> entities = userRepository.findByInternalIsFalse();
	        logger.info("Total no of internal user found :" + entities.size());
	        final List<User> dtos = new ArrayList<>(entities.size());
	        entities.forEach(entity -> {
	            User dto = new User();
	            BeanUtils.copyProperties(entity, dto);
	            dto.setEnabled(entity.getEnabled());
	            dto.setInternal(entity.getInternal());
	            dto.setPassword(null);
	            dtos.add(dto);
	        });
	       
	        
	        return dtos;
        }
        catch(Exception e)
        {
        	logger.error("Exception occured during fetching users ..", e);
        	return new ArrayList<User>();
        }
        
    }

    @Override
    public List<User> getAdminPortalUsers()
    {
        logger.debug("Finding users");
        try
        {
            final List<UserEntity> entities = userRepository.findAdminPortalUsers();
            logger.info("Total no of admin portal users found :" + entities.size());
            final List<User> dtos = new ArrayList<>(entities.size());
            entities.forEach(entity -> {
                User dto = new User();
                BeanUtils.copyProperties(entity, dto);
                dto.setEnabled(entity.getEnabled());
                dto.setInternal(entity.getInternal());
                dto.setPassword(null);
                dtos.add(dto);
            });
           
            
            return dtos;
        }
        catch(Exception e)
        {
            logger.error("Exception occured during fetching portal users ..", e);
            return new ArrayList<User>();
        }
        
    }
    @Override
    public User getUserById(final Long userId)
    {
        final Optional<UserEntity> storedUserOptional = this.userRepository.findById(userId);
        return storedUserOptional.map(User::new).orElse(null);
    }

    @Override
    public User getUserByUsername(final String username)
    {
        final UserEntity entity = userRepository.findByUsername(username);
        if (entity == null)
        {
            return null;
        }
        return new User(entity);
    }

    @Override
    public User addUser(User user)
    {
        try
        {
            user.setId(null);
            final UserEntity userEntity = new UserEntity(user);
            if (CollectionUtils.isNotEmpty(user.getRoles()))
            {
                List<RoleEntity> storedAllRoles = roleRepository.findAll();
                Map<String, RoleEntity> roleByRoleName = new HashMap<>(storedAllRoles.size());
                storedAllRoles.forEach(storedRole -> roleByRoleName.put(storedRole.getName(), storedRole));
                Set<RoleEntity> toBeAddedRoles = new HashSet<>(user.getRoles().size());
                user.getRoles().forEach(roleDto -> {
                    RoleEntity toBeAddedRole = roleByRoleName.get(roleDto.getName());
                    if (toBeAddedRole != null)
                    {
                        toBeAddedRoles.add(toBeAddedRole);
                    }
                });
                if (toBeAddedRoles.isEmpty())
                {
                    throw new InvalidDataException("Invalid role name provided. Minimum one valid Role is required.");
                }
                userEntity.setRoles(toBeAddedRoles);
            }
            else
            {
                throw new InvalidDataException("Minimum one Role is required.");
            }
            userRepository.save(userEntity);
            return new User(userEntity);
        }
        catch (final DataIntegrityViolationException e)
        {
            throw new DuplicateDataException(e.getMessage());
        }
    }

    @Override
    public User updateUserByUsername(String username, User user)
    {
        if (CollectionUtils.isEmpty(user.getRoles()))
        {
            throw new InvalidDataException("Minimum one role is required.");
        }
        UserEntity storedUser = userRepository.findByUsername(username);
        if (storedUser == null)
        {
            throw new InvalidDataException("No user found.");
        }
        user.setId(storedUser.getId());
        user.setUsername(username);

        if (StringUtils.isBlank(user.getPassword()))
        {
            user.setPassword(storedUser.getPassword());
        }
        UserEntity updatedUserEntity = new UserEntity(user);

        List<RoleEntity> storedAllRoles = roleRepository.findAll();

        Map<String, RoleEntity> roleByRoleName = new HashMap<>(storedAllRoles.size());
        storedAllRoles.forEach(storedRole -> roleByRoleName.put(storedRole.getName(), storedRole));
        Set<RoleEntity> toBeAddedRoles = new HashSet<>(user.getRoles().size());
        user.getRoles().forEach(roleDto -> {
            RoleEntity toBeAddedRole = roleByRoleName.get(roleDto.getName());
            if (toBeAddedRole != null)
            {
                toBeAddedRoles.add(toBeAddedRole);
            }
        });
        if (toBeAddedRoles.isEmpty())
        {
            throw new InvalidDataException("Invalid role name provided. Minimum one valid Role is required.");
        }
        updatedUserEntity.setRoles(toBeAddedRoles);
        userRepository.save(updatedUserEntity);
        return new User(updatedUserEntity);
    }

    @Override
    public User updateUserById(Long id, User user)
    {
        if (CollectionUtils.isEmpty(user.getRoles()))
        {
            throw new InvalidDataException("Minimum one role is required.");
        }
        Optional<UserEntity> storedUserOptional = userRepository.findById(id);
        if (!storedUserOptional.isPresent())
        {
            throw new InvalidDataException("No user found.");
        }
        UserEntity storedUserEntity = storedUserOptional.get();
        user.setId(id);
        if (!storedUserEntity.getUsername().equals(user.getUsername()))
        {
            throw new InvalidDataException("Username can't be modified.");
        }

        if (StringUtils.isBlank(user.getPassword()))
        {
            user.setPassword(storedUserEntity.getPassword());
        }
        UserEntity updatedUserEntity = new UserEntity(user);

        List<RoleEntity> storedAllRoles = roleRepository.findAll();

        Map<String, RoleEntity> roleByRoleName = new HashMap<>(storedAllRoles.size());
        storedAllRoles.forEach(storedRole -> roleByRoleName.put(storedRole.getName(), storedRole));
        Set<RoleEntity> toBeAddedRoles = new HashSet<>(user.getRoles().size());
        user.getRoles().forEach(roleDto -> {
            RoleEntity toBeAddedRole = roleByRoleName.get(roleDto.getName());
            if (toBeAddedRole != null)
            {
                toBeAddedRoles.add(toBeAddedRole);
            }
        });
        if (toBeAddedRoles.isEmpty())
        {
            throw new InvalidDataException("Invalid role name provided. Minimum one valid Role is required.");
        }
        updatedUserEntity.setRoles(toBeAddedRoles);
        userRepository.save(updatedUserEntity);
        return new User(updatedUserEntity);
    }

    @Override
    public void deleteUserById(Long id)
    {
        Optional<UserEntity> entityOptional = userRepository.findById(id);
        if (!entityOptional.isPresent())
        {
            throw new InvalidDataException("Could not find user with id : " + id);
        }
        UserEntity entity = entityOptional.get();
        userRepository.delete(entity);
    }

    @Override
    public void deleteUserByUsername(String username)
    {
        UserEntity entity = userRepository.findByUsername(username);
        if (entity == null)
        {
            throw new InvalidDataException("Could not find user with username : " + username);
        }
        userRepository.delete(entity);
    }

    @Override
    public void updateProfile(final User user)
    {
        final Optional<UserEntity> storedUser = userRepository.findById(user.getId());
        if (storedUser.isPresent())
        {
            final UserEntity storedUserEntity = storedUser.get();
            storedUserEntity.setPassword(user.getPassword());
            storedUserEntity.setLastName(user.getLastName());
            storedUserEntity.setFirstName(user.getFirstName());
            userRepository.save(storedUserEntity);
        }
        else
        {
            throw new InvalidDataException("Could not find user with user name '" + user.getUsername() + "'");
        }
    }

    private String getClientIP()
    {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null)
        {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }



   

}
