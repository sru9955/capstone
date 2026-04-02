import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../../core/services/http.service';
import { AlertService } from '../../../core/services/alert.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Subscription } from 'rxjs';
import { Appointment } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-doctor-appointments',
  templateUrl: './doctor-appointments.component.html',
  styleUrls: ['./doctor-appointments.component.scss']
})
export class DoctorAppointmentsComponent implements OnInit, OnDestroy {
  appointments: Appointment[] = [];
  isLoading = true;
  dateForm = new FormGroup({
    startDate: new FormControl(''),
    endDate: new FormControl('')
  });
  private subs = new Subscription();

  constructor(
    private http: HttpService,
    private alert: AlertService,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
    this.subs.add(
      this.notificationService.sysEvents$.subscribe(event => {
        if (event && event.action && event.action.startsWith('appointment')) {
          this.loadAppointments();
        }
      })
    );
  }
  
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  loadAppointments(): void {
    this.isLoading = true;
    this.http.getAppointmentByDoctor().subscribe({
      next: (data) => {
        this.appointments = data.reverse();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  filterResults(): void {
    const { startDate, endDate } = this.dateForm.value;
    const startDateTime = startDate ? `${startDate}T00:00:00` : '';
    const endDateTime = endDate ? `${endDate}T23:59:59` : '';

    this.isLoading = true;
    this.http.getDoctorAppointmentsByDateRange(startDateTime, endDateTime).subscribe({
      next: (data) => {
        this.appointments = data.reverse();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  resetFilter(): void {
    this.dateForm.reset();
    this.loadAppointments();
  }

  updateStatus(id: number, status: string): void {
    this.http.updateAppointmentStatus(id, status).subscribe({
      next: () => {
        this.alert.success('Status updated to ' + status);
        this.loadAppointments();
      }
    });
  }

  viewHistory(patientId: number): void {
    this.router.navigate(['/doctor/patient-history', patientId]);
  }

  addMedicalRecord(apt: Appointment): void {
    this.router.navigate(['/doctor/add-medical-record', apt.id], {
      queryParams: { patientId: apt.patient.id, patientName: apt.patient.name }
    });
  }
}
