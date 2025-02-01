import { Component } from '@angular/core';
import { AuthService } from '../../services/authentication/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MaterialModule } from '../../Material.Module';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-track-order',
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule, CommonModule],
  templateUrl: './track-order.component.html',
  styleUrl: './track-order.component.scss'
})
export class TrackOrderComponent {

  seacrhOrderForm: FormGroup;

  order: any;

  constructor(private authService: AuthService,
              private formBuilder: FormBuilder
  ){}

  ngOnInit(){
    this.seacrhOrderForm = this.formBuilder.group({
      trackingId: [null, [Validators.required]]
    })
  }

  submitForm(){
    this.authService.getOrderByTrackingId(this.seacrhOrderForm.get('trackingId').value)
    .subscribe(res => {
      this.order =  res;
      console.log(res);
    })

    this.seacrhOrderForm.reset();
  }

}
