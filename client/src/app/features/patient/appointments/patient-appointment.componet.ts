import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { HttpService } from '../../../core/services/http.service';
import { AlertService } from '../../../core/services/alert.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Appointment, Doctor } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-patient-appointments',
  templateUrl: './patient-appointments.component.html',
  styleUrls: ['./patient-appointments.component.scss']
})
export class PatientAppointmentsComponent implements OnInit, OnDestroy {
  appointments: Appointment[] = [];
  doctors: Doctor[] = [];
  isLoading = true;
  
  showModal = false;
  bookingForm!: FormGroup;
  isBooking = false;

  doctorSearchCtrl = new FormControl('');
  private subs = new Subscription();

  constructor(
    private http: HttpService,
    private alert: AlertService,
    private fb: FormBuilder,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
    this.loadDoctors();
    
    this.bookingForm = this.fb.group({
      doctorId: ['', Validators.required],
      appointmentTime: ['', Validators.required],
      notes: ['']
    });

    this.subs.add(
      this.doctorSearchCtrl.valueChanges.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => term ? this.http.searchPatientDoctors(term) : this.http.getDoctors())
      ).subscribe(data => this.doctors = data)
    );
    
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
    this.http.getAppointmentByPatient().subscribe({
      next: (data) => {
        this.appointments = data.reverse();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  loadDoctors(): void {
    this.http.getDoctors().subscribe({
      next: (data) => { this.doctors = data; },
      error: () => {}
    });
  }

  openBookingModal(): void {
    this.bookingForm.reset();
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  bookAppointment(): void {
    if (this.bookingForm.invalid) {
      this.bookingForm.markAllAsTouched();
      return;
    }
    
    this.isBooking = true;
    const { doctorId, appointmentTime, notes } = this.bookingForm.value;
    
    this.http.scheduleAppointment(doctorId, { appointmentTime, notes }).subscribe({
      next: (res) => {
        this.alert.success('Appointment booked successfully!');
        this.isBooking = false;
        this.closeModal();
        this.loadAppointments(); // Reload the list
      },
      error: (err) => {
        this.alert.error('Failed to book appointment.');
        this.isBooking = false;
      }
    });
  }
}
