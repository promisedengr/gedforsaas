package ma.project.GedforSaas.response;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class ResponsePayLoad {

	private String httpStatusTitle;

	private ZonedDateTime timeStamp;

	private String message;

	private Object responseBody;

}
