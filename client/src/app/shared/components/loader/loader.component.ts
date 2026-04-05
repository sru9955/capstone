import { Component, OnInit } from '@angular/core';
import { LoaderService } from '../../../core/services/loader.service';

@Component({
  selector: 'app-loader',
  template: `
    <div class="loader-overlay" *ngIf="loading$ | async">
      <div class="loader-spinner">
        <svg xmlns="http://www.w3.org/2000/svg" class="beating-heart" viewBox="0 0 24 24" fill="#ef4444" stroke="#ef4444" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
          <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
        </svg>
        <p class="loader-text">Loading...</p>
      </div>
    </div>
  `,
  styles: [`
    .loader-overlay {
      position: fixed; inset: 0;
      background: rgba(15, 12, 41, 0.85);
      backdrop-filter: blur(5px);
      display: flex; align-items: center; justify-content: center;
      z-index: 9999;
    }
    .loader-spinner {
      display: flex; flex-direction: column; align-items: center; gap: 1rem;
    }
    
    /* Heart styling with glowing effect */
    .beating-heart {
      width: 65px; 
      height: 65px;
      filter: drop-shadow(0 0 15px rgba(239, 68, 68, 0.5));
      /* Realistic "lub-dub" heartbeat timing */
      animation: heartbeat 1.2s infinite ease-in-out;
    }

    .loader-text { 
      color: #cbd5e1; 
      font-size: 0.9rem; 
      font-weight: 600; 
      letter-spacing: 1.5px;
      text-transform: uppercase;
      animation: pulseText 1.2s infinite ease-in-out;
    }

    /* Double-beat (lub-dub) animation */
    @keyframes heartbeat {
      0%   { transform: scale(1); }
      15%  { transform: scale(1.25); }
      30%  { transform: scale(1); }
      45%  { transform: scale(1.25); }
      60%  { transform: scale(1); }
      100% { transform: scale(1); }
    }

    /* Soft text fade to match the beat */
    @keyframes pulseText {
      0%, 100% { opacity: 0.5; }
      50% { opacity: 1; text-shadow: 0 0 10px rgba(255,255,255,0.3); }
    }
  `]
})
export class LoaderComponent {
  loading$ = this.loader.loading$;
  constructor(private loader: LoaderService) {}
}