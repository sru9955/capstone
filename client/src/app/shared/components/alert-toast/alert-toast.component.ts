import { Component, OnInit, OnDestroy } from '@angular/core';
import { AlertService, Alert } from '../../../core/services/alert.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-alert-toast',
  template: `
    <div class="toast-container">
      <div *ngFor="let alert of alerts" class="toast" [class]="'toast-' + alert.type" [id]="'toast-' + alert.id">
        <span class="toast-icon">{{ getIcon(alert.type) }}</span>
        <span class="toast-msg">{{ alert.message }}</span>
        <button class="toast-close" (click)="remove(alert.id)">✕</button>
        <div class="toast-progress"></div>
      </div>
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed; top: 1.5rem; right: 1.5rem;
      z-index: 999999; display: flex; flex-direction: column; gap: 0.75rem;
      max-width: 380px; pointer-events: none;
    }
    .toast {
      pointer-events: auto;
      display: flex; align-items: center; gap: 0.75rem;
      padding: 1rem 1.25rem; border-radius: 12px;
      backdrop-filter: blur(16px);
      border: 1px solid; 
      /* Ensures the toast stays for EXACTLY 7s, then fades out */
      animation: slideIn 0.4s ease-out forwards, fadeOut 0.4s ease-in 6.6s forwards;
      box-shadow: 0 8px 32px rgba(0,0,0,0.3);
      font-size: 0.875rem; font-weight: 500;
      position: relative; overflow: hidden;
      opacity: 1;
    }
    .toast-progress {
      position: absolute; bottom: 0; left: 0; height: 4px; 
      background: currentColor; opacity: 0.4;
      animation: shrink 7s linear forwards; 
    }
    @keyframes shrink {
      from { width: 100%; }
      to { width: 0%; }
    }
    @keyframes slideIn { 
      from { transform: translateX(100%); opacity: 0; } 
      to { transform: translateX(0); opacity: 1; } 
    }
    @keyframes fadeOut {
      from { opacity: 1; transform: translateY(0); }
      to { opacity: 0; transform: translateY(-10px); }
    }
    .toast-success { background: rgba(16,185,129,0.15); border-color: rgba(16,185,129,0.3); color: #10b981; }
    .toast-error   { background: rgba(239,68,68,0.15);  border-color: rgba(239,68,68,0.3);  color: #ef4444; }
    .toast-info    { background: rgba(59,130,246,0.15);  border-color: rgba(59,130,246,0.3); color: #3b82f6; }
    .toast-warning { background: rgba(245,158,11,0.15); border-color: rgba(245,158,11,0.3); color: #f59e0b; }
    .toast-icon { font-size: 1.1rem; }
    .toast-msg { flex: 1; }
    .toast-close { background: none; border: none; cursor: pointer; color: inherit; opacity: 0.7; font-size: 0.8rem; padding: 0; }
    .toast-close:hover { opacity: 1; }
  `]
})
export class AlertToastComponent implements OnInit, OnDestroy {
  alerts: Alert[] = [];
  private sub!: Subscription;

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    this.sub = this.alertService.alerts$.subscribe(alert => {
      this.alerts.push(alert);
      
      // Javascript cleanup after the CSS animation finishes (7000ms)
      setTimeout(() => {
        this.remove(alert.id);
      }, 7000);
    });
  }

  ngOnDestroy(): void {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  remove(id: number): void {
    this.alerts = this.alerts.filter(a => a.id !== id);
  }

  getIcon(type: string): string {
    const icons: any = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
    return icons[type] || '📢';
  }
}