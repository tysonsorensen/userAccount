package net.tysonsorensen.userAccount.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {

    @NotBlank(message = "{userName.blank}")
    private String userName;

    @NotBlank(message = "{firstName.blank}")
    private String firstName;

    @NotBlank(message = "{lastName.blank}")
    private String lastName;

    @NotBlank(message = "{email.blank}")
    @Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "{email.invalid}")
    private String email;

    @NotBlank(message = "{password.blank}")
    @Size(min=8, message = "{password.short}")
    private String password;
}
