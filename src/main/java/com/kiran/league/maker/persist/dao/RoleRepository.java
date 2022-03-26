package com.kiran.league.maker.persist.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(final String name);

    void deleteByName(final String name);

    List<RoleEntity> findByInternalIsFalse();

    RoleEntity findByIdAndInternalIsFalse(final Long id);

    RoleEntity findByNameAndInternalIsFalse(final String roleName);
}
