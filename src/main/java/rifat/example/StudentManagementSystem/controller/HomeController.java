package rifat.example.StudentManagementSystem.controller;

import rifat.example.StudentManagementSystem.entity.User;
import rifat.example.StudentManagementSystem.entity.Student;
import rifat.example.StudentManagementSystem.entity.Teacher;
import rifat.example.StudentManagementSystem.service.UserService;
import rifat.example.StudentManagementSystem.service.StudentService;
import rifat.example.StudentManagementSystem.service.TeacherService;
import rifat.example.StudentManagementSystem.service.CourseService;
import rifat.example.StudentManagementSystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

@Controller
public class HomeController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            model.addAttribute("role", user.getRole().name());
            
            // Add statistics
            model.addAttribute("studentCount", studentService.findAll().size());
            model.addAttribute("teacherCount", teacherService.findAll().size());
            model.addAttribute("courseCount", courseService.findAll().size());
            model.addAttribute("departmentCount", departmentService.findAll().size());
            
            // Get profile info based on role
            if (user.getRole().name().equals("ROLE_STUDENT")) {
                Optional<Student> student = studentService.findByUser(user);
                student.ifPresent(s -> model.addAttribute("profile", s));
            } else if (user.getRole().name().equals("ROLE_TEACHER")) {
                Optional<Teacher> teacher = teacherService.findByUser(user);
                teacher.ifPresent(t -> model.addAttribute("profile", t));
            }
        }
        
        return "dashboard";
    }
}
