package com.edutech.healthcare_appointment_management_system.dto;

public class PatientProfileUpdateDto {
    private String name;
    private String phone;
    private String address;
    private Integer age;
    private Double weight;
    private Double height;

    public PatientProfileUpdateDto() {}

    public PatientProfileUpdateDto(String name, String phone, String address, Integer age, Double weight, Double height) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public static PatientProfileUpdateDtoBuilder builder() {
        return new PatientProfileUpdateDtoBuilder();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public static class PatientProfileUpdateDtoBuilder {
        private String name;
        private String phone;
        private String address;
        private Integer age;
        private Double weight;
        private Double height;

        public PatientProfileUpdateDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PatientProfileUpdateDtoBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public PatientProfileUpdateDtoBuilder address(String address) {
            this.address = address;
            return this;
        }

        public PatientProfileUpdateDtoBuilder age(Integer age) {
            this.age = age;
            return this;
        }

        public PatientProfileUpdateDtoBuilder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public PatientProfileUpdateDtoBuilder height(Double height) {
            this.height = height;
            return this;
        }

        public PatientProfileUpdateDto build() {
            return new PatientProfileUpdateDto(name, phone, address, age, weight, height);
        }
    }
}
