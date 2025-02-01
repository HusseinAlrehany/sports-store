import { Component } from '@angular/core';
import { MaterialModule } from '../../Material.Module';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/authentication/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [MaterialModule, FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {

  signupForm! : FormGroup;

  hidePassword = true;

  constructor(private formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              private authService: AuthService,
              private router: Router
  ){

    //the sign up form must have the exact variable names as 
    //in the SignupRequest class of the backend(which send as a param in signup end point)
    //otherwise the wrong variable name will be inserted as nullin database table
    this.signupForm = this.formBuilder.group({

      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    })

  }

  onSubmit(){
    console.log(this.signupForm.value);
    const password = this.signupForm.get('password')?.value;
    const confirmPassword = this.signupForm.get('confirmPassword')?.value;
    if(password !== confirmPassword ){
      this.snackBar.open("password and confirm password don't match.", "Close", {duration: 5000, panelClass: "error-snackbar"});
      return;
    }

    this.authService.signup(this.signupForm.value).subscribe(
      (response)=> {
        console.log(response);
        if(response.id !== null){
          this.snackBar.open("Signup Successfull", "Close", {duration: 5000});
          this.router.navigateByUrl('/signin');
        } else {
          this.snackBar.open("Sign Up Failed.Try Again","Close", {duration: 5000, panelClass: "error-snackbar"});
        }

      },
      (error)=> {
        this.snackBar.open("User Already Exists With This Email", "Close", {duration: 5000, panelClass: "error-snackbar"});
      }
    )
    
  }


  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }

}
