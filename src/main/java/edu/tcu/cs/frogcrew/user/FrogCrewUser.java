package edu.tcu.cs.frogcrew.user;

import edu.tcu.cs.frogcrew.availability.Availability;
import edu.tcu.cs.frogcrew.system.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity
public class FrogCrewUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer userId;

    String username;

    @NotEmpty(message = "email is required.")
    private String email;

    @NotEmpty(message = "password is required.")
    private String password;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    @NotNull(message = "roles are required.")
    private Role role;

    private boolean enabled;

    @ElementCollection
    private List<String> qualifiedPositions;

    private String payRate;

    @ManyToOne
    private Availability availability;


    public void register() {
    }

    public void login() {

    }

    public void invite() {

    }


    public Integer getId() {
        return userId;
    }

    public void setId(Integer id) {
        this.userId = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    

    public @NotEmpty(message = "email is required.") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "email is required.") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "password is required.") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "password is required.") String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<String> getQualifiedPositions() {
        return qualifiedPositions;
    }

    public void setQualifiedPositions(List<String> qualifiedPositions) {
        this.qualifiedPositions = qualifiedPositions;
    }

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }
}
