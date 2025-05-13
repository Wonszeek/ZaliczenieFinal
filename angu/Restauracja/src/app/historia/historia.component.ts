import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RezerwacjaService } from '../rezerwacja.service';
import { HttpClientModule } from '@angular/common/http';

interface Reservation {
  id: number;
  tableId: number;
  date: string;
  startTime: string;
  endTime: string;
  people: number;
  tableName: string;
  tableSize: number;
}

@Component({
  selector: 'app-historia',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './historia.component.html',
  styleUrls: ['./historia.component.css']
})
export class HistoriaComponent implements OnInit {
  reservations: Reservation[] = [];
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(private rezerwacjaService: RezerwacjaService) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.rezerwacjaService.getReservations().subscribe({
      next: (reservations) => {
        this.reservations = reservations;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Wystąpił błąd podczas ładowania historii rezerwacji';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('pl-PL');
  }

  formatTime(timeString: string): string {
    return timeString.substring(0, 5); // Format as HH:MM
  }
}