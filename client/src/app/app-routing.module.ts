import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard, PatientGuard, DoctorGuard, ReceptionistGuard, AdminGuard } from './core/guards/auth.guard';

import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LandingPageComponent } from './features/landing/landing.component';
import { PatientDashboardComponent } from './features/patient/dashboard/patient-dashboard.component';
import { PatientAppointmentsComponent } from './features/patient/appointments/patient-appointment.componet';
import { MedicalRecordsComponent } from './features/patient/medical-records/medical-records.component';
import { DoctorDashboardComponent } from './features/doctor/dashboard/doctor-dashboard.component';
import { DoctorAppointmentsComponent } from './features/doctor/appointments/doctor-appointments.component';
import { DoctorAddRecordComponent } from './features/doctor/add-medical-record/doctor-add-record.component';
import { DoctorPatientHistoryComponent } from './features/doctor/patient-history/doctor-patient-history.component';
import { ReceptionistDashboardComponent } from './features/receptionist/dashboard/receptionist-dashboard.component';
import { ReceptionistAppointmentsComponent } from './features/receptionist/appointments/receptionist-appointemnts';
import { ReceptionistPatientHistoryComponent } from './features/receptionist/patient-history/receptionist-patient-history.component';
import { AdminDashboardComponent } from './features/admin/dashboard/admin-dashboard.component';
import { ProfileComponent } from './features/profile/profile.component';
import { PrescriptionPrintComponent } from './features/prescription-print/prescription-print.component';
import { AdminAppointmentsComponent } from './features/admin/appointments/admin-appointments/admin-appointments.component';

const routes: Routes = [
  { path: '', component: LandingPageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'patient',
    canActivate: [AuthGuard, PatientGuard],
    children: [
      { path: 'dashboard', component: PatientDashboardComponent },
      { path: 'appointments', component: PatientAppointmentsComponent },
      { path: 'medical-records', component: MedicalRecordsComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  {
    path: 'doctor',
    canActivate: [AuthGuard, DoctorGuard],
    children: [
      { path: 'dashboard', component: DoctorDashboardComponent },
      { path: 'appointments', component: DoctorAppointmentsComponent },
      { path: 'add-medical-record/:id', component: DoctorAddRecordComponent },
      { path: 'patient-history/:id', component: DoctorPatientHistoryComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  {
    path: 'receptionist',
    canActivate: [AuthGuard, ReceptionistGuard],
    children: [
      { path: 'dashboard', component: ReceptionistDashboardComponent },
      { path: 'appointments', component: ReceptionistAppointmentsComponent },
      { path: 'patient-history/:id', component: ReceptionistPatientHistoryComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  {
    path: 'admin',
    canActivate: [AuthGuard, AdminGuard],
    children: [
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'appointments', component: AdminAppointmentsComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'print/prescription/:id', component: PrescriptionPrintComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
