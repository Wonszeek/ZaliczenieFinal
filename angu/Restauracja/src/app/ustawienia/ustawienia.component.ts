// ustawienia.component.ts
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-ustawienia',
  standalone: true,
  templateUrl: './ustawienia.component.html',
  styleUrls: ['./ustawienia.component.css'],
  imports: [RouterLink],
})
export class UstawieniaComponent implements OnInit {
  username: string = '';
  email: string = '';

  ngOnInit() {
    // Pobierz dane z localStorage po załadowaniu komponentu
    this.username = localStorage.getItem('username') || '';
    this.email = localStorage.getItem('email') || '';
  }
}
