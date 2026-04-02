import { Injectable, OnDestroy, NgZone } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Notification } from '../../shared/models/notification.model';
import { HttpService } from './http.service';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class NotificationService implements OnDestroy {
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  private unreadCountSubject = new BehaviorSubject<number>(0);
  private sysEventsSubject = new BehaviorSubject<any>(null);

  notifications$ = this.notificationsSubject.asObservable();
  unreadCount$ = this.unreadCountSubject.asObservable();
  sysEvents$ = this.sysEventsSubject.asObservable();
  
  private eventSource?: EventSource;
  private reconnectTimeout?: any;

  constructor(private http: HttpService, private auth: AuthService, private zone: NgZone) {}

  startPolling(): void {
    if (this.eventSource) return;
    this.fetchNotifications();
    this.connectSse();
  }
  
  private connectSse(): void {
    if (!this.auth.isLoggedIn()) return;
    const token = this.auth.getToken();
    this.eventSource = new EventSource(`http://localhost:8080/api/notifications/stream?token=${token}`);
    
    this.eventSource.addEventListener('NOTIFICATION', (event: any) => {
      this.zone.run(() => {
        const payload = JSON.parse(event.data);
        const current = this.notificationsSubject.value;
        const updated = [payload, ...current];
        this.notificationsSubject.next(updated);
        this.unreadCountSubject.next(updated.filter(n => !n.isRead).length);
        
        if (Notification.permission === 'granted') {
          new window.Notification('Healthcare Portal', { body: payload.message });
        }
      });
    });

    this.eventSource.addEventListener('APPOINTMENT', (event: any) => {
      this.zone.run(() => {
        this.sysEventsSubject.next(JSON.parse(event.data));
      });
    });
    
    this.eventSource.onerror = (error) => {
      this.eventSource?.close();
      this.eventSource = undefined;
      // Reconnect after 5s
      this.reconnectTimeout = setTimeout(() => this.connectSse(), 5000);
    };
  }

  fetchNotifications(): void {
    if (!this.auth.isLoggedIn()) return;
    this.http.getNotifications().subscribe({
      next: (notifications) => {
        this.notificationsSubject.next(notifications);
        this.unreadCountSubject.next(notifications.filter(n => !n.isRead).length);
      },
      error: () => {}
    });
  }

  markAsRead(id: number): void {
    this.http.markNotificationRead(id).subscribe(() => this.fetchNotifications());
  }

  markAllRead(): void {
    this.http.markAllRead().subscribe(() => this.fetchNotifications());
  }

  stopPolling(): void {
    if (this.reconnectTimeout) clearTimeout(this.reconnectTimeout);
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = undefined;
    }
  }

  ngOnDestroy(): void { this.stopPolling(); }
}
