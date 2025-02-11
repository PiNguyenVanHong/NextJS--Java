package com.tutorial.apidemo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class VerifyUserDto {
    @NotBlank(message = "Your email is not empty")
    @Email(message = "Your email is not valid")
    private String email;

    @NotBlank(message = "Verify code is not empty")
    @Length(min = 6, max = 6, message = "Verify code is not valid")
    private String verificationCode;
}
