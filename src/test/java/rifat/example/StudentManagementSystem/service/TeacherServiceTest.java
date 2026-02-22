package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.*;
import rifat.example.StudentManagementSystem.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private Department department;
    private User user;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        user = new User();
        user.setId(1L);
        user.setUsername("john_teacher");
        user.setPassword("encoded");
        user.setEmail("john@example.com");
        user.setRole(Role.ROLE_TEACHER);
        user.setEnabled(true);

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john@example.com");
        teacher.setPhone("1234567890");
        teacher.setQualification("PhD");
        teacher.setSpecialization("AI");
        teacher.setDepartment(department);
        teacher.setUser(user);
    }

    @Test
    void findAll() {
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");
        teacher2.setEmail("jane@example.com");

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher, teacher2));

        List<Teacher> result = teacherService.findAll();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Teacher> result = teacherService.findById(99L);

        assertFalse(result.isPresent());
        verify(teacherRepository, times(1)).findById(99L);
    }

    @Test
    void findByEmail() {
        when(teacherRepository.findByEmail("john@example.com")).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("Doe", result.get().getLastName());
        verify(teacherRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    void findByUser() {
        when(teacherRepository.findByUser(user)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findByUser(user);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(teacherRepository, times(1)).findByUser(user);
    }

    @Test
    void findByDepartment() {
        when(teacherRepository.findByDepartment(department)).thenReturn(List.of(teacher));

        List<Teacher> result = teacherService.findByDepartment(department);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(teacherRepository, times(1)).findByDepartment(department);
    }

    @Test
    void save() {
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher result = teacherService.save(teacher);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void createTeacher() {
        Teacher newTeacher = new Teacher();
        newTeacher.setFirstName("New");
        newTeacher.setLastName("Teacher");
        newTeacher.setEmail("new@example.com");

        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("new_teacher");
        newUser.setRole(Role.ROLE_TEACHER);

        when(userService.createUser("new_teacher", "password", "new@example.com", Role.ROLE_TEACHER))
                .thenReturn(newUser);
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        Teacher result = teacherService.createTeacher(newTeacher, "new_teacher", "password");

        assertNotNull(result);
        assertEquals(newUser, result.getUser());
        verify(userService, times(1)).createUser("new_teacher", "password", "new@example.com", Role.ROLE_TEACHER);
        verify(teacherRepository, times(1)).save(newTeacher);
    }

    @Test
    void deleteById() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        doNothing().when(userService).deleteById(1L);
        doNothing().when(teacherRepository).deleteById(1L);

        teacherService.deleteById(1L);

        verify(userService, times(1)).deleteById(1L);
        verify(teacherRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_NoUser() {
        Teacher teacherNoUser = new Teacher();
        teacherNoUser.setId(2L);
        teacherNoUser.setFirstName("No");
        teacherNoUser.setLastName("User");
        teacherNoUser.setEmail("nouser@example.com");

        when(teacherRepository.findById(2L)).thenReturn(Optional.of(teacherNoUser));
        doNothing().when(teacherRepository).deleteById(2L);

        teacherService.deleteById(2L);

        verify(userService, never()).deleteById(anyLong());
        verify(teacherRepository, times(1)).deleteById(2L);
    }

    @Test
    void deleteById_NotFound() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());
        doNothing().when(teacherRepository).deleteById(99L);

        teacherService.deleteById(99L);

        verify(userService, never()).deleteById(anyLong());
        verify(teacherRepository, times(1)).deleteById(99L);
    }

    @Test
    void existsByEmail() {
        when(teacherRepository.existsByEmail("john@example.com")).thenReturn(true);

        boolean result = teacherService.existsByEmail("john@example.com");

        assertTrue(result);
        verify(teacherRepository, times(1)).existsByEmail("john@example.com");
    }

    @Test
    void existsByEmail_NotExists() {
        when(teacherRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        boolean result = teacherService.existsByEmail("unknown@example.com");

        assertFalse(result);
        verify(teacherRepository, times(1)).existsByEmail("unknown@example.com");
    }
}
