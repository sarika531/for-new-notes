package com.payswiff.mfmsproject.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Represents a role entity in the system.
 * This class is mapped to the 'roles' table in the database, 
 * storing details about the different roles that can be assigned to users or employees.
 * Each role defines the permissions and responsibilities of users in the system.
 * 
 * <p>This class contains the following fields:</p>
 * <ul>
 *     <li><b>id</b>: The unique identifier for the role (auto-incremented).</li>
 *     <li><b>name</b>: The name of the role (e.g., "Admin", "User", "Manager").</li>
 * </ul>
 * 
 * <p>The `Role` class helps manage the different roles in the system, allowing the system to assign users 
 * to specific roles and control their access and privileges based on the role.</p>
 * 
 * @author Gopi Bapanapalli
 * @version MFMS_0.0.1
 */
@Entity
@Table(name = "roles")
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
@Getter
@Setter
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param id
	 * @param name
	 */
	public Role(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	/**
	 * 
	 */
	public Role() {
	}
	
}
