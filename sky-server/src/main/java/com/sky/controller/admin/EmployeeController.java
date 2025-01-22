package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;




    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }
//TODO 1.此处返回的应该是DTO，而且返回是空置

    /**
     * work add
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("add work")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("save worker{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }



    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * worker page
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("page query")
    public Result<PageResult> Page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("pageQueryResult started,{}", employeePageQueryDTO);
        PageResult pageResult= employeeService.PageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Start or Close
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Start Or Close works account")
//    这里直接点击执行service就完了
    public Result StartOrClose(@PathVariable Integer status, Long id){
        log.info("forbid works:{},{}", status, id);
        employeeService.StartOrClose(status,id);
        return Result.success();
    }

    /**
     * select worker by id
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("select worker in id")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("getById:{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * update worker ifo
     * @param employDTO
     * @return
     */
    @PutMapping
    @ApiOperation("update worker ifo")
    public Result Update(@RequestBody EmployeeDTO employDTO){
        log.info("update worker:{}", employDTO);
        //直接传方法，无需发返回值
        employeeService.update(employDTO);
        return Result.success();
    }
}

