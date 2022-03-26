package com.kiran.league.maker.persist.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.PermissionEntity;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    PermissionEntity findByName(final String name);

    List<PermissionEntity> findByInternalIsFalse();

    PermissionEntity findByIdAndInternalIsFalse(final Long id);

    PermissionEntity findByNameAndInternalIsFalse(final String name);
}
