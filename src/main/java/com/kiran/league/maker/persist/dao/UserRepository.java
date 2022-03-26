package com.kiran.league.maker.persist.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kiran.league.maker.persist.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    @Query("select ue from UserEntity ue where ue.username like CONCAT('%',:name,'%')")
    List<UserEntity> findByMatchingName(@Param("name") String name);

    List<UserEntity> findByInternalIsFalse();
    
    @Query("select ue from UserEntity ue where ue.internal = false and ue.userType in ('ADMINISTRATOR','DELIVERY_ASST')")
    List<UserEntity> findAdminPortalUsers();
    
    @Query(value="SELECT u FROM UserEntity u where u.userType IN ('CLUB_ITC_MEMBER', 'GUEST_USER') AND u.createdOn BETWEEN :startDate AND :endDate ORDER BY u.updatedOn DESC")
    List<UserEntity> findGuestMemberLoginDataByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
