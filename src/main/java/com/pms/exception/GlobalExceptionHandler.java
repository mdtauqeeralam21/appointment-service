package com.pms.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AppointmentServiceException.class)
	public ResponseEntity<?> appointmentServiceNotFoundExceptions(AppointmentServiceException ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.NOT_FOUND, LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> appointmentServiceBadRequestExceptions(Exception ex, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<ErrorResponse>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	// @ExceptionHandler(MethodArgumentNotValidException.class)
	// public ResponseEntity<?>
	// handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest
	// request) {
	// Map<String, String> errors = new HashMap<>();
	// ex.getBindingResult().getAllErrors().forEach((error) -> {
	// String fieldName = ((FieldError) error).getField();
	// String errorMessage = error.getDefaultMessage();
	// errors.put(fieldName, errorMessage);
	// });
	// ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST,
	// LocalDateTime.now(), errors.toString(),
	// request.getDescription(false));
	// return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	// }

	// Above is custom definition of protected ResponseEntity<Object>
	// handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders
	// headers, HttpStatusCode status, WebRequest request)

	// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html
	// Handling HttpMessageNotReadableException
	// This exception will be triggered if the request body is invalid:
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	// Dealing with MethodArgumentTypeMismatchException
	// The exception, as the name implies, is thrown when a method parameter has the
	// wrong type:
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

	}

	// Handle MissingServletRequestParameterException
	// MissingServletRequestParameterException indicates that a controller method
	// does not receive a required parameter:
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> details = new ArrayList<String>();
		details.add(ex.getParameterName() + " - parameter is missing");

		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST, LocalDateTime.now(), "Missing Paramters",
				details.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	// Handling HttpMediaTypeNotSupportedException
	// The Exception informs that the specified request media type (Content type) is
	// not supported.
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> details = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are:  ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		details.add(builder.toString());

		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, LocalDateTime.now(),
				"Unsupported Media Type", details.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	// Handle NoHandlerFoundException
	// By default, DispatcherServlet sends a 404 response if there is no handler for
	// a particular request.
	// So, to override the default behavior of our Servlet and throw
	// NoHandlerFoundException instead, we need to add the following properties to
	// the application.properties file.
	// spring.mvc.throw-exception-if-no-handler-found=true
	// spring.resources.add-mappings=false
	// Now, we can handle NoHandlerFoundException in the same way we did with other
	// exceptions.

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		List<String> details = new ArrayList<String>();
		details.add(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		ErrorResponse errorDetails = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, LocalDateTime.now(),
				"Method Not Found", details.toString());
		return new ResponseEntity<>(errorDetails, HttpStatus.UNSUPPORTED_MEDIA_TYPE);

	}
}
