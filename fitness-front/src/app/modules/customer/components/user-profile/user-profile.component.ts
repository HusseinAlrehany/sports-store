import { Component } from '@angular/core';
import { MaterialModule } from '../../../../Material.Module';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StorageService } from '../../../../services/local-storage/storage.service';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule, CommonModule],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss'
})
export class UserProfileComponent {

    userProfileForm: FormGroup;
  
    imagePreview: string | ArrayBuffer | null;
    existingImage: string | null = null;
    imgChanged = false;
    selectedFile: File | null;

  constructor(private customerService: CustomerService,
              private snackBar: MatSnackBar,
              public router: Router,
              private formBuilder: FormBuilder
  ){}


  onFileSelected(event: any){
    this.selectedFile = event.target.files[0];
    this.previewImage();
    this.imgChanged = true;
    this.existingImage = null;
 }
 previewImage() {
   
   const reader = new FileReader();
   reader.onload = () => {
     //update imagePreview 
     this.imagePreview = reader.result;
   }

   reader.readAsDataURL(this.selectedFile);
 }

  ngOnInit(){
     this.userProfileForm = this.formBuilder.group({
       name: [null, [Validators.required]],
       email: [null, [Validators.required]],
       password: [null, [Validators.required]]
     });

     this.getUserById();
  }

  getUserById(){
     this.customerService.getUserById().subscribe(res => {
      //repopoulate the user profile with values
      this.userProfileForm.patchValue(res);
      this.existingImage = 'data:image/jpeg;base64,' + res.byteImg;
    })
  }


  updateProfile() {
     if(this.userProfileForm.valid){
      const formData: FormData = new FormData();

      if(this.imgChanged && this.selectedFile){
        formData.append('image', this.selectedFile);
      }

      formData.append('name', this.userProfileForm.get('name').value);
      formData.append('email', this.userProfileForm.get('email').value);
      formData.append('password', this.userProfileForm.get('password').value);

      this.customerService.updateUserProfile(formData).subscribe(
        (res)=> {
          if(res.id != null){
           const user = {
               id: res.id,
               role: res.userRole
            }

            StorageService.saveUser(user);
            StorageService.saveToken(res.updatedToken);
            this.snackBar.open("Profile Updated Successfully", "Close", {duration: 5000});
            this.router.navigateByUrl('/customer/dashboard');
          }          
        },
        (error) => {
          const errorMessage = error.error?.message || error.message || 'An unexpected Error Occur';
          this.snackBar.open(errorMessage, "ERROR", {duration: 5000});
        }
      )
     }
  }

}
