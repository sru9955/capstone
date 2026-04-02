import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest } from '../../shared/models/user.model';
import { Appointment, AppointmentDto, Doctor, MedicalRecord, MedicalRecordDto } from '../../shared/models/appointment.model';
import { DashboardStats } from '../../shared/models/dashboard.model';
import { Notification } from '../../shared/models/notification.model';

@Injectable({ providedIn: 'root' })
export class HttpService {
  private api = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // ─── AUTH ──────────────────────────────────────────────
  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.api}/user/login`, request);
  }

  registerPatient(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.api}/patient/register`, request);
  }

  registerDoctor(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.api}/doctor/register`, request);
  }

  registerReceptionist(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.api}/receptionist/register`, request);
  }

  // ─── PATIENT ───────────────────────────────────────────
  getDoctors(): Observable<Doctor[]> {
    return this.http.get<Doctor[]>(`${this.api}/patient/doctors`);
  }

  searchPatientDoctors(name: string): Observable<Doctor[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<Doctor[]>(`${this.api}/patient/doctors/search`, { params });
  }

  scheduleAppointment(doctorId: number, dto: AppointmentDto): Observable<Appointment> {
    const formattedDto = { ...dto, appointmentTime: this.formatTime(dto.appointmentTime) };
    const params = new HttpParams().set('doctorId', doctorId.toString());
    return this.http.post<Appointment>(`${this.api}/patient/appointment`, formattedDto, { params });
  }

  getAppointmentByPatient(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.api}/patient/appointments`);
  }

  getPatientProfile(): Observable<any> {
    return this.http.get<any>(`${this.api}/patient/profile`);
  }

  getMedicalRecords(): Observable<MedicalRecord[]> {
    return this.http.get<MedicalRecord[]>(`${this.api}/patient/medicalrecords`);
  }

  getMedicalRecordById(id: number): Observable<MedicalRecord> {
    return this.http.get<MedicalRecord>(`${this.api}/medical-records/${id}`);
  }

  // ─── DOCTOR ────────────────────────────────────────────
  getAppointmentByDoctor(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.api}/doctor/appointments`);
  }

  updateDoctorAvailability(availability: string): Observable<any> {
    return this.http.post(`${this.api}/doctor/availability`, { availability });
  }

  getDoctorProfile(): Observable<Doctor> {
    return this.http.get<Doctor>(`${this.api}/doctor/profile`);
  }

  updateAppointmentStatus(id: number, status: string): Observable<Appointment> {
    const params = new HttpParams().set('status', status);
    return this.http.put<Appointment>(`${this.api}/doctor/appointments/${id}/status`, null, { params });
  }

  addMedicalRecord(appointmentId: number, dto: MedicalRecordDto): Observable<MedicalRecord> {
    return this.http.post<MedicalRecord>(`${this.api}/doctor/appointments/${appointmentId}/medicalrecord`, dto);
  }

  getPatientHistory(patientId: number): Observable<MedicalRecord[]> {
    return this.http.get<MedicalRecord[]>(`${this.api}/doctor/patients/${patientId}/history`);
  }

  // ─── RECEPTIONIST ──────────────────────────────────────
  getAllAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(`${this.api}/receptionist/appointments`);
  }

  getReceptionistPatients(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/receptionist/patients`);
  }

  searchReceptionistPatients(name: string): Observable<any[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<any[]>(`${this.api}/receptionist/patients/search`, { params });
  }

  searchReceptionistDoctors(name: string): Observable<Doctor[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<Doctor[]>(`${this.api}/receptionist/doctors/search`, { params });
  }

  getReceptionistMedicalRecords(): Observable<MedicalRecord[]> {
    return this.http.get<MedicalRecord[]>(`${this.api}/receptionist/medical-records`);
  }

  getReceptionistPatientHistory(patientId: number): Observable<MedicalRecord[]> {
    return this.http.get<MedicalRecord[]>(`${this.api}/receptionist/patients/${patientId}/history`);
  }

  scheduleAppointmentByReceptionist(dto: AppointmentDto): Observable<Appointment> {
    const formattedDto = { ...dto, appointmentTime: this.formatTime(dto.appointmentTime) };
    return this.http.post<Appointment>(`${this.api}/receptionist/appointment`, formattedDto);
  }

  reScheduleAppointment(id: number, dto: AppointmentDto): Observable<Appointment> {
    const formattedDto = { ...dto, appointmentTime: this.formatTime(dto.appointmentTime) };
    return this.http.put<Appointment>(`${this.api}/receptionist/appointment-reschedule/${id}`, formattedDto);
  }

  // ─── NOTIFICATIONS ─────────────────────────────────────
  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.api}/notifications`);
  }

  getUnreadCount(): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.api}/notifications/unread-count`);
  }

  markNotificationRead(id: number): Observable<Notification> {
    return this.http.put<Notification>(`${this.api}/notifications/${id}/read`, {});
  }

  markAllRead(): Observable<any> {
    return this.http.put(`${this.api}/notifications/mark-all-read`, {});
  }

  // ─── DASHBOARD ─────────────────────────────────────────
  getPatientDashboard(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.api}/dashboard/patient`);
  }

  getDoctorDashboard(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.api}/dashboard/doctor`);
  }

  getReceptionistDashboard(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.api}/dashboard/receptionist`);
  }

  getAdminDashboard(): Observable<any> {
    return this.http.get<any>(`${this.api}/dashboard/admin`);
  }

  getAdminAppointmentsByDateRange(startDate: string, endDate: string): Observable<Appointment[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    return this.http.get<Appointment[]>(`${this.api}/admin/appointments/filter`, { params });
  }

  getDoctorAppointmentsByDateRange(startDate: string, endDate: string): Observable<Appointment[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    return this.http.get<Appointment[]>(`${this.api}/doctor/appointments/filter`, { params });
  }

  getReceptionistAppointmentsByDateRange(startDate: string, endDate: string): Observable<Appointment[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    return this.http.get<Appointment[]>(`${this.api}/receptionist/appointments/filter`, { params });
  }

  // ─── PAYMENTS ────────────────────────────────────────────
  getPayments(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/payments`);
  }

  markPaymentPaid(id: number): Observable<any> {
    return this.http.post<any>(`${this.api}/payments/${id}/pay`, {});
  }

  // ─── CHAT ──────────────────────────────────────────────
  sendChatMessage(message: string, role: string | null): Observable<{ reply: string }> {
    return this.http.post<{ reply: string }>(`${this.api}/chat/process`, { message, role });
  }

  // ─── PROFILE ───────────────────────────────────────────
  getProfile(): Observable<any> {
    return this.http.get<any>(`${this.api}/profiles/my-profile`);
  }

  getPatients(): Observable<any[]> {
    return this.http.get<any[]>(`${this.api}/receptionist/patients`);
  }

  updateProfile(profileData: any): Observable<any> {
    return this.http.put<any>(`${this.api}/profiles/my-profile`, profileData);
  }

  uploadProfileImage(file: File): Observable<{ imageUrl: string }> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<{ imageUrl: string }>(`${this.api}/profiles/image`, formData);
  }

  private formatTime(time: string): string {
    if (!time) return time;
    let formatted = time.replace('T', ' ');
    if (formatted.split(':').length === 2) {
      formatted += ':00';
    }
    return formatted;
  }
}
