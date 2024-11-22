package com.payswiff.mfmsproject.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.payswiff.mfmsproject.models.Employee;
import com.payswiff.mfmsproject.repositories.EmployeeRepository;

/**
 * Custom UserDetailsService implementation that retrieves user-specific data
 * from the database and converts it into a UserDetails object used by Spring Security.
 * @authorGopi Bapanapalli
 * @version MFMS_0.0.1
 */
@Service
public class CustomeUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Loads user-specific data given an email or phone number.
     *
     * @param emailOrPhone the email or phone number of the user.
     * @return a UserDetails object containing the user information.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
        
        // Fetch the employee from the database using the provided email or phone number.
        Employee dbEmployee = employeeRepository.findByEmployeeEmailOrEmployeePhoneNumber(emailOrPhone, emailOrPhone)
            .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email or phone: " + emailOrPhone));

        // Map employee roles to granted authorities.
        Set<GrantedAuthority> grantedAuthorities = dbEmployee.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());
        
        // Return a UserDetails object with the employee's email, password, and roles.
        return new User(dbEmployee.getEmployeeEmail(), dbEmployee.getEmployeePassword(), grantedAuthorities);
    }
}
