package rifat.example.StudentManagementSystem.repository;

import rifat.example.StudentManagementSystem.entity.Teacher;
import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByUser(User user);
    List<Teacher> findByDepartment(Department department);
    boolean existsByEmail(String email);
}
