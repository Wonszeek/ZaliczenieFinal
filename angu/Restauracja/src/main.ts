import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
  
  document.addEventListener("DOMContentLoaded", () => {
    const burgerMenu = document.getElementById("burgerMenu") as HTMLElement | null;
    const menuItems = document.getElementById("menuItems") as HTMLElement | null;
  
    if (burgerMenu && menuItems) {
      burgerMenu.addEventListener("click", (e: MouseEvent) => {
        e.stopPropagation();
        burgerMenu.classList.toggle("active");
        menuItems.style.display = menuItems.style.display === "block" ? "none" : "block";
      });
  
      document.addEventListener("click", () => {
        burgerMenu.classList.remove("active");
        menuItems.style.display = "none";
      });
  
      menuItems.addEventListener("click", (e: MouseEvent) => {
        e.stopPropagation();
      });
    }
  });
  