package com.kiran.league.maker.service;

import java.util.List;

import com.kiran.league.maker.persist.dto.Permission;

public interface PermissionService {

    Permission getPermissionById(Long id);

    Permission getPermissionByName(String name);

    List<Permission> getPermissions();

    Permission addPermission(Permission permission);

}
