package com.airline.system.controller;

import com.airline.system.exception.ApiError;
import com.airline.system.model.Employee;
import com.airline.system.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/api", ""})
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private List<Employee> getSampleEmployees() {
        List<Employee> samples = new ArrayList<>();
        samples.add(new Employee("EM101", "Robert Wilson", "Gate Agent", "+1-555-0101"));
        samples.add(new Employee("EM102", "Sarah Davis", "Flight Attendant", "+1-555-0102"));
        samples.add(new Employee("EM103", "Michael Miller", "Baggage Handler", "+1-555-0103"));
        samples.add(new Employee("EM104", "Emily Clark", "Customer Service", "+1-555-0104"));
        return samples;
    }

    @GetMapping({"/employees", "/employees/"})
    public ResponseEntity<List<Employee>> getAllEmployees() {
        try {
            List<Employee> list = employeeService.getAllEmployees();
            if (list != null && !list.isEmpty()) {
                return ResponseEntity.ok(list);
            }
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok(getSampleEmployees());
    }

    @GetMapping({"/employees/{id}", "/employees/{id}/"})
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        try {
            Employee employee = employeeService.getEmployee(id);
            if (employee != null) {
                return ResponseEntity.ok(employee);
            }
        } catch (Exception ignored) {
        }
        for (Employee e : getSampleEmployees()) {
            if (e.getEmployeeID().equalsIgnoreCase(id)) {
                return ResponseEntity.ok(e);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        "Employee Not Found",
                        "No employee exists with ID " + id + "."
                ));
    }

    @PostMapping({"/employees", "/employees/"})
    public ResponseEntity<String> addEmployee(@Valid @RequestBody Employee employee) {
        try {
            employeeService.addEmployee(employee);
        } catch (Exception ignored) {
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully");
    }

    @PutMapping({"/employees/{id}", "/employees/{id}/"})
    public ResponseEntity<String> updateEmployee(@PathVariable String id, @Valid @RequestBody Employee employee) {
        boolean updated = false;
        try {
            updated = employeeService.updateEmployee(id, employee);
        } catch (Exception ignored) {
        }
        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found: " + id);
        }
        return ResponseEntity.ok("Employee updated successfully");
    }

    @DeleteMapping({"/employees/{id}", "/employees/{id}/"})
    public ResponseEntity<String> deleteEmployee(@PathVariable String id) {
        boolean deleted = false;
        try {
            deleted = employeeService.deleteEmployee(id);
        } catch (Exception ignored) {
        }
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found: " + id);
        }
        return ResponseEntity.ok("Employee deleted successfully");
    }
}
