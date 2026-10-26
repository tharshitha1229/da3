package com.airline.system.repository;

import com.airline.system.model.Employee;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends Neo4jRepository<Employee, String> {

    List<Employee> findByDesignationIgnoreCase(String designation);

    @Query("MATCH (e:Employee {employeeID: $employeeID}) DETACH DELETE e")
    void deleteEmployeeNode(String employeeID);
}
