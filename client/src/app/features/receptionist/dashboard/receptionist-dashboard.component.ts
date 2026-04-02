import { Component, OnInit, OnDestroy } from '@angular/core';
import { DashboardService } from '../../../core/services/dashboard.service';
import { HttpService } from '../../../core/services/http.service';
import { DashboardStats } from '../../../shared/models/dashboard.model';
import { NotificationService } from '../../../core/services/notification.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-receptionist-dashboard',
  templateUrl: './receptionist-dashboard.component.html',
  styleUrls: ['./receptionist-dashboard.component.scss']
})
export class ReceptionistDashboardComponent implements OnInit, OnDestroy {
  stats: DashboardStats | null = null;
  isLoading = true;
  pendingPayments: any[] = [];
  isPaying = false;
  private subs = new Subscription();

  constructor(
    private dashboardService: DashboardService,
    private http: HttpService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.isLoading = true;
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
    this.loadPayments();
    
    this.subs.add(
      this.notificationService.sysEvents$.subscribe(event => {
        if (event && event.action && event.action.startsWith('appointment')) {
          this.loadData();
        }
      })
    );
  }
  
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  loadData(): void {
    this.dashboardService.getStats().subscribe(data => this.stats = data);
    this.loadPayments();
  }

  loadPayments(): void {
    this.http.getPayments().subscribe({
      next: (data) => {
        this.pendingPayments = data.filter(p => p.status !== 'PAID');
      },
      error: () => {}
    });
  }

  markPaid(id: number): void {
    this.isPaying = true;
    this.http.markPaymentPaid(id).subscribe({
      next: () => {
        this.loadPayments();
        this.isPaying = false;
      },
      error: () => { this.isPaying = false; }
    });
  }
}
