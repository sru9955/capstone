import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { HttpService } from '../../../core/services/http.service';
import { AlertService } from '../../../core/services/alert.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Appointment, Doctor } from '../../../shared/models/appointment.model';

@Component({
  selector: 'app-receptionist-appointments',
  templateUrl: './receptionist-appointments.component.html',
  styleUrls: ['./receptionist-appointments.component.scss']
})
export class ReceptionistAppointmentsComponent implements OnInit, OnDestroy {
  appointments: Appointment[] = [];
  filteredAppointments: Appointment[] = [];
  doctors: Doctor[] = [];
  patients: any[] = [];
  isLoading = true;
  searchTerm = '';
  
  showModal = false;
  bookingForm!: FormGroup;
  dateForm = new FormGroup({
    startDate: new FormControl(''),
    endDate: new FormControl('')
  });
  isBooking = false;
  isRescheduleMode = false;
  editingAppointmentId: number | null = null;

  patientSearchCtrl = new FormControl('');
  doctorSearchCtrl = new FormControl('');
  private subs = new Subscription();

  constructor(
    private http: HttpService,
    private alert: AlertService,
    private fb: FormBuilder,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
    this.loadDoctors();
    this.loadPatients();
    
    this.bookingForm = this.fb.group({
      patientId: ['', Validators.required],
      doctorId: ['', Validators.required],
      appointmentTime: ['', Validators.required],
      notes: ['']
    });

    this.subs.add(
      this.patientSearchCtrl.valueChanges.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => term ? this.http.searchReceptionistPatients(term) : this.http.getReceptionistPatients())
      ).subscribe(data => this.patients = data)
    );

    this.subs.add(
      this.doctorSearchCtrl.valueChanges.pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => term ? this.http.searchReceptionistDoctors(term) : this.http.getDoctors())
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
    this.http.getAllAppointments().subscribe({
      next: (data) => {
        this.appointments = data.reverse();
        this.filteredAppointments = [...this.appointments];
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  filterAppointments(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredAppointments = this.appointments.filter(a => 
      a.patient?.name?.toLowerCase().includes(term) || false
    );
  }

  filterResults(): void {
    const { startDate, endDate } = this.dateForm.value;
    const startDateTime = startDate ? `${startDate}T00:00:00` : '';
    const endDateTime = endDate ? `${endDate}T23:59:59` : '';

    this.isLoading = true;
    this.http.getReceptionistAppointmentsByDateRange(startDateTime, endDateTime).subscribe({
      next: (data) => {
        this.appointments = data.reverse();
        this.filterAppointments(); // Apply text overlay
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  resetFilter(): void {
    this.dateForm.reset();
    this.searchTerm = '';
    this.loadAppointments();
  }

  loadDoctors(): void {
    this.http.getDoctors().subscribe({
      next: (data) => { this.doctors = data; },
      error: () => {}
    });
  }

  loadPatients(): void {
    this.http.getPatients().subscribe({
      next: (data: any[]) => { this.patients = data; },
      error: () => {}
    });
  }

  openBookingModal(): void {
    this.isRescheduleMode = false;
    this.editingAppointmentId = null;
    this.bookingForm.reset();
    
    this.bookingForm.get('patientId')?.setValidators(Validators.required);
    this.bookingForm.get('doctorId')?.setValidators(Validators.required);
    this.bookingForm.get('patientId')?.updateValueAndValidity();
    this.bookingForm.get('doctorId')?.updateValueAndValidity();
    
    this.showModal = true;
  }

  openRescheduleModal(apt: Appointment): void {
    this.isRescheduleMode = true;
    this.editingAppointmentId = apt.id;
    this.bookingForm.reset();
    
    this.bookingForm.get('patientId')?.clearValidators();
    this.bookingForm.get('doctorId')?.clearValidators();
    this.bookingForm.get('patientId')?.updateValueAndValidity();
    this.bookingForm.get('doctorId')?.updateValueAndValidity();

    let t = apt.appointmentTime; 
    if (t && typeof t === 'string' && t.includes(' ')) {
       t = t.substring(0, 16).replace(' ', 'T');
    }
    
    this.bookingForm.patchValue({
       patientId: apt.patient?.id || '',
       doctorId: apt.doctor?.id || '',
       appointmentTime: t,
       notes: apt.notes || ''
    });
    
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
    const payload = this.bookingForm.value;
    
    if (this.isRescheduleMode && this.editingAppointmentId) {
      this.http.reScheduleAppointment(this.editingAppointmentId, payload).subscribe({
        next: () => {
          this.alert.success('Appointment rescheduled!');
          this.isBooking = false;
          this.closeModal();
          this.loadAppointments();
        },
        error: () => {
          this.alert.error('Failed to reschedule.');
          this.isBooking = false;
        }
      });
    } else {
      this.http.scheduleAppointmentByReceptionist(payload).subscribe({
        next: () => {
          this.alert.success('Appointment booked successfully!');
          this.isBooking = false;
          this.closeModal();
          this.loadAppointments();
        },
        error: () => {
          this.alert.error('Failed to book appointment.');
          this.isBooking = false;
        }
      });
    }
  }

  viewHistory(patientId: number): void {
    this.router.navigate(['/receptionist/patient-history', patientId]);
  }
}
