import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface Alert {
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  id: number;
}

@Injectable({ providedIn: 'root' })
export class AlertService {
  private alertSubject = new Subject<Alert>();
  alerts$ = this.alertSubject.asObservable();
  private counter = 0;

  success(message: string): void { this.show(message, 'success'); }
  error(message: string): void { this.show(message, 'error'); }
  info(message: string): void { this.show(message, 'info'); }
  warning(message: string): void { this.show(message, 'warning'); }

  private show(message: string, type: Alert['type']): void {
    this.alertSubject.next({ message, type, id: ++this.counter });
  }
}
