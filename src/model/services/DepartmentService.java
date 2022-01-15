package model.services;

import model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
  public List<Department> findAll() {
    List<Department> departments = new ArrayList<>();

    departments.add(new Department(1, "Computers"));
    departments.add(new Department(2, "Electronics"));
    departments.add(new Department(3, "Books"));

    return departments;
  }
}
