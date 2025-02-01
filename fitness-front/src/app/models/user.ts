export class User {

    //this is a mirroring of the signinRequest of the backend
    //must have the exact variable names as the SigninRequest class of the backend
    email: string;
    password: string;

    constructor(email: string, password: string){
        this.email = email;
        this.password = password;
    }
}
