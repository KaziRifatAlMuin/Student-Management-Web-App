package rifat.example.StudentManagementSystem.repository;

import rifat.example.StudentManagementSystem.entity.Student;
import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByUser(User user);
    List<Student> findByDepartment(Department department);
    boolean existsByEmail(String email);
    boolean existsByStudentId(String studentId);
}
