// auth.service.ts
import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

interface User {
    id?: number;
    email: string;
    username: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    public currentUser = signal<User | null>(null);
    private authToken: string | null = null; // Store the token

    constructor(private http: HttpClient, private router: Router) {
        // Initialize from localStorage
        const userString = localStorage.getItem('currentUser');
        if (userString) {
            const user = JSON.parse(userString);
            this.currentUser.set(user);
            this.authToken = localStorage.getItem('authToken'); // Load token
        }
    }

    get currentUserValue(): User | null {
        return this.currentUser();
    }

    get isLoggedIn(): boolean {
        return this.currentUser() !== null;
    }



    login(email: string, password: string): Observable<any> {
        return this.http.post<any>('http://localhost:9040/api/auth/login', 
            { email, password }, 
            { withCredentials: true}
        ).pipe(
            tap(response => {
                if (response.success) {
                    const user = {
                        email: response.email,
                        username: response.username
                    };
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    this.currentUser.set(user);
                    
                }
            })
        );
    }

    logout(): Observable<any> {
        return this.http.post<any>('http://localhost:9040/api/auth/logout', 
            {}, 
            { withCredentials: true }
        ).pipe(
            tap(response => {
                if (response.success) {
                    localStorage.removeItem('currentUser');
                    this.currentUser.set(null);
                    this.router.navigate(['/login']);
                }
            })
        );
    }
}