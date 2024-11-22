package com.payswiff.mfmsproject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents an Employee entity in the system. This class is mapped to the 'employee' table in the database
 * and holds information about a specific employee. The employee has attributes like unique UUID, Payswiff ID,
 * name, email, password, phone number, designation, employee type, creation time, and last updated time. 
 * The entity supports automatic timestamping for creation and updates.
 * <p>
 * This class uses Lombok annotations for reducing boilerplate code:
 * <ul>
 *     <li>@Getter and @Setter: Automatically generates getter and setter methods for all fields.</li>
 *     <li>@Builder: Provides a builder pattern to create instances of the class.</li>
 *     <li>@Data: Generates getter, setter, toString, equals, and hashCode methods for the class.</li>
 *     <li>@AllArgsConstructor: Generates a constructor with all fields as parameters.</li>
 *     <li>@NoArgsConstructor: Generates a no-arguments constructor.</li>
 * </ul>
 * </p>
 * 
 * @Entity Marks the class as a JPA entity to be mapped to a database table.
 * @Table(name = "employee") Specifies the table name in the database.
 * @Id Denotes the primary key field of the entity.
 * @GeneratedValue(strategy = GenerationType.IDENTITY) Automatically generates the primary key value.
 * @Column Specifies the database column mapping for each field.
 * @CreationTimestamp Automatically sets the creation timestamp when the employee record is created.
 * @UpdateTimestamp Automatically updates the timestamp when the employee record is updated.
 * @ManyToMany Establishes a many-to-many relationship with the Role entity.
 * 
 * @author Revanth K
 * @version MFMS_0.0.1
 */


@Entity
@Table(name = "employee") // Specifies the name of the table in the database
@Data // Generates getter and setter methods, toString, equals, and hashCode
@Getter // Generates getter methods for all fields
@Setter // Generates setter methods for all fields
//@AllArgsConstructor // Generates a constructor with all fields
//@NoArgsConstructor // Generates a no-arguments constructor
@Builder // Enables the Builder pattern for this class
public class Employee {

	/**
	 * The unique identifier for the employee. This field is the primary key and is
	 * auto-generated.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generates the primary key value
	@Column(name = "employee_id") // Maps the field to the 'employee_id' column in the table
	private Long employeeId;

	/**
	 * The unique UUID for the employee. This field is required and must be unique.
	 */
	@Column(name = "employee_uuid", nullable = false, unique = true) // Maps to 'employee_uuid' column with unique
																		// constraint
	private String employeeUuid;

	/**
	 * The unique Payswiff ID for the employee. This field is required and must be
	 * unique.
	 */
	@Column(name = "employee_payswiff_id", nullable = false, unique = true) // Unique Payswiff ID for the employee
	private String employeePayswiffId;

	/**
	 * The name of the employee. This field is required.
	 */
	@Column(name = "employee_name", nullable = false) // Maps to 'employee_name' column, cannot be null
	private String employeeName;

	/**
	 * The email address of the employee. This field is required.
	 */
	@Column(name = "employee_email", nullable = false, unique = true) // Maps to 'employee_email' column, cannot be null
	private String employeeEmail;

	/**
	 * The password for the employee's account. This field is required.
	 */
	@Column(name = "employee_password", nullable = false) // Maps to 'employee_password' column, cannot be null
	@JsonIgnoreProperties
	@JsonIgnore
	private String employeePassword;

	/**
	 * The phone number of the employee. This field is optional.
	 */
	@Column(name = "employee_phone_number", nullable = false, unique = true) // Maps to 'employee_phone_number' column,
																				// can be null
	private String employeePhoneNumber;

	/**
	 * The designation of the employee. This field is required.
	 */
	@Column(name = "employee_designation", nullable = false) // Maps to 'employee_designation' column, cannot be null
	private String employeeDesignation;

	/**
	 * The type of employee, represented as an enum. This field is required.
	 */
	@Enumerated(EnumType.STRING) // Maps the enum type as a String in the database
	@Column(name = "employee_type", nullable = false) // Maps to 'employee_type' column, cannot be null
	private EmployeeType employeeType;

	/**
	 * The timestamp indicating when the employee record was created. This field is
	 * automatically populated when the record is created.
	 */
	@Column(name = "employee_creation_time") // Maps to 'employee_creation_time' column
	@CreationTimestamp // Automatically sets the timestamp when the entity is created
	private LocalDateTime employeeCreationTime;

	/**
	 * The timestamp indicating the last time the employee record was updated. This
	 * field is automatically populated when the record is updated.
	 */
	@UpdateTimestamp // Automatically updates the timestamp when the entity is updated
	@Column(name = "employee_updation_time") // Maps to 'employee_updation_time' column
	private LocalDateTime employeeUpdationTime;

	// roles
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "employee_roles", 
	joinColumns = @JoinColumn(referencedColumnName = "employee_id", name = "employee_id"), 
	inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "role_id"))
	private Set<Role> roles;

	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the employeeUuid
	 */
	public String getEmployeeUuid() {
		return employeeUuid;
	}

	/**
	 * @param employeeUuid the employeeUuid to set
	 */
	public void setEmployeeUuid(String employeeUuid) {
		this.employeeUuid = employeeUuid;
	}

	/**
	 * @return the employeePayswiffId
	 */
	public String getEmployeePayswiffId() {
		return employeePayswiffId;
	}

	/**
	 * @param employeePayswiffId the employeePayswiffId to set
	 */
	public void setEmployeePayswiffId(String employeePayswiffId) {
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
	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	/**
	 * @param employeeType the employeeType to set
	 */
	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}

	/**
	 * @return the employeeCreationTime
	 */
	public LocalDateTime getEmployeeCreationTime() {
		return employeeCreationTime;
	}

	/**
	 * @param employeeCreationTime the employeeCreationTime to set
	 */
	public void setEmployeeCreationTime(LocalDateTime employeeCreationTime) {
		this.employeeCreationTime = employeeCreationTime;
	}

	/**
	 * @return the employeeUpdationTime
	 */
	public LocalDateTime getEmployeeUpdationTime() {
		return employeeUpdationTime;
	}

	/**
	 * @param employeeUpdationTime the employeeUpdationTime to set
	 */
	public void setEmployeeUpdationTime(LocalDateTime employeeUpdationTime) {
		this.employeeUpdationTime = employeeUpdationTime;
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @param employeeId
	 * @param employeeUuid
	 * @param employeePayswiffId
	 * @param employeeName
	 * @param employeeEmail
	 * @param employeePassword
	 * @param employeePhoneNumber
	 * @param employeeDesignation
	 * @param employeeType
	 * @param employeeCreationTime
	 * @param employeeUpdationTime
	 * @param roles
	 */
	public Employee(Long employeeId, String employeeUuid, String employeePayswiffId, String employeeName,
			String employeeEmail, String employeePassword, String employeePhoneNumber, String employeeDesignation,
			EmployeeType employeeType, LocalDateTime employeeCreationTime, LocalDateTime employeeUpdationTime,
			Set<Role> roles) {
		this.employeeId = employeeId;
		this.employeeUuid = employeeUuid;
		this.employeePayswiffId = employeePayswiffId;
		this.employeeName = employeeName;
		this.employeeEmail = employeeEmail;
		this.employeePassword = employeePassword;
		this.employeePhoneNumber = employeePhoneNumber;
		this.employeeDesignation = employeeDesignation;
		this.employeeType = employeeType;
		this.employeeCreationTime = employeeCreationTime;
		this.employeeUpdationTime = employeeUpdationTime;
		this.roles = roles;
	}

	/**
	 * 
	 */
	public Employee() {
	}

	public Employee(String payswiffId, String email, String password, String type, String phonenumber) {
		// TODO Auto-generated constructor stub
		this.employeeEmail=email;
		this.employeePassword=password;
		this.employeePayswiffId=payswiffId;
		this.employeePhoneNumber=phonenumber;
		this.employeeType=EmployeeType.valueOf(type);
		
	}

	public Employee(long id, String name, String email) {
		// TODO Auto-generated constructor stub
		this.employeeId=id;
		this.employeeName=name;
		this.employeeEmail=email;
	}

	
	
	

	

}
