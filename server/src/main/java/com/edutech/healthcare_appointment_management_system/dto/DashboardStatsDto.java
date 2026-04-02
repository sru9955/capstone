package com.edutech.healthcare_appointment_management_system.dto;

import java.util.Map;

public class DashboardStatsDto {
    // Patient
    private Long totalAppointments;
    private Long upcomingAppointments;

    // Doctor
    private Long todayAppointments;
    private Long totalPatients;

    // Receptionist
    private Long pendingAppointments;
    private Long totalDoctors;

    // Common
    private Long cancelledAppointments;
    private Long completedAppointments;

    // Admin specific
    private Double totalRevenue;
    private Map<String, Double> revenuePerDoctor;
    private Map<String, Long> appointmentsPerDoctor;

    public DashboardStatsDto() {}

    public DashboardStatsDto(Long totalAppointments, Long upcomingAppointments, Long todayAppointments, Long totalPatients, Long pendingAppointments, Long totalDoctors, Long cancelledAppointments, Long completedAppointments, Double totalRevenue, Map<String, Double> revenuePerDoctor, Map<String, Long> appointmentsPerDoctor) {
        this.totalAppointments = totalAppointments;
        this.upcomingAppointments = upcomingAppointments;
        this.todayAppointments = todayAppointments;
        this.totalPatients = totalPatients;
        this.pendingAppointments = pendingAppointments;
        this.totalDoctors = totalDoctors;
        this.cancelledAppointments = cancelledAppointments;
        this.completedAppointments = completedAppointments;
        this.totalRevenue = totalRevenue;
        this.revenuePerDoctor = revenuePerDoctor;
        this.appointmentsPerDoctor = appointmentsPerDoctor;
    }

    public static DashboardStatsDtoBuilder builder() {
        return new DashboardStatsDtoBuilder();
    }

    public Long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(Long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public Long getUpcomingAppointments() {
        return upcomingAppointments;
    }

    public void setUpcomingAppointments(Long upcomingAppointments) {
        this.upcomingAppointments = upcomingAppointments;
    }

    public Long getTodayAppointments() {
        return todayAppointments;
    }

    public void setTodayAppointments(Long todayAppointments) {
        this.todayAppointments = todayAppointments;
    }

    public Long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(Long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public Long getPendingAppointments() {
        return pendingAppointments;
    }

    public void setPendingAppointments(Long pendingAppointments) {
        this.pendingAppointments = pendingAppointments;
    }

    public Long getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(Long totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public Long getCancelledAppointments() {
        return cancelledAppointments;
    }

    public void setCancelledAppointments(Long cancelledAppointments) {
        this.cancelledAppointments = cancelledAppointments;
    }

    public Long getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(Long completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Map<String, Double> getRevenuePerDoctor() {
        return revenuePerDoctor;
    }

    public void setRevenuePerDoctor(Map<String, Double> revenuePerDoctor) {
        this.revenuePerDoctor = revenuePerDoctor;
    }

    public Map<String, Long> getAppointmentsPerDoctor() {
        return appointmentsPerDoctor;
    }

    public void setAppointmentsPerDoctor(Map<String, Long> appointmentsPerDoctor) {
        this.appointmentsPerDoctor = appointmentsPerDoctor;
    }

    public static class DashboardStatsDtoBuilder {
        private Long totalAppointments;
        private Long upcomingAppointments;
        private Long todayAppointments;
        private Long totalPatients;
        private Long pendingAppointments;
        private Long totalDoctors;
        private Long cancelledAppointments;
        private Long completedAppointments;
        private Double totalRevenue;
        private Map<String, Double> revenuePerDoctor;
        private Map<String, Long> appointmentsPerDoctor;

        public DashboardStatsDtoBuilder totalAppointments(Long totalAppointments) {
            this.totalAppointments = totalAppointments;
            return this;
        }

        public DashboardStatsDtoBuilder upcomingAppointments(Long upcomingAppointments) {
            this.upcomingAppointments = upcomingAppointments;
            return this;
        }

        public DashboardStatsDtoBuilder todayAppointments(Long todayAppointments) {
            this.todayAppointments = todayAppointments;
            return this;
        }

        public DashboardStatsDtoBuilder totalPatients(Long totalPatients) {
            this.totalPatients = totalPatients;
            return this;
        }

        public DashboardStatsDtoBuilder pendingAppointments(Long pendingAppointments) {
            this.pendingAppointments = pendingAppointments;
            return this;
        }

        public DashboardStatsDtoBuilder totalDoctors(Long totalDoctors) {
            this.totalDoctors = totalDoctors;
            return this;
        }

        public DashboardStatsDtoBuilder cancelledAppointments(Long cancelledAppointments) {
            this.cancelledAppointments = cancelledAppointments;
            return this;
        }

        public DashboardStatsDtoBuilder completedAppointments(Long completedAppointments) {
            this.completedAppointments = completedAppointments;
            return this;
        }

        public DashboardStatsDtoBuilder totalRevenue(Double totalRevenue) {
            this.totalRevenue = totalRevenue;
            return this;
        }

        public DashboardStatsDtoBuilder revenuePerDoctor(Map<String, Double> revenuePerDoctor) {
            this.revenuePerDoctor = revenuePerDoctor;
            return this;
        }

        public DashboardStatsDtoBuilder appointmentsPerDoctor(Map<String, Long> appointmentsPerDoctor) {
            this.appointmentsPerDoctor = appointmentsPerDoctor;
            return this;
        }

        public DashboardStatsDto build() {
            return new DashboardStatsDto(totalAppointments, upcomingAppointments, todayAppointments, totalPatients, pendingAppointments, totalDoctors, cancelledAppointments, completedAppointments, totalRevenue, revenuePerDoctor, appointmentsPerDoctor);
        }
    }
}
