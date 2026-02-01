package rifat.example.StudentManagementSystem.controller;

import rifat.example.StudentManagementSystem.entity.User;
import rifat.example.StudentManagementSystem.entity.Student;
import rifat.example.StudentManagementSystem.entity.Teacher;
import rifat.example.StudentManagementSystem.service.UserService;
import rifat.example.StudentManagementSystem.service.StudentService;
import rifat.example.StudentManagementSystem.service.TeacherService;
import rifat.example.StudentManagementSystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping
    public String viewProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            
            if (user.getRole().name().equals("ROLE_STUDENT")) {
                Optional<Student> student = studentService.findByUser(user);
                student.ifPresent(s -> model.addAttribute("profile", s));
                model.addAttribute("isStudent", true);
            } else {
                Optional<Teacher> teacher = teacherService.findByUser(user);
                teacher.ifPresent(t -> model.addAttribute("profile", t));
                model.addAttribute("isStudent", false);
            }
            model.addAttribute("departments", departmentService.findAll());
        }
        
        return "profile/view";
    }
    
    @GetMapping("/edit")
    public String editProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            
            if (user.getRole().name().equals("ROLE_STUDENT")) {
                Optional<Student> student = studentService.findByUser(user);
                student.ifPresent(s -> model.addAttribute("profile", s));
                model.addAttribute("isStudent", true);
            } else {
                Optional<Teacher> teacher = teacherService.findByUser(user);
                teacher.ifPresent(t -> model.addAttribute("profile", t));
                model.addAttribute("isStudent", false);
            }
            model.addAttribute("departments", departmentService.findAll());
        }
        
        return "profile/edit";
    }
    
    @PostMapping("/update/student")
    public String updateStudentProfile(@ModelAttribute Student updatedStudent,
                                        @RequestParam(required = false) Long departmentId,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Student> studentOpt = studentService.findByUser(user);
            
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                student.setFirstName(updatedStudent.getFirstName());
                student.setLastName(updatedStudent.getLastName());
                student.setPhone(updatedStudent.getPhone());
                
                if (departmentId != null) {
                    departmentService.findById(departmentId).ifPresent(student::setDepartment);
                }
                
                studentService.save(student);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
            }
        }
        
        return "redirect:/profile";
    }
    
    @PostMapping("/update/teacher")
    public String updateTeacherProfile(@ModelAttribute Teacher updatedTeacher,
                                        @RequestParam(required = false) Long departmentId,
                                        Authentication authentication,
                                        RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<Teacher> teacherOpt = teacherService.findByUser(user);
            
            if (teacherOpt.isPresent()) {
                Teacher teacher = teacherOpt.get();
                teacher.setFirstName(updatedTeacher.getFirstName());
                teacher.setLastName(updatedTeacher.getLastName());
                teacher.setPhone(updatedTeacher.getPhone());
                teacher.setQualification(updatedTeacher.getQualification());
                teacher.setSpecialization(updatedTeacher.getSpecialization());
                
                if (departmentId != null) {
                    departmentService.findById(departmentId).ifPresent(teacher::setDepartment);
                }
                
                teacherService.save(teacher);
                redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
            }
        }
        
        return "redirect:/profile";
    }
}
