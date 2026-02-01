package rifat.example.StudentManagementSystem.controller;

import rifat.example.StudentManagementSystem.entity.Course;
import rifat.example.StudentManagementSystem.service.CourseService;
import rifat.example.StudentManagementSystem.service.DepartmentService;
import rifat.example.StudentManagementSystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private DepartmentService departmentService;
    
    @Autowired
    private TeacherService teacherService;
    
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "course/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "course/view";
        }
        return "redirect:/courses";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "course/form";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course, 
                             @RequestParam(required = false) Long departmentId,
                             @RequestParam(required = false) Long teacherId,
                             RedirectAttributes redirectAttributes) {
        if (departmentId != null) {
            departmentService.findById(departmentId).ifPresent(course::setDepartment);
        }
        if (teacherId != null) {
            teacherService.findById(teacherId).ifPresent(course::setTeacher);
        }
        courseService.save(course);
        redirectAttributes.addFlashAttribute("message", "Course saved successfully!");
        return "redirect:/courses";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.findById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("teachers", teacherService.findAll());
            return "course/form";
        }
        return "redirect:/courses";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Course deleted successfully!");
        return "redirect:/courses";
    }
}
