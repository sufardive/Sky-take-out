package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

//    TODO 2.service 打注解说明在干嘛

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    Employee save(EmployeeDTO employeeDTO);

    /**
     * pagequery
     *
     * @param EmployeePageQueryDTO
     * @return
     */
    PageResult PageQuery(EmployeePageQueryDTO EmployeePageQueryDTO);
}
