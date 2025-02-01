import { Component, Input } from '@angular/core';
import { MaterialModule } from '../../../../../Material.Module';

@Component({
  selector: 'app-order-by-status',
  standalone: true,
  imports: [MaterialModule],
  templateUrl: './order-by-status.component.html',
  styleUrl: './order-by-status.component.scss'
})
export class OrderByStatusComponent {

  //to get data from analytics component
  @Input() data: any;

}
