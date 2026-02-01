package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.Course;
import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.entity.Teacher;
import rifat.example.StudentManagementSystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    public List<Course> findAll() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }
    
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }
    
    public List<Course> findByDepartment(Department department) {
        return courseRepository.findByDepartment(department);
    }
    
    public List<Course> findByTeacher(Teacher teacher) {
        return courseRepository.findByTeacher(teacher);
    }
    
    public Course save(Course course) {
        return courseRepository.save(course);
    }
    
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }
    
    public boolean existsByCode(String code) {
        return courseRepository.existsByCode(code);
    }
}
