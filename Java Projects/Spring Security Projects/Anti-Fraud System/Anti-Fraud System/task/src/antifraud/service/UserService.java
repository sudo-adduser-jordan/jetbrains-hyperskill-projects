package antifraud.service;


import antifraud.model.User;
import antifraud.model.UserRepository;
import antifraud.request.UserRequest;
import antifraud.response.UserResponse;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> addNewUser(UserRequest userRequest) {
        Optional<User> userFromDb = userRepository.findByUsername(userRequest.getUsername());
        if (userFromDb.isPresent()) {
            return Optional.empty();
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return Optional.of(userRepository.save(user));
    }

    public List<UserResponse> getAllUsersAscending() {
        List<UserResponse> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(new UserResponse(user.getId(), user.getName(), user.getUsername())));
        users.sort(Comparator.naturalOrder());
        return users;
    }

    @Transactional
    public boolean deleteUser(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            return false;
        }
        userRepository.delete(byUsername.get());
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with provided username not found."));
    }
}