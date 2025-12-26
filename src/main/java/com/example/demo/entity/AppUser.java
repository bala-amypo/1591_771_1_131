package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Boolean active = true;

    public AppUser() {}

    public AppUser(String email, String password, String role, Boolean active) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.active = active != null ? active : true;
    }

    public static AppUserBuilder builder() {
        return new AppUserBuilder();
    }

    public static class AppUserBuilder {
        private Long id;
        private String email;
        private String password;
        private String role;
        private Boolean active = true;

        public AppUserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AppUserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AppUserBuilder role(String role) {
            this.role = role;
            return this;
        }

        public AppUserBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public AppUser build() {
            AppUser user = new AppUser(email, password, role, active);
            user.setId(id);
            return user;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}