import { Component, OnInit } from '@angular/core';
import { LoaderService } from '../../../core/services/loader.service';

@Component({
  selector: 'app-loader',
  template: `
    <div class="loader-overlay" *ngIf="loading$ | async">
      <div class="loader-spinner">
        <div class="spinner-ring"></div>
        <div class="spinner-ring"></div>
        <div class="spinner-ring"></div>
        <p class="loader-text">Loading...</p>
      </div>
    </div>
  `,
  styles: [`
    .loader-overlay {
      position: fixed; inset: 0;
      background: rgba(15, 12, 41, 0.8);
      backdrop-filter: blur(4px);
      display: flex; align-items: center; justify-content: center;
      z-index: 9999;
    }
    .loader-spinner {
      display: flex; flex-direction: column; align-items: center; gap: 0.5rem;
    }
    .spinner-ring {
      position: absolute;
      width: 60px; height: 60px;
      border-radius: 50%;
      border: 3px solid transparent;
      animation: spin 1.2s cubic-bezier(0.5,0,0.5,1) infinite;
    }
    .spinner-ring:nth-child(1) { border-top-color: #6366f1; animation-delay: -0.45s; }
    .spinner-ring:nth-child(2) { width: 48px; height: 48px; border-top-color: #06b6d4; animation-delay: -0.3s; }
    .spinner-ring:nth-child(3) { width: 36px; height: 36px; border-top-color: #10b981; animation-delay: -0.15s; }
    .loader-text { margin-top: 4.5rem; color: #94a3b8; font-size: 0.85rem; font-weight: 500; }
    @keyframes spin { to { transform: rotate(360deg); } }
  `]
})
export class LoaderComponent {
  loading$ = this.loader.loading$;
  constructor(private loader: LoaderService) {}
}
