import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DashboardStats } from '../../shared/models/dashboard.model';
import { HttpService } from './http.service';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  constructor(private http: HttpService, private auth: AuthService) {}

  getStats(): Observable<DashboardStats> {
    const role = this.auth.getRole();
    if (role === 'PATIENT') return this.http.getPatientDashboard();
    if (role === 'DOCTOR') return this.http.getDoctorDashboard();
    return this.http.getReceptionistDashboard();
  }
}
