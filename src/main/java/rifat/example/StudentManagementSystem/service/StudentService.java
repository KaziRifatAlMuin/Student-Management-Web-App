package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.Student;
import rifat.example.StudentManagementSystem.entity.Role;
import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.entity.Course;
import rifat.example.StudentManagementSystem.entity.User;
import rifat.example.StudentManagementSystem.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CourseService courseService;
    
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    
    public Optional<Student> findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    public Optional<Student> findByUser(User user) {
        return studentRepository.findByUser(user);
    }
    
    public List<Student> findByDepartment(Department department) {
        return studentRepository.findByDepartment(department);
    }
    
    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    
    @Transactional
    public Student createStudent(Student student, String username, String password) {
        User user = userService.createUser(username, password, student.getEmail(), Role.ROLE_STUDENT);
        student.setUser(user);
        return studentRepository.save(student);
    }
    
    @Transactional
    public void deleteById(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent() && student.get().getUser() != null) {
            userService.deleteById(student.get().getUser().getId());
        }
        studentRepository.deleteById(id);
    }
    
    @Transactional
    public void enrollInCourse(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseService.findById(courseId);
        
        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();
            if (!student.getCourses().contains(course)) {
                student.getCourses().add(course);
                studentRepository.save(student);
            }
        }
    }
    
    @Transactional
    public void unenrollFromCourse(Long studentId, Long courseId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseService.findById(courseId);
        
        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();
            student.getCourses().remove(course);
            studentRepository.save(student);
        }
    }
    
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
    
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }
}
