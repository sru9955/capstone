import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
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
        
        let cleanUrl = event.urlAfterRedirects;
        
        // 1. Safely remove query parameters (like ?role=PATIENT)
        if (cleanUrl.indexOf('?') !== -1) {
          cleanUrl = cleanUrl.substring(0, cleanUrl.indexOf('?'));
        }
        
        // 2. Safely remove fragments (like #roles)
        if (cleanUrl.indexOf('#') !== -1) {
          cleanUrl = cleanUrl.substring(0, cleanUrl.indexOf('#'));
        }
        
        // 3. Safely check the clean URL!
        this.showLayout = !cleanUrl.includes('/login') && 
                          !cleanUrl.includes('/register') && 
                          cleanUrl !== '/' && 
                          cleanUrl !== '/home';
                          
        if (this.auth.isLoggedIn() && this.showLayout) {
          this.notificationService.startPolling();
        }
      }
    });
  }
}