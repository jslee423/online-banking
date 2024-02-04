package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.User;
import com.synergisticit.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {
	
	@Autowired UserRepository userRepository;
	@Autowired BCryptPasswordEncoder encoder;

	@Override
	public User saveUser(User user) {
		String encryptedPasswored = encoder.encode(user.getPassword());
		user.setPassword(encryptedPasswored);
		
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findById(Long userId) {
		Optional<User> optUser = userRepository.findById(userId);
		if (optUser.isPresent()) {
			return optUser.get();
		}
		return null;
	}

	@Override
	public void deleteById(Long userId) {
		userRepository.deleteById(userId);

	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

}
