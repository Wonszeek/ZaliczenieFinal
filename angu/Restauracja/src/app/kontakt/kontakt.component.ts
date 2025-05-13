import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
// Removed unnecessary self-import of KontaktComponent

@Component({
  selector: 'app-kontakt',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './kontakt.component.html',
  styleUrls: ['./kontakt.component.css'],
})
export class KontaktComponent {}
