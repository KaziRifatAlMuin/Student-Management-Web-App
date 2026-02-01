package rifat.example.StudentManagementSystem.controller;

import rifat.example.StudentManagementSystem.entity.Department;
import rifat.example.StudentManagementSystem.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/departments")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "department/list";
    }
    
    @GetMapping("/view/{id}")
    public String viewDepartment(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "department/view";
        }
        return "redirect:/departments";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "department/form";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute Department department, RedirectAttributes redirectAttributes) {
        departmentService.save(department);
        redirectAttributes.addFlashAttribute("message", "Department saved successfully!");
        return "redirect:/departments";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "department/form";
        }
        return "redirect:/departments";
    }
    
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        departmentService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Department deleted successfully!");
        return "redirect:/departments";
    }
}
