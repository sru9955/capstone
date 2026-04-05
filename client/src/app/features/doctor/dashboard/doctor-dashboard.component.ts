import { Component, OnInit, OnDestroy } from '@angular/core';
import { DashboardService } from '../../../core/services/dashboard.service';
import { HttpService } from '../../../core/services/http.service';
import { AlertService } from '../../../core/services/alert.service';
import { DashboardStats } from '../../../shared/models/dashboard.model';
import { Appointment, Doctor } from '../../../shared/models/appointment.model';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-doctor-dashboard',
  templateUrl: './doctor-dashboard.component.html',
  styleUrls: ['./doctor-dashboard.component.scss']
})
export class DoctorDashboardComponent implements OnInit, OnDestroy {
  stats: DashboardStats | null = null;
  todayAppointments: Appointment[] = [];
  doctorProfile: Doctor | null = null;
  isLoading = true;
  isUpdatingStatus = false;
  private subs = new Subscription();

  constructor(
    private dashboardService: DashboardService,
    private http: HttpService,
    private alert: AlertService,
    private auth: AuthService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadDashboardData();
    this.subs.add(
      this.notificationService.sysEvents$.subscribe(event => {
        if (event && event.action && event.action.startsWith('appointment')) {
          this.loadDashboardData();
        }
      })
    );
  }
  
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  loadProfile(): void {
    this.http.getDoctorProfile().subscribe({
      next: (data) => { this.doctorProfile = data; },
      error: () => {}
    });
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.loadTodayAppointments();
      },
      error: () => { this.isLoading = false; }
    });
  }

  loadTodayAppointments(): void {
    this.http.getAppointmentByDoctor().subscribe({
      next: (data) => {
        // THE FIX: Use substring(0, 10) to guarantee a pure string output (YYYY-MM-DD)
        const todayStr = new Date().toISOString().substring(0, 10);
        
        this.todayAppointments = data.filter(apt => apt.appointmentTime.startsWith(todayStr))
                                    .slice(0, 5); // top 5
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  updateAvailability(status: string): void {
    // Prevent spam clicking
    if (this.isUpdatingStatus) return;
    
    this.isUpdatingStatus = true;
    this.http.updateDoctorAvailability(status).subscribe({
      next: () => {
        if (this.doctorProfile) {
          // Update locally so Angular doesn't refresh the whole page
          this.doctorProfile.availability = status;
        }
        
        // Fire the newly fixed, long-lasting alert
        this.alert.success(`Availability successfully updated to ${status}`);
        this.isUpdatingStatus = false;
      },
      error: () => {
        this.alert.error('Failed to update availability. Please try again.');
        this.isUpdatingStatus = false;
      }
    });
  }
}