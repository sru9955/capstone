import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../../../core/services/dashboard.service';
import { HttpService } from '../../../core/services/http.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  stats: any = null;
  isLoading = true;

  constructor(
    private dashboardService: DashboardService,
    private http: HttpService
  ) {}

  ngOnInit(): void {
    this.loadAdminStats();
  }

  loadAdminStats(): void {
    this.isLoading = true;
    this.http.getAdminDashboard().subscribe({
      next: (data) => {
        this.stats = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  getObjectKeys(obj: any): string[] {
    return obj ? Object.keys(obj) : [];
  }
}
