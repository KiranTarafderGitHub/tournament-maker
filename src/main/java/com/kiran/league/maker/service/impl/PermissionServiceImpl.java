package com.kiran.league.maker.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.exception.DuplicateDataException;
import com.kiran.league.maker.persist.dao.PermissionRepository;
import com.kiran.league.maker.persist.dto.Permission;
import com.kiran.league.maker.persist.entity.PermissionEntity;
import com.kiran.league.maker.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
    
    private final PermissionRepository      permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission getPermissionById(final Long id) {
        PermissionEntity entity = permissionRepository.findByIdAndInternalIsFalse(id);
        if(entity != null) {
            return new Permission(entity);
        }
        return null;
    }

    @Override
    public Permission getPermissionByName(final String name) {
        PermissionEntity entity = permissionRepository.findByNameAndInternalIsFalse(name);
        if(entity != null) {
            return new Permission(entity);
        }
        return null;
    }

    @Override
    public List<Permission> getPermissions() {
        List<PermissionEntity> entities = permissionRepository.findByInternalIsFalse();
        List<Permission> permissions = new ArrayList<>(entities.size());
        entities.forEach(entity -> permissions.add(new Permission(entity)));
        return permissions;
    }

    @Override
    public Permission addPermission(final Permission permission) {
        permission.setId(null);
        PermissionEntity entity = new PermissionEntity(permission);
        try {
            entity = permissionRepository.save(entity);
            permission.setId(entity.getId());
            return permission;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDataException("Permission '"+permission.getName()+"' already present.");
        }
    }

}
