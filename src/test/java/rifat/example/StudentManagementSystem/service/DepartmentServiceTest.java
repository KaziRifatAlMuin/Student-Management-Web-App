package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.repository.DepartmentRepository;
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
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setDescription("CS Department");
    }

    @Test
    void findAll() {
        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("Mathematics");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department, department2));

        List<Department> result = departmentService.findAll();

        assertEquals(2, result.size());
        assertEquals("Computer Science", result.get(0).getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Computer Science", result.get().getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.findById(99L);

        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(99L);
    }

    @Test
    void findByName() {
        when(departmentRepository.findByName("Computer Science")).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findByName("Computer Science");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(departmentRepository, times(1)).findByName("Computer Science");
    }

    @Test
    void save() {
        when(departmentRepository.save(department)).thenReturn(department);

        Department result = departmentService.save(department);

        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void deleteById() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteById(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void existsByName() {
        when(departmentRepository.existsByName("Computer Science")).thenReturn(true);

        boolean result = departmentService.existsByName("Computer Science");

        assertTrue(result);
        verify(departmentRepository, times(1)).existsByName("Computer Science");
    }

    @Test
    void existsByName_NotExists() {
        when(departmentRepository.existsByName("Physics")).thenReturn(false);

        boolean result = departmentService.existsByName("Physics");

        assertFalse(result);
        verify(departmentRepository, times(1)).existsByName("Physics");
    }
}
