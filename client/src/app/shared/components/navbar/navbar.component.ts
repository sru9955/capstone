import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Notification } from '../../models/notification.model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  showNotifications = false;
  showProfileMenu = false;
  notifications: Notification[] = [];
  unreadCount = 0;
  currentUser: any;

  patientLinks = [
    { label: 'Dashboard', route: '/patient/dashboard' },
    { label: 'My Appointments', route: '/patient/appointments' },
    { label: 'Medical Records', route: '/patient/medical-records' },
  ];

  doctorLinks = [
    { label: 'Dashboard', route: '/doctor/dashboard' },
    { label: 'Appointments', route: '/doctor/appointments' },
  ];

  receptionistLinks = [
    { label: 'Dashboard', route: '/receptionist/dashboard' },
    { label: 'All Appointments', route: '/receptionist/appointments' },
  ];

  adminLinks = [
    { label: 'Analytics', route: '/admin/dashboard' },
    { label: 'Appointment Reports', route: '/admin/appointments' },
  ];

  constructor(public auth: AuthService, public notifService: NotificationService, private router: Router) {}

  ngOnInit(): void {
    this.currentUser = this.auth.getCurrentUser();
    this.notifService.notifications$.subscribe(n => this.notifications = n);
    this.notifService.unreadCount$.subscribe(c => this.unreadCount = c);
  }

  get navLinks() {
    if (this.auth.isPatient()) return this.patientLinks;
    if (this.auth.isDoctor()) return this.doctorLinks;
    if (this.auth.isReceptionist()) return this.receptionistLinks;
    if (this.auth.isAdmin()) return this.adminLinks;
    return [];
  }

  isActive(route: string): boolean {
    return this.router.url.startsWith(route);
  }

  toggleNotifications(): void { 
    this.showNotifications = !this.showNotifications; 
    this.showProfileMenu = false;
  }
  
  toggleProfileMenu(): void {
    this.showProfileMenu = !this.showProfileMenu;
    this.showNotifications = false;
  }

  markRead(id: number): void { this.notifService.markAsRead(id); }

  markAllRead(): void { this.notifService.markAllRead(); this.showNotifications = false; }

  logout(): void { this.auth.logout(); }
  
  viewProfile(): void {
    this.router.navigate(['/profile']);
    this.showProfileMenu = false;
  }

  getTimeAgo(dateStr: string): string {
    const diff = Date.now() - new Date(dateStr).getTime();
    const mins = Math.floor(diff / 60000);
    if (mins < 1) return 'Just now';
    if (mins < 60) return `${mins}m ago`;
    const hrs = Math.floor(mins / 60);
    if (hrs < 24) return `${hrs}h ago`;
    return `${Math.floor(hrs / 24)}d ago`;
  }
}

