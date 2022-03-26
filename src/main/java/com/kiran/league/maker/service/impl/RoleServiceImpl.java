package com.kiran.league.maker.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.exception.DuplicateDataException;
import com.kiran.league.maker.common.exception.InvalidDataException;
import com.kiran.league.maker.persist.dao.PermissionRepository;
import com.kiran.league.maker.persist.dao.RoleRepository;
import com.kiran.league.maker.persist.dto.Permission;
import com.kiran.league.maker.persist.dto.Role;
import com.kiran.league.maker.persist.entity.PermissionEntity;
import com.kiran.league.maker.persist.entity.RoleEntity;
import com.kiran.league.maker.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private static final    Logger                  logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    public static final     String                  SELECT_FROM_USER_ROLE_WHERE_ROLE_ID = "SELECT * FROM user_role WHERE role_id=";

    private final           JdbcTemplate            jdbcTemplate;

    private final           RoleRepository          roleRepository;

    private final           PermissionRepository    permissionRepository;

    @Autowired
    public RoleServiceImpl(JdbcTemplate jdbcTemplate, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Role> getRoles() {
        List<RoleEntity> roleEntities = roleRepository.findByInternalIsFalse();
        List<Role> roles = new ArrayList<>(roleEntities.size());
        roleEntities.forEach(entity -> roles.add(new Role(entity)));
        return roles;
    }

    @Override
    public Role getRoleById(final Long roleId) {
        RoleEntity entity = roleRepository.findByIdAndInternalIsFalse(roleId);
        if(entity != null) {
            Role roleDto = new Role(entity);
            entity.getPermissions().forEach(permissionEntity -> roleDto.addPermission(new Permission(permissionEntity)));
            return roleDto;
        }
        return null;
    }

    @Override
    public Role getRoleByName(final String roleName) {
        logger.debug("Get role by role name = {}", roleName);
        RoleEntity entity = roleRepository.findByNameAndInternalIsFalse(roleName);
        if(entity != null) {
            Role roleDto = new Role(entity);
            entity.getPermissions().forEach(permissionEntity -> roleDto.addPermission(new Permission(permissionEntity)));
            return roleDto;
        }
        return null;
    }

    @Override
    public Role addRole(Role role) {
        role.setId(null);
        try {
            RoleEntity roleEntity = new RoleEntity(role.getName(), role.getDescription());
            if(CollectionUtils.isNotEmpty(role.getPermissions())) {
                populatePermissions(role, roleEntity);
            } else {
                throw new InvalidDataException("Minimum one permission is required.");
            }
            roleRepository.save(roleEntity);
            final Role newlyCreatedRole = new Role(roleEntity);
            roleEntity.getPermissions().forEach(entity -> newlyCreatedRole.addPermission(new Permission(entity)));
            return newlyCreatedRole;
        }catch (DataIntegrityViolationException e) {
            throw new DuplicateDataException("Role ("+role.getName()+") already present.");
        }
    }

    @Override
    public Role updateRoleById(final Long id, final Role role) {
        try {
            RoleEntity storedRole = roleRepository.findByIdAndInternalIsFalse(id);
            if(storedRole == null) {
                throw new InvalidDataException("Could not find role with id - "+id);
            }
            if(!storedRole.getName().equals(role.getName())) {
                throw new InvalidDataException("Role name can't be modified.");
            }
            storedRole.setDescription(role.getDescription());
            populatePermissions(role, storedRole);

            roleRepository.save(storedRole);
            role.setId(storedRole.getId());
            List<Permission> permissions = new ArrayList<>(storedRole.getPermissions().size());
            storedRole.getPermissions().forEach(perm -> permissions.add(new Permission(perm)));
            role.setPermissions(permissions);
            return role;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDataException("RoleEntity "+role.getName()+" already present.");
        }
    }

    @Override
    public Role updateRoleByName(final String name, final Role role) {
        try {
            RoleEntity storedRole = roleRepository.findByNameAndInternalIsFalse(name);
            if(storedRole == null) {
                throw new InvalidDataException("Could not find role with name - "+name);
            }
            if(!storedRole.getName().equals(role.getName())) {
                throw new InvalidDataException("Role name can't be modified.");
            }
            storedRole.setDescription(role.getDescription());
            populatePermissions(role, storedRole);
            roleRepository.save(storedRole);
            role.setId(storedRole.getId());
            List<Permission> permissions = new ArrayList<>(storedRole.getPermissions().size());
            storedRole.getPermissions().forEach(perm -> permissions.add(new Permission(perm)));
            role.setPermissions(permissions);
            return role;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDataException("RoleEntity "+role.getName()+" already present.");
        }
    }

    @Override
    public void deleteRoleById(final Long roleId) {
        RoleEntity storedRoleEntity = roleRepository.findByIdAndInternalIsFalse(roleId);
        if(storedRoleEntity == null) {
            throw new InvalidDataException("Could not find Role id ("+roleId+").");
        }
        List<Map<String, Object>> dataRows = jdbcTemplate.queryForList(SELECT_FROM_USER_ROLE_WHERE_ROLE_ID +storedRoleEntity.getId());
        if(CollectionUtils.isNotEmpty(dataRows)) {
            throw new InvalidDataException("Role "+storedRoleEntity.getName()+" ("+roleId+") is in use.");
        }
        roleRepository.delete(storedRoleEntity);
    }

    @Override
    public void deleteRoleByName(final String name) {
        RoleEntity storedRoleEntity = roleRepository.findByNameAndInternalIsFalse(name);
        if(storedRoleEntity == null) {
            throw new InvalidDataException("Could not find Role ("+name+").");
        }
        List<Map<String, Object>> dataRows = jdbcTemplate.queryForList(SELECT_FROM_USER_ROLE_WHERE_ROLE_ID +storedRoleEntity.getId());
        if(CollectionUtils.isNotEmpty(dataRows)) {
            throw new InvalidDataException("Role with name ("+name+") is in use.");
        }
        roleRepository.delete(storedRoleEntity);
    }

    private void populatePermissions(Role role, RoleEntity roleEntity) {
        List<PermissionEntity> storedAllPermission = permissionRepository.findByInternalIsFalse();
        Map<String, PermissionEntity> permissionByName = new HashMap<>(storedAllPermission.size());
        storedAllPermission.forEach(storedPermission -> permissionByName.put(storedPermission.getName(), storedPermission));
        Set<PermissionEntity> toBeAddedPermissions = new HashSet<>(role.getPermissions().size());
        role.getPermissions().forEach(permissionDto -> {
            PermissionEntity toBeAddedPermission = permissionByName.get(permissionDto.getName());
            if(toBeAddedPermission != null) {
                toBeAddedPermissions.add(toBeAddedPermission);
            }
        });
        if(toBeAddedPermissions.isEmpty()) {
            throw new InvalidDataException("Invalid permission name provided. Minimum one valid permission is required.");
        }
        roleEntity.setPermissions(toBeAddedPermissions);
    }
}
