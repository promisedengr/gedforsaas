package ma.project.GedforSaas.service;

import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import ma.project.GedforSaas.response.ResponsePayLoad;

@Service
public class ResponsePayLoadService {

	public ResponsePayLoad generateNewPayLoad(String message, Object response) {

		return new ResponsePayLoad("200 OK", ZonedDateTime.now(), message, response);
	}

	public ResponsePayLoad generateNewPayLoad(String httpStatusTitle, String message, Object response) {

		return new ResponsePayLoad(httpStatusTitle, ZonedDateTime.now(), message, response);
	}

}
