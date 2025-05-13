import { Component, OnInit, OnDestroy, effect } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { AuthService } from '../auth.service';
import { RezerwacjaService } from '../rezerwacja.service';
import { Subject, takeUntil } from 'rxjs'; // Import Subject and takeUntil
import { signal } from '@angular/core';

interface ReservationRequest {
    tableId: number;
    date: string;
    startTime: string;
    endTime: string;
    people: number;

}

interface RestaurantTable {
    id: number;
    name: string;
    description: string;
    size: number;
}

@Component({
    selector: 'app-rezerwacja',
    standalone: true,
    imports: [FormsModule, CommonModule, HttpClientModule],
    templateUrl: './rezerwacja.component.html',
    styleUrls: ['./rezerwacja.component.css'],
})
export class RezerwacjaComponent implements OnInit, OnDestroy {
    currentDate = new Date();
    today = new Date();
    selectedDate: string = '';
    selectedStartTime: string = '';
    selectedEndTime: string = '';
    peopleCount: number = 1;
    availableTables: RestaurantTable[] = [];
    selectedTableId: number | null = null;
    reservationSuccess: boolean = false;
    errorMessage: string = '';
    currentUser: AuthService['currentUser'] = signal(null);
    private destroy$ = new Subject<void>(); // Subject to manage subscriptions

    dayNames: string[] = ['Pn', 'Wt', 'Śr', 'Cz', 'Pt', 'Sb', 'Nd'];
    hours: string[] = [];

    constructor(
        private http: HttpClient,
        private authService: AuthService,
        private RezerwacjaService: RezerwacjaService
    ) {
        for (let h = 12; h <= 20; h++) {
            this.hours.push(`${h}:00`);
            if (h < 20) this.hours.push(`${h}:30`);
        }

        effect(() => {
            this.currentUser.set(this.authService.currentUser());
        });
    }

    ngOnInit(): void {
        // No explicit subscription needed with effect
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }

    prevMonth(): void {
        this.currentDate = new Date(
            this.currentDate.getFullYear(),
            this.currentDate.getMonth() - 1,
            1
        );
    }

    nextMonth(): void {
        this.currentDate = new Date(
            this.currentDate.getFullYear(),
            this.currentDate.getMonth() + 1,
            1
        );
    }

    getMonthYear(): string {
        return this.currentDate.toLocaleString('pl-PL', {
            month: 'long',
            year: 'numeric',
        });
    }

    getDaysInMonth(): number {
        return new Date(
            this.currentDate.getFullYear(),
            this.currentDate.getMonth() + 1,
            0
        ).getDate();
    }

    getFirstDayOfWeek(): number {
        return (
            (new Date(
                this.currentDate.getFullYear(),
                this.currentDate.getMonth(),
                1
            ).getDay() +
                6) %
            7
        );
    }

    isPastDay(day: number): boolean {
        const dayDate = new Date(
            this.currentDate.getFullYear(),
            this.currentDate.getMonth(),
            day
        );
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        return dayDate < today;
    }

    selectDate(day: number): void {
        const selectedDate = new Date(
            this.currentDate.getFullYear(),
            this.currentDate.getMonth(),
            day
        );
        this.selectedDate = selectedDate.toISOString().split('T')[0];
        this.selectedStartTime = '';
        this.selectedEndTime = '';
        this.selectedTableId = null;
        this.availableTables = [];
        this.reservationSuccess = false;
        this.errorMessage = '';
    }

    selectTime(time: string, isStartTime: boolean): void {
        if (isStartTime) {
            this.selectedStartTime = time;
            const [hours, minutes] = time.split(':').map(Number);
            let endHours = hours + 2;
            if (endHours > 20) endHours = 20;
            this.selectedEndTime = `${endHours}:${minutes.toString().padStart(2, '0')}`;
        } else {
            this.selectedEndTime = time;
        }

        if (this.selectedStartTime && this.selectedEndTime && this.selectedDate) {
            this.checkAvailableTables();
        }
    }

    checkAvailableTables(): void {
        if (!this.selectedDate || !this.selectedStartTime || !this.selectedEndTime) {
            return;
        }

        this.RezerwacjaService.getAvailableTables(
            this.peopleCount,
            this.selectedDate,
            this.selectedStartTime,
            this.selectedEndTime
        ).pipe(takeUntil(this.destroy$)) // Unsubscribe when component destroys
        .subscribe({
            next: (tables) => {
                this.availableTables = tables;
                this.errorMessage = '';
                this.selectedTableId = null;
            },
            error: (err) => {
                this.errorMessage = 'Wystąpił błąd podczas sprawdzania dostępności stolików';
                console.error(err);
            },
        });
    }

    selectTable(tableId: number): void {
        this.selectedTableId = tableId;
    }

    incrementPeople(): void {
        if (this.peopleCount < 8) {
            this.peopleCount++;
            if (this.selectedStartTime && this.selectedEndTime) {
                this.checkAvailableTables();
            }
        }
    }

    decrementPeople(): void {
        if (this.peopleCount > 1) {
            this.peopleCount--;
            if (this.selectedStartTime && this.selectedEndTime) {
                this.checkAvailableTables();
            }
        }
    }

    makeReservation(): void {
        if (!this.selectedTableId || !this.selectedDate || !this.selectedStartTime || !this.selectedEndTime) {
            this.errorMessage = 'Proszę wypełnić wszystkie pola rezerwacji';
            return;
        }

        if (!this.authService.isLoggedIn) {
            this.errorMessage = 'Musisz być zalogowany aby dokonać rezerwacji';
            return;
        }

        const reservationRequest: ReservationRequest = {
            tableId: this.selectedTableId,
            date: this.selectedDate,
            startTime: this.selectedStartTime,
            endTime: this.selectedEndTime,
            people: this.peopleCount,
        };

        this.RezerwacjaService.createReservation(reservationRequest)
            .pipe(takeUntil(this.destroy$)) // Unsubscribe when component destroys
            .subscribe({
                next: () => {
                    this.reservationSuccess = true;
                    this.errorMessage = '';
                    this.resetForm();
                },
                error: (err: any) => {
                    this.errorMessage = err.error?.message || 'Wystąpił błąd podczas rezerwacji';
                    console.error(err);
                },
            });
    }

    resetForm(): void {
        this.selectedDate = '';
        this.selectedStartTime = '';
        this.selectedEndTime = '';
        this.selectedTableId = null;
        this.availableTables = [];
        this.peopleCount = 1;
        setTimeout(() => {
            this.reservationSuccess = false;
        }, 5000);
    }

    isDateSelected(day: number): boolean {
        if (!this.selectedDate) return false;
        const selectedDate = new Date(this.selectedDate);
        return (
            selectedDate.getFullYear() === this.currentDate.getFullYear() &&
            selectedDate.getMonth() === this.currentDate.getMonth() &&
            selectedDate.getDate() === day
        );
    }

    isTimeSelected(time: string, isStartTime: boolean): boolean {
        return isStartTime
            ? time === this.selectedStartTime
            : time === this.selectedEndTime;
    }

    isTableSelected(tableId: number): boolean {
        return this.selectedTableId === tableId;
    }

    isTimeDisabled(time: string, isStartTime: boolean): boolean {
        if (!this.selectedDate) return true;

        if (isStartTime) {
            return false;
        } else {
            if (!this.selectedStartTime) return true;
            const [startH, startM] = this.selectedStartTime.split(':').map(Number);
            const [currentH, currentM] = time.split(':').map(Number);
            return currentH < startH || (currentH === startH && currentM <= startM);
        }
    }
}