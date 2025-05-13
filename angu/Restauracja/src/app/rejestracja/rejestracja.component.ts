import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-rejestracja',
  standalone: true,
  templateUrl: './rejestracja.component.html',
  styleUrls: ['./rejestracja.component.css'],
  imports: [CommonModule, FormsModule, RouterModule],
})
export class RejestracjaComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  register() {
   
    if (this.password !== this.confirmPassword) {
      alert('Hasła nie są takie same!');
      return;
    }

    if (this.password.length < 8) {
      alert('Hasło musi mieć co najmniej 8 znaków');
      return;
    }

   
    if (!this.email.includes('@')) {
      alert('Proszę podać poprawny adres email');
      return;
    }

    const registerData = {
      username: this.username,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    };

    this.http.post<{success: boolean, message: string}>(
      'http://localhost:9040/api/auth/register',
      registerData
    ).subscribe({
      next: (response) => {
        if (response.success) {
          alert('Rejestracja zakończona sukcesem!');
          this.resetForm();
          this.router.navigate(['/login']);
        } else {
          alert('Błąd rejestracji: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Błąd rejestracji:', error);
        alert('Wystąpił błąd podczas rejestracji: ' + error.error?.message || error.message);
      }
    });
  }

  private resetForm() {
    this.username = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
  }
}