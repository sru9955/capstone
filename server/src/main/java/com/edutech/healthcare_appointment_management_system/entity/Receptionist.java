package com.edutech.healthcare_appointment_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "receptionists")
public class Receptionist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    public Receptionist() {}

    public Receptionist(Long id, String name, String phone, User user) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.user = user;
    }

    public static ReceptionistBuilder builder() {
        return new ReceptionistBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class ReceptionistBuilder {
        private Long id;
        private String name;
        private String phone;
        private User user;

        public ReceptionistBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ReceptionistBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ReceptionistBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public ReceptionistBuilder user(User user) {
            this.user = user;
            return this;
        }

        public Receptionist build() {
            return new Receptionist(id, name, phone, user);
        }
    }
}
