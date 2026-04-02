import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpService } from '../../../core/services/http.service';
import { AuthService } from '../../../core/services/auth.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  isLoading = false;
  showPassword = false;
  targetRole = '';

  constructor(
    private fb: FormBuilder,
    private http: HttpService,
    private auth: AuthService,
    private alert: AlertService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    if (this.auth.isLoggedIn()) this.redirectByRole();
    
    this.route.queryParams.subscribe(params => {
      if (params['role']) {
        this.targetRole = params['role'];
      }
    });

    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get f() { return this.loginForm.controls; }

  onSubmit(): void {
    if (this.loginForm.invalid) { this.loginForm.markAllAsTouched(); return; }
    this.isLoading = true;
    this.http.login(this.loginForm.value).subscribe({
      next: (res) => {
        this.auth.login(res);
        this.alert.success('Welcome back, ' + res.username + '!');
        this.redirectByRole(res.role);
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  private redirectByRole(role?: string): void {
    const r = role || this.auth.getRole();
    if (r === 'PATIENT') this.router.navigate(['/patient/dashboard']);
    else if (r === 'DOCTOR') this.router.navigate(['/doctor/dashboard']);
    else if (r === 'RECEPTIONIST') this.router.navigate(['/receptionist/dashboard']);
    else if (r === 'ADMIN') this.router.navigate(['/admin/dashboard']);
  }
}
