package com.karol.app.validator;

import com.karol.app.dto.AirportDto;
import com.karol.app.dto.BookingDto;
import com.karol.app.dto.FlightDto;
import com.karol.app.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullIdValidator implements ConstraintValidator<NotNullId, Object> {

   public void initialize(NotNullId constraint) { }

   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      if (obj == null)
         return false;
      else if (obj instanceof AirportDto)
         return ((AirportDto) obj).getId() != null;
      else if (obj instanceof BookingDto)
         return ((BookingDto) obj).getId() != null;
      else if (obj instanceof FlightDto)
         return ((FlightDto) obj).getId() != null;
      else if (obj instanceof UserDto)
         return ((UserDto) obj).getId() != null;
      return false;
   }

}
