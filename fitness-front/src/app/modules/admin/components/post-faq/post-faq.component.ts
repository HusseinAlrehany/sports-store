import { Component } from '@angular/core';
import { MaterialModule } from '../../../../Material.Module';
import { CommonModule } from '@angular/common';
import { AdminService } from '../../services/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-post-faq',
  standalone: true,
  imports: [MaterialModule, CommonModule, ReactiveFormsModule],
  templateUrl: './post-faq.component.html',
  styleUrl: './post-faq.component.scss'
})
export class PostFaqComponent {

  productId: number = this.activatedRoute.snapshot.params["productId"];
  faqForm: FormGroup;

  constructor(private adminService: AdminService, 
              private router: Router,
              private snackBar: MatSnackBar,
              private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder
  ){}

  ngOnInit(){
    this.faqForm = this.formBuilder.group({
      question: [null, [Validators.required]],
      answer: [null, [Validators.required]]
    })
  }

  postFaq(){
    this.adminService.createFaq(this.productId, this.faqForm.value).subscribe(
      (res)=> {
        if(res.id != null){
          this.snackBar.open("FAQ posted Successfully!", "Close", {duration: 5000});
          this.router.navigateByUrl('/admin/dashboard');
        } else{
          this.snackBar.open("Somthing Went Wrong!", "Close", {duration: 5000, panelClass: 'error-snackbar'});

        }
      }
    )
  }

}
