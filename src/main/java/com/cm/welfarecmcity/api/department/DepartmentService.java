package com.cm.welfarecmcity.api.department;

import com.cm.welfarecmcity.dto.DepartmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentDto> searchDepartment() {
        return departmentRepository.findAll();
    }

}
