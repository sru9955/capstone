import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Interceptors
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { LoaderInterceptor } from './core/interceptors/loader.interceptor';
import { ErrorInterceptor } from './core/interceptors/error.interceptor';

// Shared Components
import { LoaderComponent } from './shared/components/loader/loader.component';
import { AlertToastComponent } from './shared/components/alert-toast/alert-toast.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';

// Auth
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';

// Landing
import { LandingPageComponent } from './features/landing/landing.component';

// Patient
import { PatientDashboardComponent } from './features/patient/dashboard/patient-dashboard.component';
import { PatientAppointmentsComponent } from './features/patient/appointments/patient-appointment.componet';
import { MedicalRecordsComponent } from './features/patient/medical-records/medical-records.component';

// Doctor
import { DoctorDashboardComponent } from './features/doctor/dashboard/doctor-dashboard.component';
import { DoctorAppointmentsComponent } from './features/doctor/appointments/doctor-appointments.component';
import { DoctorAddRecordComponent } from './features/doctor/add-medical-record/doctor-add-record.component';
import { DoctorPatientHistoryComponent } from './features/doctor/patient-history/doctor-patient-history.component';

// Receptionist
import { ReceptionistDashboardComponent } from './features/receptionist/dashboard/receptionist-dashboard.component';
import { ReceptionistAppointmentsComponent } from './features/receptionist/appointments/receptionist-appointemnts';
import { ReceptionistPatientHistoryComponent } from './features/receptionist/patient-history/receptionist-patient-history.component';

// Admin
import { AdminDashboardComponent } from './features/admin/dashboard/admin-dashboard.component';

// Chatbot
import { ChatbotComponent } from './chatbot/chatbot.component';
import { ProfileComponent } from './features/profile/profile.component';
import { PrescriptionPrintComponent } from './features/prescription-print/prescription-print.component';
import { AdminAppointmentsComponent } from './features/admin/appointments/admin-appointments/admin-appointments.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingPageComponent,
    LoaderComponent,
    AlertToastComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    PatientDashboardComponent,
    PatientAppointmentsComponent,
    MedicalRecordsComponent,
    DoctorDashboardComponent,
    DoctorAppointmentsComponent,
    DoctorAddRecordComponent,
    DoctorPatientHistoryComponent,
    ReceptionistDashboardComponent,
    ReceptionistAppointmentsComponent,
    ReceptionistPatientHistoryComponent,
    AdminDashboardComponent,
    ChatbotComponent,
    ProfileComponent,
    PrescriptionPrintComponent,
    AdminAppointmentsComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
