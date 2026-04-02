import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpService } from '../../../../core/services/http.service';
import { Appointment } from '../../../../shared/models/appointment.model';

@Component({
  selector: 'app-admin-appointments',
  templateUrl: './admin-appointments.component.html',
  styleUrls: ['./admin-appointments.component.scss']
})
export class AdminAppointmentsComponent implements OnInit {
  appointments: Appointment[] = [];
  isLoading = false;

  dateForm = new FormGroup({
    startDate: new FormControl(''),
    endDate: new FormControl('')
  });

  constructor(private http: HttpService) {}

  ngOnInit(): void {
    // Set default dates to month start and end
    const today = new Date();
    const start = new Date(today.getFullYear(), today.getMonth(), 1);
    const end = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    
    this.dateForm.patchValue({
      startDate: start.toISOString().split('T')[0],
      endDate: end.toISOString().split('T')[0]
    });
    
    this.loadAppointments();
  }

  loadAppointments(): void {
    const { startDate, endDate } = this.dateForm.value;
    
    // Add time boundary to fetch whole day
    const startDateTime = startDate ? `${startDate}T00:00:00` : '';
    const endDateTime = endDate ? `${endDate}T23:59:59` : '';

    this.isLoading = true;
    this.http.getAdminAppointmentsByDateRange(startDateTime, endDateTime).subscribe({
      next: (data) => {
        this.appointments = data.reverse();
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  filterResults(): void {
    this.loadAppointments();
  }

  resetFilter(): void {
    this.dateForm.reset();
    this.loadAppointments();
  }
}
