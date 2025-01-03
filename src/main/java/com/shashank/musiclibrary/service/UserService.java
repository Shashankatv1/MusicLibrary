package com.shashank.musiclibrary.service;//package com.shashank.MusicLibrary.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.shashank.musiclibrary.exception.InvalidPasswordException;
import com.shashank.musiclibrary.exception.PasswordReuseException;
import com.shashank.musiclibrary.exception.UserNotFoundException;
import com.shashank.musiclibrary.model.User;
import com.shashank.musiclibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JWTService jwtService;


    // Authenticate User
	public boolean authenticateUser(User user) {
		Authentication auth = authManager.
				authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        return auth.isAuthenticated();
	}

	//Create a new User
	public User createUser(User user) {
		if (userRepository.count() == 0) {
			user.setRole(User.Role.ADMIN);
		} else if (user.getRole() == null || user.getRole() == User.Role.VIEWER) {  // Check if the role was not passed in the request
				user.setRole(User.Role.VIEWER); // Set to VIEWER if no role was passed
		}

		user.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	//get all User
	public List<User> getAllUsers() {
		if (isAdmin()) {
			return userRepository.findAll(); // Admin can view all users
		} else {
			throw new RuntimeException("Access Denied");
		}
	}

	//Get a User By ID
	public Optional<User> getUserByID(UUID userID) {
		return userRepository.findById(userID);
	}

	public User updateUserRole(UUID userId, User.Role newRole) {
		if (!isAdmin()) {
			throw new RuntimeException("Access Denied");
		}

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// Prevent Admin from changing other Admin's roles
		if (user.getRole() == User.Role.ADMIN && !getCurrentUser().getUsername().equals(user.getEmail())) {
			throw new RuntimeException("Cannot change another Admin's role");
		}

		user.setRole(newRole);
		return userRepository.save(user);
	}

	// Delete user (Only Admins can delete users)
	public boolean deleteUser(UUID userId) {
		if (isAdmin()) {
			userRepository.deleteById(userId);
			return true;
		} else {
			throw new RuntimeException("Access Denied");
		}
	}
	// Helper method to check if current user is Admin
	private boolean isAdmin() {
		UserDetails currentUser = getCurrentUser();
		return currentUser != null && currentUser.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
	}

	// Helper method to get the current authenticated user
	private UserDetails getCurrentUser() {
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public boolean updatePassword(UUID userId, String oldPassword, String newPassword) {
		Optional<User> userOptional = userRepository.findById(userId);

		if (!userOptional.isPresent()) {
			throw new UserNotFoundException("User not found");
		}

		User user = userOptional.get();

		// Verify if the old password matches the stored password
		if (!encoder.matches(oldPassword, user.getPassword())) {
			throw new InvalidPasswordException("Incorrect current password");
		}

		// Check if the new password is the same as the old password
		if (encoder.matches(newPassword, user.getPassword())) {
			throw new PasswordReuseException("New password cannot be the same as the old password");
		}

		// Encrypt and set the new password
		user.setPassword(encoder.encode(newPassword));
		userRepository.save(user);
		return true;  // Successfully updated password
	}
}
