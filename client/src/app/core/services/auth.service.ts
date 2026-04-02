import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { LoginResponse } from '../../shared/models/user.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUserSubject = new BehaviorSubject<LoginResponse | null>(this.getStoredUser());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private router: Router) {}

  login(response: LoginResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('user', JSON.stringify(response));
    this.currentUserSubject.next(response);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): LoginResponse | null {
    return this.currentUserSubject.value;
  }

  getRole(): string | null {
    return this.getCurrentUser()?.role ?? null;
  }

  getUserId(): number | null {
    return this.getCurrentUser()?.userId ?? null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  isPatient(): boolean { return this.getRole() === 'PATIENT'; }
  isDoctor(): boolean { return this.getRole() === 'DOCTOR'; }
  isReceptionist(): boolean { return this.getRole() === 'RECEPTIONIST'; }
  isAdmin(): boolean { return this.getRole() === 'ADMIN'; }

  private getStoredUser(): LoginResponse | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }
}
