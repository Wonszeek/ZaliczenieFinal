import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login() {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.email, this.password)
      .subscribe({
        next: (response) => {
          if (response?.success) {
            this.router.navigate(['/body']);
          } else {
            this.errorMessage = response?.message || "Podano niepoprawne dane";
          }
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = 'Login failed. Please try again.';
          console.error('Złe', error);
        }
      });
  }
}