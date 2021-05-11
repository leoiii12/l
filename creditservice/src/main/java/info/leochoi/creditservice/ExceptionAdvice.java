package info.leochoi.creditservice;

import com.fasterxml.jackson.annotation.JsonFormat;
import info.leochoi.creditservice.scoring.exception.NoRuleIsMatchedException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

  public static class CustomErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime timestamp;

    private int statusCode;
    private String message;

    public ZonedDateTime getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(ZonedDateTime now) {
      this.timestamp = now;
    }

    public int getStatusCode() {
      return statusCode;
    }

    public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<CustomErrorResponse> handleGenericNotFoundException(final Exception e) {
    if (e instanceof MethodArgumentNotValidException || e instanceof NoRuleIsMatchedException) {
      final CustomErrorResponse customErrorResponse = new CustomErrorResponse();
      customErrorResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Asia/Hong_Kong")));
      customErrorResponse.setStatusCode((HttpStatus.BAD_REQUEST.value()));
      customErrorResponse.setMessage("The input is invalid.");

      return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);
    }
    if (e instanceof AccessDeniedException) {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      // 401
      if (!(authentication instanceof AnonymousAuthenticationToken)) {
        final CustomErrorResponse customErrorResponse = new CustomErrorResponse();
        customErrorResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Asia/Hong_Kong")));
        customErrorResponse.setStatusCode((HttpStatus.UNAUTHORIZED.value()));
        customErrorResponse.setMessage("Unauthorized.");

        return new ResponseEntity<>(customErrorResponse, HttpStatus.UNAUTHORIZED);
      }

      // 403
      final CustomErrorResponse customErrorResponse = new CustomErrorResponse();
      customErrorResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Asia/Hong_Kong")));
      customErrorResponse.setStatusCode((HttpStatus.UNAUTHORIZED.value()));
      customErrorResponse.setMessage("Unauthenticated. Please authenticate with a Bearer JWT.");

      return new ResponseEntity<>(customErrorResponse, HttpStatus.FORBIDDEN);
    }

    final CustomErrorResponse customErrorResponse = new CustomErrorResponse();
    customErrorResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Asia/Hong_Kong")));
    customErrorResponse.setStatusCode((HttpStatus.INTERNAL_SERVER_ERROR.value()));
    customErrorResponse.setMessage("Internal Server Error.");

    logger.error("Internal Server Error. Unable to handle the exception.", e);

    return new ResponseEntity<>(customErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
