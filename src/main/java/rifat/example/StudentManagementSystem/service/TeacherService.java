package rifat.example.StudentManagementSystem.service;

import rifat.example.StudentManagementSystem.entity.Teacher;
import rifat.example.StudentManagementSystem.entity.Role;
import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.entity.User;
import rifat.example.StudentManagementSystem.repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private UserService userService;
    
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }
    
    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }
    
    public Optional<Teacher> findByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }
    
    public Optional<Teacher> findByUser(User user) {
        return teacherRepository.findByUser(user);
    }
    
    public List<Teacher> findByDepartment(Department department) {
        return teacherRepository.findByDepartment(department);
    }
    
    @Transactional
    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    
    @Transactional
    public Teacher createTeacher(Teacher teacher, String username, String password) {
        User user = userService.createUser(username, password, teacher.getEmail(), Role.ROLE_TEACHER);
        teacher.setUser(user);
        return teacherRepository.save(teacher);
    }
    
    @Transactional
    public void deleteById(Long id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent() && teacher.get().getUser() != null) {
            userService.deleteById(teacher.get().getUser().getId());
        }
        teacherRepository.deleteById(id);
    }
    
    public boolean existsByEmail(String email) {
        return teacherRepository.existsByEmail(email);
    }
}
