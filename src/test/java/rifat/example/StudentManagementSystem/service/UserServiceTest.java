package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.Role;
import rifat.example.StudentManagementSystem.entity.User;
import rifat.example.StudentManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setPassword("encoded_password");
        user.setEmail("john@example.com");
        user.setRole(Role.ROLE_STUDENT);
        user.setEnabled(true);
    }

    @Test
    void findAll() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane_doe");
        user2.setEmail("jane@example.com");
        user2.setRole(Role.ROLE_TEACHER);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        assertEquals("john_doe", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("john_doe", result.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(99L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void findByUsername() {
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("john_doe");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void save_NewUser() {
        User newUser = new User();
        newUser.setUsername("new_user");
        newUser.setPassword("plain_password");
        newUser.setEmail("new@example.com");
        newUser.setRole(Role.ROLE_STUDENT);

        when(passwordEncoder.encode("plain_password")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.save(newUser);

        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode("plain_password");
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void save_ExistingUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser() {
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(2L);
            return savedUser;
        });

        User result = userService.createUser("test_user", "password123", "test@example.com", Role.ROLE_STUDENT);

        assertNotNull(result);
        assertEquals("test_user", result.getUsername());
        assertEquals("encoded_password", result.getPassword());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(Role.ROLE_STUDENT, result.getRole());
        assertTrue(result.isEnabled());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsByUsername() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        boolean result = userService.existsByUsername("john_doe");

        assertTrue(result);
        verify(userRepository, times(1)).existsByUsername("john_doe");
    }

    @Test
    void existsByUsername_NotExists() {
        when(userRepository.existsByUsername("unknown")).thenReturn(false);

        boolean result = userService.existsByUsername("unknown");

        assertFalse(result);
        verify(userRepository, times(1)).existsByUsername("unknown");
    }

    @Test
    void existsByEmail() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        boolean result = userService.existsByEmail("john@example.com");

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }

    @Test
    void existsByEmail_NotExists() {
        when(userRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        boolean result = userService.existsByEmail("unknown@example.com");

        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail("unknown@example.com");
    }
}
