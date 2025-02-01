import { Component, OnInit } from '@angular/core';
import { MaterialModule } from '../../../../Material.Module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post-category',
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule, CommonModule],
  templateUrl: './post-category.component.html',
  styleUrl: './post-category.component.scss'
})
export class PostCategoryComponent implements OnInit{

  categoryForm!: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              private adminService: AdminService,
              private router: Router
  ){
      
    
  }
  ngOnInit(): void {

    this.categoryForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]]
    })
  }


  addCategory() {

    if(this.categoryForm.valid){
        
       this.adminService.addCategory(this.categoryForm.value).subscribe(
        (response)=> {
          console.log(response);
          if(response.id != null){
            this.snackBar.open("Category Created successfully","Close", {duration: 5000});

            this.router.navigateByUrl('/admin/dashboard');
          }
        })  
      
    } else {
      this.categoryForm.markAllAsTouched();
    }
    
      
    
  }

}
