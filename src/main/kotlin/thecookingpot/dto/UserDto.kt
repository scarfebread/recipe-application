package thecookingpot.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

class UserDto {
    lateinit var username: @NotEmpty @Size(min = 3) String
    lateinit var password: @NotEmpty @Size(min = 5) String
    lateinit var email: @NotEmpty @Email String
}