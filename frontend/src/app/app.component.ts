import { Component, OnInit } from '@angular/core';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'E-Commerce Platform';

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    // Initialize auth state from localStorage
    this.authService.checkAuthStatus();
  }
}
