package rifat.example.StudentManagementSystem.controller;

import rifat.example.StudentManagementSystem.entity.Student;
import rifat.example.StudentManagementSystem.entity.User;
import rifat.example.StudentManagementSystem.service.StudentService;
import rifat.example.StudentManagementSystem.service.DepartmentService;
import rifat.example.StudentManagementSystem.service.CourseService;
import rifat.example.StudentManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "student/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.findById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("availableCourses", courseService.findAll());
            return "student/view";
        }
        return "redirect:/students";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("departments", departmentService.findAll());
        return "student/form";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, 
                              @RequestParam(required = false) Long departmentId,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String password,
                              RedirectAttributes redirectAttributes) {
        if (departmentId != null) {
            departmentService.findById(departmentId).ifPresent(student::setDepartment);
        }
        
        if (student.getId() == null && username != null && password != null) {
            // Creating new student with user account
            studentService.createStudent(student, username, password);
        } else {
            studentService.save(student);
        }
        
        redirectAttributes.addFlashAttribute("message", "Student saved successfully!");
        return "redirect:/students";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.findById(id);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("departments", departmentService.findAll());
            return "student/form";
        }
        return "redirect:/students";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
        return "redirect:/students";
    }
    
    @PostMapping("/enroll/{studentId}/{courseId}")
    public String enrollInCourse(@PathVariable Long studentId, 
                                  @PathVariable Long courseId,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        // Check if student is enrolling themselves or if teacher is doing it
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean isTeacher = user.getRole().name().equals("ROLE_TEACHER");
            
            if (isTeacher) {
                studentService.enrollInCourse(studentId, courseId);
                redirectAttributes.addFlashAttribute("message", "Student enrolled in course successfully!");
            } else {
                // Student can only enroll themselves
                Optional<Student> currentStudent = studentService.findByUser(user);
                if (currentStudent.isPresent() && currentStudent.get().getId().equals(studentId)) {
                    studentService.enrollInCourse(studentId, courseId);
                    redirectAttributes.addFlashAttribute("message", "Enrolled in course successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only enroll yourself!");
                }
            }
        }
        
        return "redirect:/students/view/" + studentId;
    }
    
    @PostMapping("/unenroll/{studentId}/{courseId}")
    public String unenrollFromCourse(@PathVariable Long studentId, 
                                      @PathVariable Long courseId,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean isTeacher = user.getRole().name().equals("ROLE_TEACHER");
            
            if (isTeacher) {
                studentService.unenrollFromCourse(studentId, courseId);
                redirectAttributes.addFlashAttribute("message", "Student unenrolled from course successfully!");
            } else {
                Optional<Student> currentStudent = studentService.findByUser(user);
                if (currentStudent.isPresent() && currentStudent.get().getId().equals(studentId)) {
                    studentService.unenrollFromCourse(studentId, courseId);
                    redirectAttributes.addFlashAttribute("message", "Unenrolled from course successfully!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "You can only unenroll yourself!");
                }
            }
        }
        
        return "redirect:/students/view/" + studentId;
    }
}
