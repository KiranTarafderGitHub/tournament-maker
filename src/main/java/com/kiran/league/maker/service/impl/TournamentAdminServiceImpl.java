package com.kiran.league.maker.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kiran.league.maker.common.exception.NoDataFoundException;
import com.kiran.league.maker.common.util.RandomGenerator;
import com.kiran.league.maker.persist.dao.RoleRepository;
import com.kiran.league.maker.persist.dao.TournamentAdminRepository;
import com.kiran.league.maker.persist.dao.TournamentRepository;
import com.kiran.league.maker.persist.dao.UserRepository;
import com.kiran.league.maker.persist.dto.Role;
import com.kiran.league.maker.persist.entity.RoleEntity;
import com.kiran.league.maker.persist.entity.Tournament;
import com.kiran.league.maker.persist.entity.TournamentAdmin;
import com.kiran.league.maker.persist.entity.UserEntity;
import com.kiran.league.maker.persist.entity.UserType;
import com.kiran.league.maker.service.RoleService;
import com.kiran.league.maker.service.TournamentAdminService;
import com.kiran.league.maker.service.TournamnetService;

@Service
public class TournamentAdminServiceImpl implements TournamentAdminService {

	private static final Log log = LogFactory.getLog(TournamentAdminService.class);
	
	@Autowired
	TournamentRepository  tournamentRepository;
	
	@Value("${user.admin.default.password}")
	String defaultAdminPassword;
	
	@Autowired
	TournamentAdminRepository tournamentAdminRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Override
	public UserEntity creteAdminUserForTournament(Tournament tournament) {

		UserEntity user = new UserEntity();
		user.setUsername(RandomGenerator.generateRandomAlphaNumericString(8).toUpperCase());
		user.setPassword(encoder.encode(defaultAdminPassword));
		user.setUserType(UserType.LEAGUE_ADMIN);
		user.setFirstName("League");
		user.setLastName("Admin");
		user.setEmail(tournament.getAdminEmail());
		user.setCreatedBy("System");
		user.setEnabled(true);
		
		RoleEntity role =  roleRepository.findByName(UserType.LEAGUE_ADMIN.toString().toLowerCase());
		Set<RoleEntity> roleSet = new HashSet<>();
		roleSet.add(role);
		
		user.setRoles(roleSet);
		userRepository.saveAndFlush(user);
		
		TournamentAdmin tournamentAdmin = new TournamentAdmin();
		tournamentAdmin.setUserId(user.getId());
		tournamentAdmin.setTournamentId(tournament.getId());
		
		tournamentAdminRepository.save(tournamentAdmin);
		
		return user;
	}

	@Override
	public Tournament getTournamentForUser(UserEntity user) {
		
		TournamentAdmin tournamentAdmin =  tournamentAdminRepository.findByUserId(user.getId());
		
		Optional<Tournament> tournamentOptional= tournamentRepository.findById(tournamentAdmin.getTournamentId());
		if(tournamentOptional.isPresent())
			return tournamentOptional.get();
		else
			throw new NoDataFoundException("Could not find Tournamnet for user");
	}

}
