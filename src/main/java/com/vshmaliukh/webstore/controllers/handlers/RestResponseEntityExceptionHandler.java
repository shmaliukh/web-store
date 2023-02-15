package com.vshmaliukh.webstore.controllers.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(ValidationException.class)
//    public ModelAndView handleValidationException(Exception e, WebRequest request) {
//        ModelMap modelMap = new ModelMap();
//        String errorMessage = e.getMessage();
//        String contextPath = request.getContextPath();
//        modelMap.addAttribute("errorMessage", errorMessage);
//        log.error("validation error // path: '{}' // error message: '{}'", contextPath, errorMessage);
//        return new ModelAndView("redirect:" + contextPath, modelMap);
//    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ModelAndView handleRuntimeException(Exception exception) {
        // TODO exclude '/admin/**'
        log.error(exception.getMessage(), exception);
        return new ModelAndView("error");
    }

    // error handle for @Valid
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
//                                                                  HttpHeaders headers,
//                                                                  HttpStatus status,
//                                                                  WebRequest request) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", new Date());
//        body.put("status", status.value());
//
//        List<String> errorList = exception.getBindingResult()
//                .getFieldErrors().stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
//        body.put("errors", errorList);
//        return new ResponseEntity<>(body, headers, status);
//    }

}
