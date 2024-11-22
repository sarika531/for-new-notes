package com.payswiff.mfmsproject.reuquests;

import java.util.UUID;

import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.models.EmployeeType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;


/**
 * Represents a request to create a new Employee.
 * This class holds the necessary information to create an Employee entity.
 * <p>It contains fields for the employee's personal information, such as 
 * Payswiff ID, name, email, phone number, and designation. Additionally, 
 * it includes a method to convert the request into an Employee entity, 
 * with a randomly generated UUID for the employee.</p>
 * 
 * @author Chatla Sarika
 * @version MFMS_0.0.1
 */
@Data
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEmployeeRequest {

    /**
     * The Payswiff ID assigned to the employee.
     */
    private long employeePayswiffId;

    /**
     * The name of the employee.
     */
    private String employeeName;

    /**
     * The email address of the employee.
     */
    private String employeeEmail;

    /**
     * The password for the employee account.
    private String employeePassword;

    /**
     * The phone number of the employee.
     */
    private String employeePhoneNumber;

    /**
     * The designation or job title of the employee.
     */
    private String employeeDesignation;

    /**
     * The type of employee (e.g., admin, employee).
     */
    private String employeeType;
    
    private String employeePassword;

    /**
     * Converts this CreateEmployeeRequest to an Employee entity.
     *
     * @return A new Employee entity populated with the data from this request,
     *         including a randomly generated UUID for the employee.
     */
    public Employee toEmployee() {
    	
    	 ModelMapper modelMapper = new ModelMapper();
    	 
//    	 Add your custom converter here
         modelMapper.addConverter(new Converter<String, Long>() {
             public Long convert(MappingContext<String, Long> context) {
                 return Long.valueOf(context.getSource());
             }
         });
    	    Employee employee = modelMapper.map(this, Employee.class);
    	    employee.setEmployeeUuid(UUID.randomUUID().toString()); // Set UUID separately
    	    
    	    return employee;
    
    }

	/**
	 * @return the employeePayswiffId
	 */
	public long getEmployeePayswiffId() {
		return employeePayswiffId;
	}

	/**
	 * @param employeePayswiffId the employeePayswiffId to set
	 */
	public void setEmployeePayswiffId(long employeePayswiffId) {
		this.employeePayswiffId = employeePayswiffId;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the employeeEmail
	 */
	public String getEmployeeEmail() {
		return employeeEmail;
	}

	/**
	 * @param employeeEmail the employeeEmail to set
	 */
	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	/**
	 * @return the employeePassword
	 */
	public String getEmployeePassword() {
		return employeePassword;
	}

	/**
	 * @param employeePassword the employeePassword to set
	 */
	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}

	/**
	 * @return the employeePhoneNumber
	 */
	public String getEmployeePhoneNumber() {
		return employeePhoneNumber;
	}

	/**
	 * @param employeePhoneNumber the employeePhoneNumber to set
	 */
	public void setEmployeePhoneNumber(String employeePhoneNumber) {
		this.employeePhoneNumber = employeePhoneNumber;
	}

	/**
	 * @return the employeeDesignation
	 */
	public String getEmployeeDesignation() {
		return employeeDesignation;
	}

	/**
	 * @param employeeDesignation the employeeDesignation to set
	 */
	public void setEmployeeDesignation(String employeeDesignation) {
		this.employeeDesignation = employeeDesignation;
	}

	/**
	 * @return the employeeType
	 */
	public String getEmployeeType() {
		return employeeType;
	}

	/**
	 * @param employeeType the employeeType to set
	 */
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	/**
	 * @param employeePayswiffId
	 * @param employeeName
	 * @param employeeEmail
	 * @param employeePassword
	 * @param employeePhoneNumber
	 * @param employeeDesignation
	 * @param employeeType
	 */
	public CreateEmployeeRequest(long employeePayswiffId, String employeeName, String employeeEmail,
			String employeePassword, String employeePhoneNumber, String employeeDesignation, String employeeType) {
		this.employeePayswiffId = employeePayswiffId;
		this.employeeName = employeeName;
		this.employeeEmail = employeeEmail;
		this.employeePassword = employeePassword;
		this.employeePhoneNumber = employeePhoneNumber;
		this.employeeDesignation = employeeDesignation;
		this.employeeType = employeeType;
	}
	
}
