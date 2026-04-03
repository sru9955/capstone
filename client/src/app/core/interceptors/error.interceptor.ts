import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { AlertService } from '../services/alert.service';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private auth: AuthService,
    private alert: AlertService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let message = 'Something went wrong. Please try again.';
        if (error.status === 0) {
          message = 'Cannot connect to server. Please check backend is running.';
        } else if (error.status === 400) {
          message = error.error?.message || 'Invalid request. Please check your inputs.';
        } else if (error.status === 401) {
          message = 'Session expired. Please login again.';
          this.auth.logout();
          this.router.navigate(['/login']);
        } else if (error.status === 403) {
          if (request.url.includes('/api/notifications')) {
             return throwError(() => error); // Suppress popup for background polling
          }
          message = 'Access denied. You do not have permission.';
        } else if (error.status === 404) {
          message = error.error?.message || 'Resource not found.';
        } else if (error.status === 500) {
          message = error.error?.message || 'Server error. Please try again later.';
        }
        console.error('HTTP Error:', error);
        this.alert.error(message);
        return throwError(() => error);
      })
    );
  }
}
