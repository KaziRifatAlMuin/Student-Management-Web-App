package rifat.example.StudentManagementSystem.config;

import rifat.example.StudentManagementSystem.entity.*;
import rifat.example.StudentManagementSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (userRepository.count() == 0) {
            initializeData();
        }
    }
    
    private void initializeData() {
        // Create Departments
        Department csDept = new Department();
        csDept.setName("Computer Science");
        csDept.setDescription("Department of Computer Science and Engineering");
        departmentRepository.save(csDept);
        
        Department mathDept = new Department();
        mathDept.setName("Mathematics");
        mathDept.setDescription("Department of Mathematics and Statistics");
        departmentRepository.save(mathDept);
        
        Department physicsDept = new Department();
        physicsDept.setName("Physics");
        physicsDept.setDescription("Department of Physics");
        departmentRepository.save(physicsDept);
        
        // Create Teacher User and Profile
        User teacherUser = new User();
        teacherUser.setUsername("teacher");
        teacherUser.setPassword(passwordEncoder.encode("password"));
        teacherUser.setEmail("teacher@example.com");
        teacherUser.setRole(Role.ROLE_TEACHER);
        teacherUser.setEnabled(true);
        userRepository.save(teacherUser);
        
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Smith");
        teacher.setEmail("teacher@example.com");
        teacher.setPhone("123-456-7890");
        teacher.setQualification("PhD in Computer Science");
        teacher.setSpecialization("Software Engineering");
        teacher.setDepartment(csDept);
        teacher.setUser(teacherUser);
        teacherRepository.save(teacher);
        
        // Create another teacher
        User teacherUser2 = new User();
        teacherUser2.setUsername("professor");
        teacherUser2.setPassword(passwordEncoder.encode("password"));
        teacherUser2.setEmail("professor@example.com");
        teacherUser2.setRole(Role.ROLE_TEACHER);
        teacherUser2.setEnabled(true);
        userRepository.save(teacherUser2);
        
        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Sarah");
        teacher2.setLastName("Johnson");
        teacher2.setEmail("professor@example.com");
        teacher2.setPhone("987-654-3210");
        teacher2.setQualification("PhD in Mathematics");
        teacher2.setSpecialization("Applied Mathematics");
        teacher2.setDepartment(mathDept);
        teacher2.setUser(teacherUser2);
        teacherRepository.save(teacher2);
        
        // Create Student User and Profile
        User studentUser = new User();
        studentUser.setUsername("student");
        studentUser.setPassword(passwordEncoder.encode("password"));
        studentUser.setEmail("student@example.com");
        studentUser.setRole(Role.ROLE_STUDENT);
        studentUser.setEnabled(true);
        userRepository.save(studentUser);
        
        Student student = new Student();
        student.setFirstName("Alice");
        student.setLastName("Brown");
        student.setEmail("student@example.com");
        student.setPhone("555-123-4567");
        student.setStudentId("STU001");
        student.setYear(2);
        student.setDepartment(csDept);
        student.setUser(studentUser);
        studentRepository.save(student);
        
        // Create another student
        User studentUser2 = new User();
        studentUser2.setUsername("student2");
        studentUser2.setPassword(passwordEncoder.encode("password"));
        studentUser2.setEmail("student2@example.com");
        studentUser2.setRole(Role.ROLE_STUDENT);
        studentUser2.setEnabled(true);
        userRepository.save(studentUser2);
        
        Student student2 = new Student();
        student2.setFirstName("Bob");
        student2.setLastName("Wilson");
        student2.setEmail("student2@example.com");
        student2.setPhone("555-987-6543");
        student2.setStudentId("STU002");
        student2.setYear(3);
        student2.setDepartment(mathDept);
        student2.setUser(studentUser2);
        studentRepository.save(student2);
        
        // Create Courses
        Course course1 = new Course();
        course1.setCode("CS101");
        course1.setName("Introduction to Programming");
        course1.setDescription("Learn the basics of programming with Java");
        course1.setCredits(3);
        course1.setDepartment(csDept);
        course1.setTeacher(teacher);
        courseRepository.save(course1);
        
        Course course2 = new Course();
        course2.setCode("CS201");
        course2.setName("Data Structures");
        course2.setDescription("Learn about arrays, linked lists, trees, and graphs");
        course2.setCredits(4);
        course2.setDepartment(csDept);
        course2.setTeacher(teacher);
        courseRepository.save(course2);
        
        Course course3 = new Course();
        course3.setCode("MATH101");
        course3.setName("Calculus I");
        course3.setDescription("Introduction to differential and integral calculus");
        course3.setCredits(4);
        course3.setDepartment(mathDept);
        course3.setTeacher(teacher2);
        courseRepository.save(course3);
        
        Course course4 = new Course();
        course4.setCode("MATH201");
        course4.setName("Linear Algebra");
        course4.setDescription("Vectors, matrices, and linear transformations");
        course4.setCredits(3);
        course4.setDepartment(mathDept);
        course4.setTeacher(teacher2);
        courseRepository.save(course4);
        
        Course course5 = new Course();
        course5.setCode("PHY101");
        course5.setName("Physics I");
        course5.setDescription("Mechanics and thermodynamics");
        course5.setCredits(4);
        course5.setDepartment(physicsDept);
        courseRepository.save(course5);
        
        // Enroll students in courses
        student.getCourses().add(course1);
        student.getCourses().add(course3);
        studentRepository.save(student);
        
        student2.getCourses().add(course3);
        student2.getCourses().add(course4);
        studentRepository.save(student2);
        
        System.out.println("===========================================");
        System.out.println("Demo data initialized successfully!");
        System.out.println("===========================================");
        System.out.println("Demo Accounts:");
        System.out.println("  Teacher: username=teacher, password=password");
        System.out.println("  Teacher: username=professor, password=password");
        System.out.println("  Student: username=student, password=password");
        System.out.println("  Student: username=student2, password=password");
        System.out.println("===========================================");
    }
}
