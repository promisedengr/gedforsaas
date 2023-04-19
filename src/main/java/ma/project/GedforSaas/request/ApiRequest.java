package ma.project.GedforSaas.request;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiRequest {

	private String message;

	private HttpStatus httpStatus;

	private ZonedDateTime timeStamp;

}
