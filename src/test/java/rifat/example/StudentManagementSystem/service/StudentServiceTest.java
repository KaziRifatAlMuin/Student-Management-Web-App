package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.*;
import rifat.example.StudentManagementSystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Department department;
    private User user;
    private Course course;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        user = new User();
        user.setId(1L);
        user.setUsername("student1");
        user.setPassword("encoded");
        user.setEmail("student@example.com");
        user.setRole(Role.ROLE_STUDENT);
        user.setEnabled(true);

        course = new Course();
        course.setId(1L);
        course.setName("Data Structures");
        course.setCode("CS101");

        student = new Student();
        student.setId(1L);
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setEmail("alice@example.com");
        student.setPhone("1234567890");
        student.setStudentId("STU001");
        student.setYear(2);
        student.setDepartment(department);
        student.setUser(user);
        student.setCourses(new ArrayList<>());
    }

    @Test
    void findAll() {
        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Bob");
        student2.setLastName("Smith");
        student2.setEmail("bob@example.com");

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student, student2));

        List<Student> result = studentService.findAll();

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findById(99L);

        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    void findByEmail() {
        when(studentRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findByEmail("alice@example.com");

        assertTrue(result.isPresent());
        assertEquals("Johnson", result.get().getLastName());
        verify(studentRepository, times(1)).findByEmail("alice@example.com");
    }

    @Test
    void findByStudentId() {
        when(studentRepository.findByStudentId("STU001")).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findByStudentId("STU001");

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
        verify(studentRepository, times(1)).findByStudentId("STU001");
    }

    @Test
    void findByUser() {
        when(studentRepository.findByUser(user)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findByUser(user);

        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
        verify(studentRepository, times(1)).findByUser(user);
    }

    @Test
    void findByDepartment() {
        when(studentRepository.findByDepartment(department)).thenReturn(List.of(student));

        List<Student> result = studentService.findByDepartment(department);

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        verify(studentRepository, times(1)).findByDepartment(department);
    }

    @Test
    void save() {
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.save(student);

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void createStudent() {
        Student newStudent = new Student();
        newStudent.setFirstName("New");
        newStudent.setLastName("Student");
        newStudent.setEmail("new@example.com");

        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("new_student");
        newUser.setRole(Role.ROLE_STUDENT);

        when(userService.createUser("new_student", "password", "new@example.com", Role.ROLE_STUDENT))
                .thenReturn(newUser);
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        Student result = studentService.createStudent(newStudent, "new_student", "password");

        assertNotNull(result);
        assertEquals(newUser, result.getUser());
        verify(userService, times(1)).createUser("new_student", "password", "new@example.com", Role.ROLE_STUDENT);
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void deleteById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(userService).deleteById(1L);
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteById(1L);

        verify(userService, times(1)).deleteById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_NoUser() {
        Student studentNoUser = new Student();
        studentNoUser.setId(2L);
        studentNoUser.setFirstName("No");
        studentNoUser.setLastName("User");
        studentNoUser.setEmail("nouser@example.com");

        when(studentRepository.findById(2L)).thenReturn(Optional.of(studentNoUser));
        doNothing().when(studentRepository).deleteById(2L);

        studentService.deleteById(2L);

        verify(userService, never()).deleteById(anyLong());
        verify(studentRepository, times(1)).deleteById(2L);
    }

    @Test
    void deleteById_NotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        doNothing().when(studentRepository).deleteById(99L);

        studentService.deleteById(99L);

        verify(userService, never()).deleteById(anyLong());
        verify(studentRepository, times(1)).deleteById(99L);
    }

    @Test
    void enrollInCourse() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseService.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(student)).thenReturn(student);

        studentService.enrollInCourse(1L, 1L);

        assertTrue(student.getCourses().contains(course));
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void enrollInCourse_AlreadyEnrolled() {
        student.getCourses().add(course);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseService.findById(1L)).thenReturn(Optional.of(course));

        studentService.enrollInCourse(1L, 1L);

        assertEquals(1, student.getCourses().size());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void enrollInCourse_StudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        when(courseService.findById(1L)).thenReturn(Optional.of(course));

        studentService.enrollInCourse(99L, 1L);

        verify(studentRepository, never()).save(any());
    }

    @Test
    void enrollInCourse_CourseNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseService.findById(99L)).thenReturn(Optional.empty());

        studentService.enrollInCourse(1L, 99L);

        verify(studentRepository, never()).save(any());
    }

    @Test
    void unenrollFromCourse() {
        student.getCourses().add(course);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseService.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(student)).thenReturn(student);

        studentService.unenrollFromCourse(1L, 1L);

        assertFalse(student.getCourses().contains(course));
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void unenrollFromCourse_NotEnrolled() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseService.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(student)).thenReturn(student);

        studentService.unenrollFromCourse(1L, 1L);

        assertTrue(student.getCourses().isEmpty());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void existsByEmail() {
        when(studentRepository.existsByEmail("alice@example.com")).thenReturn(true);

        boolean result = studentService.existsByEmail("alice@example.com");

        assertTrue(result);
        verify(studentRepository, times(1)).existsByEmail("alice@example.com");
    }

    @Test
    void existsByEmail_NotExists() {
        when(studentRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        boolean result = studentService.existsByEmail("unknown@example.com");

        assertFalse(result);
        verify(studentRepository, times(1)).existsByEmail("unknown@example.com");
    }

    @Test
    void existsByStudentId() {
        when(studentRepository.existsByStudentId("STU001")).thenReturn(true);

        boolean result = studentService.existsByStudentId("STU001");

        assertTrue(result);
        verify(studentRepository, times(1)).existsByStudentId("STU001");
    }

    @Test
    void existsByStudentId_NotExists() {
        when(studentRepository.existsByStudentId("STU999")).thenReturn(false);

        boolean result = studentService.existsByStudentId("STU999");

        assertFalse(result);
        verify(studentRepository, times(1)).existsByStudentId("STU999");
    }
}
