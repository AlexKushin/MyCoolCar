package com.mycoolcar.validation;

import com.mycoolcar.dtos.UserCreationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

   @Override
   public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
       final UserCreationDto user = (UserCreationDto) obj;
       return user.password().equals(user.matchingPassword());
   }

}