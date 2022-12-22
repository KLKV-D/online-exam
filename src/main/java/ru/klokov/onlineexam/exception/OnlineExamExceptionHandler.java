package ru.klokov.onlineexam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class OnlineExamExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> messages = new HashMap<>();

        for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
                messages.computeIfAbsent(((FieldError) objectError).getField(), k -> new ArrayList<>()).add(objectError.getDefaultMessage());
        }

//        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
//            String fieldName = ((FieldError) error).getField();
//            List<String> errors = messages.get(fieldName);
//            if (errors == null) {
//                errors = new ArrayList<>();
//                messages.put(fieldName, errors);
//            }
//            errors.add(error.getDefaultMessage());
//        }

        ValidationExceptionResponse validationExceptionResponse = new ValidationExceptionResponse(
                messages,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(validationExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class, ResourceAlreadyExistsException.class})
    public ResponseEntity<CustomExceptionResponse> handleResourceNotFoundException(Exception ex) {
        CustomExceptionResponse customExceptionResponse = new CustomExceptionResponse(
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(customExceptionResponse, HttpStatus.NOT_FOUND);
    }
}
