import { Component, OnInit, OnDestroy } from '@angular/core';
import { DashboardService } from '../../../core/services/dashboard.service';
import { HttpService } from '../../../core/services/http.service';
import { DashboardStats } from '../../../shared/models/dashboard.model';
import { Appointment } from '../../../shared/models/appointment.model';
import { NotificationService } from '../../../core/services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrls: ['./patient-dashboard.component.scss']
})
export class PatientDashboardComponent implements OnInit, OnDestroy {
  stats: DashboardStats | null = null;
  recentAppointments: Appointment[] = [];
  patientProfile: any = null;
  bmi: number | null = null;
  bmiStatus: string = '';
  isLoading = true;
  private subs = new Subscription();

  constructor(
    private dashboardService: DashboardService,
    private http: HttpService,
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
    this.http.getPatientProfile().subscribe({
       next: (profile) => {
         this.patientProfile = profile;
         if (profile.weight && profile.height) {
            const heightMeters = profile.height / 100;
            this.bmi = +(profile.weight / (heightMeters * heightMeters)).toFixed(1);
            if (this.bmi < 18.5) this.bmiStatus = 'Underweight (Eat more nutritious food)';
            else if (this.bmi < 25) this.bmiStatus = 'Normal Weight (Keep it up!)';
            else if (this.bmi < 30) this.bmiStatus = 'Overweight (Exercise regularly)';
            else this.bmiStatus = 'Obese (Consult a professional)';
         }
       }
    });
  }

  loadDashboardData(): void {
    this.isLoading = true;
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.loadRecentAppointments();
      },
      error: () => { this.isLoading = false; }
    });
  }

  loadRecentAppointments(): void {
    this.http.getAppointmentByPatient().subscribe({
      next: (data) => {
        this.recentAppointments = data.slice(0, 5); // Just show the top 5
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }
}
