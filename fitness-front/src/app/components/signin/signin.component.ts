import { Component } from '@angular/core';
import { MaterialModule } from '../../Material.Module';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/authentication/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterLink } from '@angular/router';
import { StorageService } from '../../services/local-storage/storage.service';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [MaterialModule, CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss'
})
export class SigninComponent {

  signinForm!: FormGroup;

  hidePassword = true;

  //user: User;

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private snackBar: MatSnackBar,
              private router: Router

  ){

    //this.user = new User('', '');

    //the sign in form must have the exact variable names as 
    //in the SigninRequest class of the backend(which send as a param in signin end point)
    this.signinForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    })

  }

  onSubmit(){
    console.log(this.signinForm.value);
   
    //this.user.email = this.signinForm.get('email')?.value;
    //this.user.password = this.signinForm.get('password')?.value;

    this.authService.signin(this.signinForm.value).subscribe(
      (response)=> {
        console.log(response);
        if(response.userId != null){
          //setting the user of localstorage 
          //set the id of the user of localstorage to the userId from API
          //set role of the user of localstorage to the userRole from API
          const user = {
            id: response.userId, 
            role: response.userRole

          }
            StorageService.saveUser(user);
            StorageService.saveToken(response.jwtToken);
            if(StorageService.isAdminLoggedIn()){
              this.router.navigateByUrl("/admin/dashboard");
            }
            else if(StorageService.isCustomerLoggedIn()){
               this.router.navigateByUrl("/customer/dashboard");
            }

            this.snackBar.open("Sign in success", "Close", {duration: 5000});

        }
          
      },
      (error)=> {
         this.snackBar.open("Invalid username or password", "Close", {duration: 5000, panelClass: "error-snackbar"});
      }
    )

  }


  togglePasswordVisibility(){
      this.hidePassword = !this.hidePassword;
  }

}
