package rifat.example.StudentManagementSystem.repository;

import rifat.example.StudentManagementSystem.entity.Course;
import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
    List<Course> findByDepartment(Department department);
    List<Course> findByTeacher(Teacher teacher);
    boolean existsByCode(String code);
}
