package rifat.example.StudentManagementSystem.controller;

import rifat.example.StudentManagementSystem.entity.Teacher;
import rifat.example.StudentManagementSystem.service.TeacherService;
import rifat.example.StudentManagementSystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teacher/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewTeacher(@PathVariable Long id, Model model) {
        Optional<Teacher> teacher = teacherService.findById(id);
        if (teacher.isPresent()) {
            model.addAttribute("teacher", teacher.get());
            return "teacher/view";
        }
        return "redirect:/teachers";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("departments", departmentService.findAll());
        return "teacher/form";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/save")
    public String saveTeacher(@ModelAttribute Teacher teacher, 
                              @RequestParam(required = false) Long departmentId,
                              @RequestParam(required = false) String username,
                              @RequestParam(required = false) String password,
                              RedirectAttributes redirectAttributes) {
        if (departmentId != null) {
            departmentService.findById(departmentId).ifPresent(teacher::setDepartment);
        }
        
        if (teacher.getId() == null && username != null && password != null) {
            // Creating new teacher with user account
            teacherService.createTeacher(teacher, username, password);
        } else {
            teacherService.save(teacher);
        }
        
        redirectAttributes.addFlashAttribute("message", "Teacher saved successfully!");
        return "redirect:/teachers";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Teacher> teacher = teacherService.findById(id);
        if (teacher.isPresent()) {
            model.addAttribute("teacher", teacher.get());
            model.addAttribute("departments", departmentService.findAll());
            return "teacher/form";
        }
        return "redirect:/teachers";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        teacherService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Teacher deleted successfully!");
        return "redirect:/teachers";
    }
}
