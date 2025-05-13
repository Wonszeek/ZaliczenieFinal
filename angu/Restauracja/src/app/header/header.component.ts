import {
  Component,
  ChangeDetectorRef,
  AfterViewInit,
  OnDestroy,
  effect,
  EffectRef
} from '@angular/core';
import { AuthService } from '../auth.service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

interface User {
  email: string;
  username: string;
}

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements AfterViewInit, OnDestroy {
  title = 'Restauracja';
  menuActive: boolean = false;
  currentUser: User | null = null;
  private authEffect: EffectRef;

  constructor(
    public authService: AuthService,
    private cdr: ChangeDetectorRef
  ) {
    // Initialize with current value
    this.currentUser = this.authService.currentUser();
    
    // Watch for changes in the signal
    this.authEffect = effect(() => {
      this.currentUser = this.authService.currentUser();
      this.cdr.detectChanges();
    });
  }

  ngAfterViewInit(): void {
    document.addEventListener('click', this.handleOutsideClick.bind(this));
  }

  ngOnDestroy(): void {
    document.removeEventListener('click', this.handleOutsideClick.bind(this));
    this.authEffect.destroy(); // Properly destroy the effect
  }

  get isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  toggleMenu(event: Event): void {
    this.menuActive = !this.menuActive;
    event.stopPropagation();
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.menuActive = false;
      },
      error: (err) => {
        console.error('Logout error:', err);
      }
    });
  }

  private handleOutsideClick(event: Event): void {
    if (this.menuActive) {
      const menu = document.getElementById('menuItems');
      const burger = document.getElementById('burgerMenu');
      
      if (
        menu &&
        burger &&
        !menu.contains(event.target as Node) &&
        !burger.contains(event.target as Node)
      ) {
        this.menuActive = false;
      }
    }
  }
}