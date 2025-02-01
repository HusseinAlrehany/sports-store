import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MaterialModule } from './Material.Module';
import { StorageService } from './services/local-storage/storage.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MaterialModule, RouterLink, CommonModule, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{

isAdminLoggedIn: boolean = StorageService.isAdminLoggedIn();
isCustomerLoggedIn: boolean = StorageService.isCustomerLoggedIn();

constructor(private router: Router){}

  ngOnInit(): void {
   this.router.events.subscribe(event => {
     this.isAdminLoggedIn = StorageService.isAdminLoggedIn();
     this.isCustomerLoggedIn = StorageService.isCustomerLoggedIn();
   })
  }

  logout(){
    StorageService.logout();
    this.router.navigateByUrl("/signin");
  }
 

}
