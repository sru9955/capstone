import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpService } from '../../../core/services/http.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  isLoading = false;
  selectedRole = 'PATIENT';
  showPassword = false;

  // for password dropdown
  showRules = false;

  roles = [
    { value: 'PATIENT', label: 'Patient', icon: 'assets/patient-icon.png', desc: 'Book appointments & view records' },
    { value: 'DOCTOR', label: 'Doctor', icon: 'assets/doctor-icon.png', desc: 'Manage schedule & appointments' },
    { value: 'RECEPTIONIST', label: 'Receptionist', icon: 'assets/receptionist-icon.png', desc: 'Manage all appointments' },
  ];

  specialties = [
    'Cardiology','Dermatology','Endocrinology','Gastroenterology','General Medicine',
    'Neurology','Oncology','Ophthalmology','Orthopedics','Pediatrics',
    'Psychiatry','Radiology','Surgery','Urology'
  ];

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private alert: AlertService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],

      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).+$/)
      ]],

      email: ['', [Validators.required, Validators.email]],
      name: ['', [Validators.required]],
      phone: [''],
      address: [''],
      specialty: [''],
      availability: [''],
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  /* ===== Password Validation Helpers (FIX) ===== */

  hasMinLength(): boolean {
    return (this.f['password'].value || '').length >= 8;
  }

  hasUpperCase(): boolean {
    return /[A-Z]/.test(this.f['password'].value || '');
  }

  hasLowerCase(): boolean {
    return /[a-z]/.test(this.f['password'].value || '');
  }

  hasNumber(): boolean {
    return /[0-9]/.test(this.f['password'].value || '');
  }

  hasSpecialChar(): boolean {
    return /[@$!%*?&]/.test(this.f['password'].value || '');
  }

  /* ============================================ */

  selectRole(role: string): void {
    this.selectedRole = role;

    if (role === 'DOCTOR') {
      this.registerForm.get('specialty')!.setValidators([Validators.required]);
      this.registerForm.get('availability')!.setValidators([Validators.required]);
    } else {
      this.registerForm.get('specialty')!.clearValidators();
      this.registerForm.get('availability')!.clearValidators();
    }

    this.registerForm.get('specialty')!.updateValueAndValidity();
    this.registerForm.get('availability')!.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    const payload = this.registerForm.value;

    let request$;
    if (this.selectedRole === 'PATIENT') {
      request$ = this.http.registerPatient(payload);
    } else if (this.selectedRole === 'DOCTOR') {
      request$ = this.http.registerDoctor(payload);
    } else {
      request$ = this.http.registerReceptionist(payload);
    }

    request$.subscribe({
      next: () => {
        this.alert.success('Registration successful! Please login.');
        this.router.navigate(['/login']);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }
}