package thecookingpot.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

class PasswordDto {
    lateinit var password: @NotEmpty @Size(min = 5) String
}