import { Routes } from '@angular/router';
import { BodyComponent } from './body/body.component';
import { RezerwacjaComponent } from './rezerwacja/rezerwacja.component';
import { LoginComponent } from './login/login.component';
import { RejestracjaComponent } from './rejestracja/rejestracja.component';
import { UstawieniaComponent } from './ustawienia/ustawienia.component';
import { HistoriaComponent } from './historia/historia.component';
import { KontaktComponent } from './kontakt/kontakt.component';
import { AboutComponent } from './about/about.component';

export const routes: Routes = [
  {
    path: '',
    component: BodyComponent,
  },
  {
    path: 'body',
    component: BodyComponent,
  },
  {
    path: 'rezerwacja',
    component: RezerwacjaComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'rejestracja',
    component: RejestracjaComponent,
  },
  {
    path: 'ustawienia',
    component: UstawieniaComponent,
  },
  {
    path: 'historia',
    component: HistoriaComponent,
  },
  {
    path: 'kontakt',
    component: KontaktComponent,
  },
  {
    path: 'about',
    component: AboutComponent,
  },
];
