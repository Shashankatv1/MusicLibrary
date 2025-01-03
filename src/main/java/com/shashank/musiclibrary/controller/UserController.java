package com.shashank.musiclibrary.controller;

import com.shashank.musiclibrary.exception.ForbiddenAccessException;
import com.shashank.musiclibrary.exception.UnauthorizedAccessException;
import com.shashank.musiclibrary.exception.UserAlreadyExistsException;
import com.shashank.musiclibrary.model.PasswordRequest;
import com.shashank.musiclibrary.model.User;
import com.shashank.musiclibrary.service.JWTService;
import com.shashank.musiclibrary.service.UserService;

import com.shashank.musiclibrary.myutil.LoginResponse;
import com.shashank.musiclibrary.myutil.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private JWTService jwtService;

	@GetMapping("/logout")
	public ResponseEntity<ApiResponse<?>> logout(@RequestHeader(value = "Authorization", required = false) String token) {
		// Check if the Authorization header is present and properly formatted
		if (token == null || !token.startsWith("Bearer ")) {
			// Return Bad Request response if the token is missing or invalid
			return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
		}

		// Extract the actual token
		String actualToken = token.substring(7);

		// Invalidate the token (e.g., add to blacklist)
		jwtService.invalidateToken(actualToken);

		// Clear the authentication context
		SecurityContextHolder.clearContext();

		// Return success response
		return new ResponseEntity<>(new ApiResponse<>(200, "logged out successfully.",null), HttpStatus.CREATED);
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
		try {
			userService.createUser(user);
			return new ResponseEntity<>(new ApiResponse<>(201, "User created successfully",null), HttpStatus.CREATED);
		} catch (UserAlreadyExistsException e) {
			return new ResponseEntity<>(new ApiResponse<>(409, null, "Email already exists"), HttpStatus.CONFLICT);
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiResponse<>(500, null, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-user")
	public ResponseEntity<ApiResponse<User>> addUser(@RequestBody User user) {
		try {
			userService.createUser(user);
			return new ResponseEntity<>(new ApiResponse<>(201,"User created successfully.",null), HttpStatus.CREATED);
		} catch (UserAlreadyExistsException e) {
			return new ResponseEntity<>(new ApiResponse<>(409, null, "Email already exists"), HttpStatus.CONFLICT);
		} catch (UnauthorizedAccessException e) {
			return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
		} catch (ForbiddenAccessException e) {
			return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody User user) {
		try {
			boolean isAuthenticated = userService.authenticateUser(user);
			if (isAuthenticated) {
				String token = jwtService.generateToken(user.getEmail());
				return new ResponseEntity<>(new ApiResponse<>(200, new LoginResponse(token), "Login successful"), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ApiResponse<>(401, null, "Invalid credentials"), HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiResponse<>(401, null, "Authentication failed"), HttpStatus.UNAUTHORIZED);
		}
	}

	// Get user details by ID
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<User>> getUser(@PathVariable UUID userId) {
		return userService.getUserByID(userId)
				.map(foundUser -> new ResponseEntity<>(new ApiResponse<>(200, foundUser, "User retrieved successfully"), HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(new ApiResponse<>(404, null, "User not found"), HttpStatus.NOT_FOUND));
	}

	// Get all users (Only Admins should be allowed)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
		try {
			List<User> users = userService.getAllUsers();
			return new ResponseEntity<>(new ApiResponse<>(200, users, "Users retrieved successfully"), HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access/Operation not allowed"), HttpStatus.FORBIDDEN);
		}
	}

	// Update user role (Only Admins should be allowed)
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}")
	public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable UUID userId, @RequestBody User user) {
		User updatedUser = userService.updateUserRole(userId, user.getRole());
		return updatedUser != null
				? new ResponseEntity<>(new ApiResponse<>(200, updatedUser, "User role updated successfully"), HttpStatus.OK)
				: new ResponseEntity<>(new ApiResponse<>(404, null, "User not found"), HttpStatus.NOT_FOUND);
	}

	// Delete user (Only Admins should be allowed)
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
		try {
			boolean isDeleted = userService.deleteUser(userId);
			if (isDeleted) {
				return new ResponseEntity<>(new ApiResponse<>(200, null, "User deleted successfully"), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ApiResponse<>(404, null, "User not found"), HttpStatus.NOT_FOUND);
			}
		} catch (UnauthorizedAccessException e) {
			return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
		} catch (ForbiddenAccessException e) {
			return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/update-password")
	public ResponseEntity<ApiResponse<String>> updatePassword(@RequestBody PasswordRequest request) {
		try {
			boolean isUpdated = userService.updatePassword(request.getUserId(),request.getOldPassword(),request.getNewPassword());
			if (isUpdated) {
				return new ResponseEntity<>(new ApiResponse<>(200, null, "Password updated successfully"), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ApiResponse<>(404, null, "User not found"), HttpStatus.NOT_FOUND);
			}
		} catch (UnauthorizedAccessException e) {
			return new ResponseEntity<>(new ApiResponse<>(401, null, "Unauthorized Access"), HttpStatus.UNAUTHORIZED);
		} catch (ForbiddenAccessException e) {
			return new ResponseEntity<>(new ApiResponse<>(403, null, "Forbidden Access"), HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiResponse<>(400, null, "Bad Request"), HttpStatus.BAD_REQUEST);
		}
	}


}
