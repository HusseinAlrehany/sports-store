import { Injectable } from '@angular/core';

const TOKEN = 'token';
const USER = 'user';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }

  static isLocalStorageAvailable(): boolean {
    return (
      typeof window !== 'undefined' &&
      typeof window.localStorage !== 'undefined'
    );
  }

  static saveToken(token: string): void {
    if(this.isLocalStorageAvailable()){
      localStorage.removeItem(TOKEN);
      localStorage.setItem(TOKEN, token);
    }
  }

  static saveUser(user: any): void {
    if(this.isLocalStorageAvailable()){
      localStorage.removeItem(USER);
      localStorage.setItem(USER, JSON.stringify(user));
    }
  }
  static getToken(): string | null{
    if(this.isLocalStorageAvailable()){
      return localStorage.getItem(TOKEN);
    }
    return null;
  }

  static getUser(): any {
    if(this.isLocalStorageAvailable()){
      return JSON.parse(localStorage.getItem(USER)!);
    }
    return null;
  }
  static getUserRole(): string {
    const user = this.getUser();
    if(user == null){
      return '';
    }
    return user.role; //role is a variable in the localstorage of the browser can not be changed to another name
  }

  static getUserId(): string {
    const user = this.getUser();
    if(user == null){
      return '';
    }
    return user.id; //id is a variable in the localstorage of the browser can not be changed to another name
  }
  static isAdminLoggedIn(): boolean {
    if(this.getToken() === null || !this.isLocalStorageAvailable()){
      return false;
    }
    const role: string = this.getUserRole();
    return role == 'ADMIN'; //role is a variable in the localstorage of the browser can not be changed to another name
  }

  static isCustomerLoggedIn(): boolean {
    if(this.getToken() === null || !this.isLocalStorageAvailable()){
      return false;
    }
    const role: string = this.getUserRole();
    return role == 'CUSTOMER'; //role is a variable in the localstorage of the browser can not be changed to another name
  }
  static logout(): void {
     if(this.isLocalStorageAvailable()){
      localStorage.removeItem(TOKEN);
      localStorage.removeItem(USER);
     }
  }
}
