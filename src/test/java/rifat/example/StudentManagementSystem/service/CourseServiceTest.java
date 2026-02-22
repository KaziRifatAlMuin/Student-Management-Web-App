package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.Course;
import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.entity.Teacher;
import rifat.example.StudentManagementSystem.repository.CourseRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Department department;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setEmail("john.doe@example.com");

        course = new Course();
        course.setId(1L);
        course.setName("Data Structures");
        course.setCode("CS101");
        course.setDescription("Introduction to Data Structures");
        course.setCredits(3);
        course.setDepartment(department);
        course.setTeacher(teacher);
    }

    @Test
    void findAll() {
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Algorithms");
        course2.setCode("CS102");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course, course2));

        List<Course> result = courseService.findAll();

        assertEquals(2, result.size());
        assertEquals("Data Structures", result.get(0).getName());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Optional<Course> result = courseService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("CS101", result.get().getCode());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Course> result = courseService.findById(99L);

        assertFalse(result.isPresent());
        verify(courseRepository, times(1)).findById(99L);
    }

    @Test
    void findByCode() {
        when(courseRepository.findByCode("CS101")).thenReturn(Optional.of(course));

        Optional<Course> result = courseService.findByCode("CS101");

        assertTrue(result.isPresent());
        assertEquals("Data Structures", result.get().getName());
        verify(courseRepository, times(1)).findByCode("CS101");
    }

    @Test
    void findByDepartment() {
        when(courseRepository.findByDepartment(department)).thenReturn(List.of(course));

        List<Course> result = courseService.findByDepartment(department);

        assertEquals(1, result.size());
        assertEquals("CS101", result.get(0).getCode());
        verify(courseRepository, times(1)).findByDepartment(department);
    }

    @Test
    void findByTeacher() {
        when(courseRepository.findByTeacher(teacher)).thenReturn(List.of(course));

        List<Course> result = courseService.findByTeacher(teacher);

        assertEquals(1, result.size());
        assertEquals("CS101", result.get(0).getCode());
        verify(courseRepository, times(1)).findByTeacher(teacher);
    }

    @Test
    void save() {
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.save(course);

        assertNotNull(result);
        assertEquals("Data Structures", result.getName());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void deleteById() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteById(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsByCode() {
        when(courseRepository.existsByCode("CS101")).thenReturn(true);

        boolean result = courseService.existsByCode("CS101");

        assertTrue(result);
        verify(courseRepository, times(1)).existsByCode("CS101");
    }

    @Test
    void existsByCode_NotExists() {
        when(courseRepository.existsByCode("CS999")).thenReturn(false);

        boolean result = courseService.existsByCode("CS999");

        assertFalse(result);
        verify(courseRepository, times(1)).existsByCode("CS999");
    }
}
