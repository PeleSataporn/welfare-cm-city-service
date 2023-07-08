package com.cm.welfarecmcity.mapper;

import com.cm.welfarecmcity.api.employee.model.EmpEditReq;
import com.cm.welfarecmcity.api.notification.model.NotificationRes;
import com.cm.welfarecmcity.api.notification.model.NotifyEmployeeRes;
import com.cm.welfarecmcity.dto.AffiliationDto;
import com.cm.welfarecmcity.dto.BeneficiaryDto;
import com.cm.welfarecmcity.dto.BureauDto;
import com.cm.welfarecmcity.dto.DepartmentDto;
import com.cm.welfarecmcity.dto.EmployeeDto;
import com.cm.welfarecmcity.dto.EmployeeTypeDto;
import com.cm.welfarecmcity.dto.LevelDto;
import com.cm.welfarecmcity.dto.PetitionNotificationDto;
import com.cm.welfarecmcity.dto.PositionsDto;
import com.cm.welfarecmcity.logic.employee.model.AffiliationRes;
import com.cm.welfarecmcity.logic.employee.model.BeneficiariesRes;
import com.cm.welfarecmcity.logic.employee.model.BureauRes;
import com.cm.welfarecmcity.logic.employee.model.DepartmentRes;
import com.cm.welfarecmcity.logic.employee.model.EmployeeRes;
import com.cm.welfarecmcity.logic.employee.model.EmployeeTypeRes;
import com.cm.welfarecmcity.logic.employee.model.LevelRes;
import com.cm.welfarecmcity.logic.employee.model.PositionRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-08T11:37:19+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
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

    @Override
    public EmployeeRes employeeToRes(EmployeeDto dto) {
        if ( dto == null ) {
            return null;
        }

        EmployeeRes employeeRes = new EmployeeRes();

        employeeRes.setEmployeeCode( dto.getEmployeeCode() );
        employeeRes.setPrefix( dto.getPrefix() );
        employeeRes.setFirstName( dto.getFirstName() );
        employeeRes.setLastName( dto.getLastName() );
        employeeRes.setIdCard( dto.getIdCard() );
        employeeRes.setGender( dto.getGender() );
        employeeRes.setMarital( dto.getMarital() );
        employeeRes.setBirthday( dto.getBirthday() );
        employeeRes.setPosition( positionsDtoToPositionRes( dto.getPosition() ) );
        employeeRes.setAffiliation( affiliationDtoToAffiliationRes( dto.getAffiliation() ) );
        employeeRes.setEmployeeType( employeeTypeDtoToEmployeeTypeRes( dto.getEmployeeType() ) );
        employeeRes.setLevel( levelDtoToLevelRes( dto.getLevel() ) );
        employeeRes.setDepartment( departmentDtoToDepartmentRes( dto.getDepartment() ) );
        employeeRes.setUser( dto.getUser() );
        employeeRes.setSalary( dto.getSalary() );
        employeeRes.setCompensation( dto.getCompensation() );
        employeeRes.setContractStartDate( dto.getContractStartDate() );
        employeeRes.setCivilServiceDate( dto.getCivilServiceDate() );
        employeeRes.setEmployeeStatus( dto.getEmployeeStatus() );
        employeeRes.setBillingStartDate( dto.getBillingStartDate() );
        employeeRes.setMonthlyStockMoney( dto.getMonthlyStockMoney() );
        employeeRes.setRetirementDate( dto.getRetirementDate() );
        employeeRes.setSalaryBankAccountNumber( dto.getSalaryBankAccountNumber() );
        employeeRes.setBankAccountReceivingNumber( dto.getBankAccountReceivingNumber() );
        employeeRes.setApproveFlag( dto.getApproveFlag() );
        employeeRes.setPasswordFlag( dto.getPasswordFlag() );
        employeeRes.setProfileFlag( dto.getProfileFlag() );
        employeeRes.setCheckStockValueFlag( dto.getCheckStockValueFlag() );
        employeeRes.setContact( dto.getContact() );
        employeeRes.setBeneficiaries( beneficiaryDtoListToBeneficiariesResList( dto.getBeneficiaries() ) );

        return employeeRes;
    }

    @Override
    public NotificationRes notificationToRes(PetitionNotificationDto dto) {
        if ( dto == null ) {
            return null;
        }

        NotificationRes notificationRes = new NotificationRes();

        notificationRes.setId( dto.getId() );
        notificationRes.setStatus( dto.getStatus() );
        notificationRes.setReason( dto.getReason() );
        notificationRes.setDescription( dto.getDescription() );
        notificationRes.setCreateDate( dto.getCreateDate() );
        notificationRes.setEmployee( employeeDtoToNotifyEmployeeRes( dto.getEmployee() ) );

        return notificationRes;
    }

    protected PositionRes positionsDtoToPositionRes(PositionsDto positionsDto) {
        if ( positionsDto == null ) {
            return null;
        }

        PositionRes positionRes = new PositionRes();

        positionRes.setId( positionsDto.getId() );
        positionRes.setName( positionsDto.getName() );

        return positionRes;
    }

    protected BureauRes bureauDtoToBureauRes(BureauDto bureauDto) {
        if ( bureauDto == null ) {
            return null;
        }

        BureauRes bureauRes = new BureauRes();

        bureauRes.setId( bureauDto.getId() );
        bureauRes.setName( bureauDto.getName() );

        return bureauRes;
    }

    protected AffiliationRes affiliationDtoToAffiliationRes(AffiliationDto affiliationDto) {
        if ( affiliationDto == null ) {
            return null;
        }

        AffiliationRes affiliationRes = new AffiliationRes();

        affiliationRes.setId( affiliationDto.getId() );
        affiliationRes.setName( affiliationDto.getName() );
        affiliationRes.setBureau( bureauDtoToBureauRes( affiliationDto.getBureau() ) );

        return affiliationRes;
    }

    protected EmployeeTypeRes employeeTypeDtoToEmployeeTypeRes(EmployeeTypeDto employeeTypeDto) {
        if ( employeeTypeDto == null ) {
            return null;
        }

        EmployeeTypeRes employeeTypeRes = new EmployeeTypeRes();

        employeeTypeRes.setId( employeeTypeDto.getId() );
        employeeTypeRes.setName( employeeTypeDto.getName() );

        return employeeTypeRes;
    }

    protected LevelRes levelDtoToLevelRes(LevelDto levelDto) {
        if ( levelDto == null ) {
            return null;
        }

        LevelRes levelRes = new LevelRes();

        levelRes.setId( levelDto.getId() );
        levelRes.setName( levelDto.getName() );

        return levelRes;
    }

    protected DepartmentRes departmentDtoToDepartmentRes(DepartmentDto departmentDto) {
        if ( departmentDto == null ) {
            return null;
        }

        DepartmentRes departmentRes = new DepartmentRes();

        departmentRes.setId( departmentDto.getId() );
        departmentRes.setName( departmentDto.getName() );

        return departmentRes;
    }

    protected BeneficiariesRes beneficiaryDtoToBeneficiariesRes(BeneficiaryDto beneficiaryDto) {
        if ( beneficiaryDto == null ) {
            return null;
        }

        BeneficiariesRes beneficiariesRes = new BeneficiariesRes();

        beneficiariesRes.setId( beneficiaryDto.getId() );
        beneficiariesRes.setPrefix( beneficiaryDto.getPrefix() );
        beneficiariesRes.setFirstName( beneficiaryDto.getFirstName() );
        beneficiariesRes.setLastName( beneficiaryDto.getLastName() );
        beneficiariesRes.setGender( beneficiaryDto.getGender() );
        beneficiariesRes.setBirthday( beneficiaryDto.getBirthday() );
        beneficiariesRes.setRelationship( beneficiaryDto.getRelationship() );
        beneficiariesRes.setMarital( beneficiaryDto.getMarital() );
        beneficiariesRes.setLifeStatus( beneficiaryDto.getLifeStatus() );

        return beneficiariesRes;
    }

    protected List<BeneficiariesRes> beneficiaryDtoListToBeneficiariesResList(List<BeneficiaryDto> list) {
        if ( list == null ) {
            return null;
        }

        List<BeneficiariesRes> list1 = new ArrayList<BeneficiariesRes>( list.size() );
        for ( BeneficiaryDto beneficiaryDto : list ) {
            list1.add( beneficiaryDtoToBeneficiariesRes( beneficiaryDto ) );
        }

        return list1;
    }

    protected NotifyEmployeeRes employeeDtoToNotifyEmployeeRes(EmployeeDto employeeDto) {
        if ( employeeDto == null ) {
            return null;
        }

        NotifyEmployeeRes notifyEmployeeRes = new NotifyEmployeeRes();

        notifyEmployeeRes.setId( employeeDto.getId() );
        notifyEmployeeRes.setEmployeeCode( employeeDto.getEmployeeCode() );
        notifyEmployeeRes.setPrefix( employeeDto.getPrefix() );
        notifyEmployeeRes.setFirstName( employeeDto.getFirstName() );
        notifyEmployeeRes.setLastName( employeeDto.getLastName() );
        notifyEmployeeRes.setIdCard( employeeDto.getIdCard() );
        notifyEmployeeRes.setGender( employeeDto.getGender() );

        return notifyEmployeeRes;
    }
}
