import { Component, OnInit } from '@angular/core';
import { Router, NavigationStart, NavigationEnd } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { NotificationService } from './core/services/notification.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  showLayout = false;

  constructor(
    private router: Router,
    public auth: AuthService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const url = event.urlAfterRedirects;
        this.showLayout = !url.includes('/login') && !url.includes('/register') && url !== '/' && url !== '/home';
        if (this.auth.isLoggedIn() && this.showLayout) {
          this.notificationService.startPolling();
        }
      }
    });
  }
}
