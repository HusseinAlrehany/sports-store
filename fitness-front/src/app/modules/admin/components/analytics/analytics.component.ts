import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { OrderByStatusComponent } from "./order-by-status/order-by-status.component";
import { MaterialModule } from '../../../../Material.Module';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [OrderByStatusComponent, MaterialModule, CommonModule],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent {

  data: any;
  constructor(private adminService: AdminService){}

  ngOnInit(){
    this.adminService.getAnalytics().subscribe(res => {
      console.log(res);
      this.data = res;
    })
  }

}
