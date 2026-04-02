import { Component, OnInit } from '@angular/core';
import { AlertService, Alert } from '../../../core/services/alert.service';

@Component({
  selector: 'app-alert-toast',
  template: `
    <div class="toast-container">
      <div *ngFor="let alert of alerts" class="toast" [class]="'toast-' + alert.type">
        <span class="toast-icon">{{ getIcon(alert.type) }}</span>
        <span class="toast-msg">{{ alert.message }}</span>
        <button class="toast-close" (click)="remove(alert.id)">✕</button>
      </div>
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed; top: 1.5rem; right: 1.5rem;
      z-index: 10000; display: flex; flex-direction: column; gap: 0.75rem;
      max-width: 380px;
    }
    .toast {
      display: flex; align-items: center; gap: 0.75rem;
      padding: 1rem 1.25rem; border-radius: 12px;
      backdrop-filter: blur(16px);
      border: 1px solid; animation: slideIn 0.35s ease;
      box-shadow: 0 8px 32px rgba(0,0,0,0.3);
      font-size: 0.875rem; font-weight: 500;
    }
    .toast-success { background: rgba(16,185,129,0.15); border-color: rgba(16,185,129,0.3); color: #10b981; }
    .toast-error   { background: rgba(239,68,68,0.15);  border-color: rgba(239,68,68,0.3);  color: #ef4444; }
    .toast-info    { background: rgba(59,130,246,0.15);  border-color: rgba(59,130,246,0.3); color: #3b82f6; }
    .toast-warning { background: rgba(245,158,11,0.15); border-color: rgba(245,158,11,0.3); color: #f59e0b; }
    .toast-icon { font-size: 1.1rem; }
    .toast-msg { flex: 1; }
    .toast-close { background: none; border: none; cursor: pointer; color: inherit; opacity: 0.7; font-size: 0.8rem; padding: 0; }
    .toast-close:hover { opacity: 1; }
    @keyframes slideIn { from { transform: translateX(100%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }
  `]
})
export class AlertToastComponent implements OnInit {
  alerts: Alert[] = [];

  constructor(private alertService: AlertService) {}

  ngOnInit(): void {
    this.alertService.alerts$.subscribe(alert => {
      this.alerts.push(alert);
      setTimeout(() => this.remove(alert.id), 4000);
    });
  }

  remove(id: number): void {
    this.alerts = this.alerts.filter(a => a.id !== id);
  }

  getIcon(type: string): string {
    const icons: any = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
    return icons[type] || '📢';
  }
}
