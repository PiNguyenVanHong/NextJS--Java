package com.tutorial.apidemo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegisterUserDto {
    @NotBlank(message = "Your email is not empty")
    @Email(message = "Your email is not valid")
    private String email;

    @NotBlank(message = "Your password is not empty")
    @Length(min = 6, max = 50, message = "Your password is not valid")
    private String password;

    @NotBlank(message = "Your name is not empty")
    @Length(min = 6, max = 50, message = "Your name is not valid")
    private String name;
}
