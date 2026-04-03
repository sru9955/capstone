import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardService } from '../../../core/services/dashboard.service';
import { HttpService } from '../../../core/services/http.service';
import { Appointment, MedicalRecord, PrescriptionItem } from '../../../shared/models/appointment.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrls: ['./patient-dashboard.component.scss']
})
export class PatientDashboardComponent implements OnInit, OnDestroy {

  patientProfile: any = null;
  allAppointments: Appointment[] = [];
  recentAppointments: Appointment[] = [];
  medicalRecords: MedicalRecord[] = [];
  recentRecords: MedicalRecord[] = [];

  isLoading = true;
  private subs = new Subscription();

  latestVitals: MedicalRecord | null = null;

  bmi: number | null = null;
  bmiCategory = '';
  bmiStatus = '';
  bmiMarkerLeft = '0%';

  todayDate = new Date().toDateString();
  patientInitial = '?';

  constructor(
    private dashboardService: DashboardService,
    private http: HttpService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadAppointments();
    this.loadMedicalRecords();
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  get upcomingCount(): number {
    return this.allAppointments.filter(a => a?.status === 'SCHEDULED').length;
  }

  get completedCount(): number {
    return this.allAppointments.filter(a => a?.status === 'COMPLETED').length;
  }

  goToBookAppointment(): void {
    this.router.navigate(['/patient/appointments']);
  }

  goToAllAppointments(): void {
    this.router.navigate(['/patient/appointments']);
  }

  loadProfile(): void {
    this.http.getPatientProfile().subscribe(profile => {
      this.patientProfile = profile;
      this.patientInitial = profile?.name?.charAt(0)?.toUpperCase() || '?';
    });
  }

  loadAppointments(): void {
    this.http.getAppointmentByPatient().subscribe(data => {
      this.allAppointments = data;
      this.recentAppointments = data.slice(0, 5);
      this.isLoading = false;
    });
  }

  loadMedicalRecords(): void {
    this.http.getMedicalRecords().subscribe(records => {
      this.medicalRecords = records.sort(
        (a, b) =>
          new Date(b.recordDate).getTime() -
          new Date(a.recordDate).getTime()
      );

      this.recentRecords = this.medicalRecords.slice(0, 4);
      this.latestVitals = this.medicalRecords[0] || null;

      if (this.latestVitals?.weight && this.latestVitals?.height) {
        this.calculateBMI(
          this.latestVitals.weight,
          this.latestVitals.height
        );
      }
    });
  }

  calculateBMI(weight: number, height: number): void {
    const h = height / 100;
    const value = weight / (h * h);

    this.bmi = +value.toFixed(1);

    if (this.bmi < 18.5) {
      this.bmiCategory = 'Underweight';
      this.bmiStatus = 'Eat more healthy food';
    } else if (this.bmi < 25) {
      this.bmiCategory = 'Normal';
      this.bmiStatus = 'Good health';
    } else if (this.bmi < 30) {
      this.bmiCategory = 'Overweight';
      this.bmiStatus = 'Exercise recommended';
    } else {
      this.bmiCategory = 'Obese';
      this.bmiStatus = 'Consult doctor';
    }

    const pct = Math.min(Math.max((this.bmi - 10) / 30, 0), 1);
    this.bmiMarkerLeft = `${pct * 100}%`;
  }
}