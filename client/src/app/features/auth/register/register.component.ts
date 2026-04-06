import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
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

  // Password rules visibility
  showRules = false;
  private passwordFocused = false;

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

    // default role validators (PATIENT -> none for specialty/availability)
    this.selectRole(this.selectedRole);
  }

  get f() {
    return this.registerForm.controls;
  }

  /* ===========================
     VALIDATION VISIBILITY HELPERS
     =========================== */

  private ctrl(name: string): AbstractControl {
    return this.registerForm.get(name) as AbstractControl;
  }

  // show error only after user touched that control
  shouldShowError(name: string): boolean {
    const c = this.ctrl(name);
    return !!c && c.touched && c.invalid;
  }

  // show a specific error only after touched
  hasError(name: string, error: string): boolean {
    const c = this.ctrl(name);
    return !!c && c.touched && c.hasError(error);
  }

  // password rules panel: show only if password touched (optionally also when focused)
  shouldShowPasswordRules(): boolean {
    const c = this.ctrl('password');
    return !!c && (c.touched || this.passwordFocused) && !this.isPasswordEmpty();
  }

  private isPasswordEmpty(): boolean {
    return ((this.f['password']?.value ?? '') as string).length === 0;
  }

  onPasswordFocus(): void {
    this.passwordFocused = true;
    this.updatePasswordRulesVisibility();
  }

  onPasswordBlur(): void {
    this.passwordFocused = false;
    this.f['password']?.markAsTouched();
    this.updatePasswordRulesVisibility();
  }

  onPasswordInput(): void {
    this.updatePasswordRulesVisibility();
  }

  private updatePasswordRulesVisibility(): void {
    const c = this.ctrl('password');
    this.showRules = !!c && (c.touched || this.passwordFocused) && !this.isPasswordEmpty();
  }

  /* ===== Password Validation Helpers ===== */

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

  /* =========================== */

  selectRole(role: string): void {
    this.selectedRole = role;

    const specialtyCtrl = this.registerForm.get('specialty');
    const availabilityCtrl = this.registerForm.get('availability');

    if (!specialtyCtrl || !availabilityCtrl) return;

    if (role === 'DOCTOR') {
      specialtyCtrl.setValidators([Validators.required]);
      availabilityCtrl.setValidators([Validators.required]);
    } else {
      specialtyCtrl.clearValidators();
      availabilityCtrl.clearValidators();

      // optional: reset values when not doctor
      specialtyCtrl.setValue('');
      availabilityCtrl.setValue('');

      // important: also reset touched state so errors don't show when switching roles
      specialtyCtrl.markAsUntouched();
      specialtyCtrl.markAsPristine();
      availabilityCtrl.markAsUntouched();
      availabilityCtrl.markAsPristine();
    }

    specialtyCtrl.updateValueAndValidity();
    availabilityCtrl.updateValueAndValidity();
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      // this will show validators for all fields ONLY after submit attempt
      // (if you want strictly only user touch, remove markAllAsTouched)
      this.registerForm.markAllAsTouched();
      this.updatePasswordRulesVisibility();
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