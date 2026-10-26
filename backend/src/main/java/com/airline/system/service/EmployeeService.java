package com.airline.system.service;

import com.airline.system.model.Employee;
import com.airline.system.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getEmployee(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public boolean updateEmployee(String id, Employee employee) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }
        employee.setEmployeeID(id);
        employeeRepository.save(employee);
        return true;
    }

    public boolean deleteEmployee(String id) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }
        employeeRepository.deleteEmployeeNode(id);
        return true;
    }
}
