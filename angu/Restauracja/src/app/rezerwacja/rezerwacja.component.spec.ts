import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RezerwacjaComponent } from './rezerwacja.component';

describe('RezerwacjaComponent', () => {
  let component: RezerwacjaComponent;
  let fixture: ComponentFixture<RezerwacjaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RezerwacjaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RezerwacjaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
