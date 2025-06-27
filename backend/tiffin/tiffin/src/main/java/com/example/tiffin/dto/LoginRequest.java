package com.example.tiffin.dto;

import lombok.Data;

@Data
public class LoginRequest{
   private  String email;
   private String password;

   public String getPassword() {
      return password;
   }

   public Object getEmail() {
      return  email;
   }
}
