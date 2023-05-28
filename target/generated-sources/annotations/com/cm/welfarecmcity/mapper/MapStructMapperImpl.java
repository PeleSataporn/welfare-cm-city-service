package com.cm.welfarecmcity.mapper;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.dto.EmployeeDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-28T13:29:32+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
@Component
public class MapStructMapperImpl implements MapStructMapper {

    @Override
    public EmployeeDto reqToEmployee(EmpEditReq req) {
        if ( req == null ) {
            return null;
        }

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId( req.getId() );
        employeeDto.setEmployeeCode( req.getEmployeeCode() );
        employeeDto.setPrefix( req.getPrefix() );
        employeeDto.setFirstName( req.getFirstName() );
        employeeDto.setLastName( req.getLastName() );
        employeeDto.setIdCard( req.getIdCard() );
        employeeDto.setGender( req.getGender() );
        employeeDto.setMarital( req.getMarital() );
        employeeDto.setBirthday( req.getBirthday() );
        employeeDto.setPosition( req.getPosition() );
        employeeDto.setAffiliation( req.getAffiliation() );
        employeeDto.setEmployeeType( req.getEmployeeType() );
        employeeDto.setLevel( req.getLevel() );
        employeeDto.setDepartment( req.getDepartment() );
        employeeDto.setSalary( req.getSalary() );
        employeeDto.setCompensation( req.getCompensation() );
        employeeDto.setContractStartDate( req.getContractStartDate() );
        employeeDto.setCivilServiceDate( req.getCivilServiceDate() );
        employeeDto.setEmployeeStatus( req.getEmployeeStatus() );
        employeeDto.setBillingStartDate( req.getBillingStartDate() );
        if ( req.getMonthlyStockMoney() != null ) {
            employeeDto.setMonthlyStockMoney( req.getMonthlyStockMoney().intValue() );
        }
        employeeDto.setContact( req.getContact() );
        employeeDto.setSalaryBankAccountNumber( req.getSalaryBankAccountNumber() );
        employeeDto.setBankAccountReceivingNumber( req.getBankAccountReceivingNumber() );
        employeeDto.setUser( req.getUser() );
        employeeDto.setStock( req.getStock() );
        employeeDto.setLoan( req.getLoan() );
        employeeDto.setApproveFlag( req.getApproveFlag() );
        employeeDto.setPasswordFlag( req.getPasswordFlag() );
        employeeDto.setProfileFlag( req.getProfileFlag() );

        return employeeDto;
    }
}
