package ma.project.GedforSaas.service;

import ma.project.GedforSaas.exception.ResourceNotFoundExceptionConstimized;
import ma.project.GedforSaas.model.ConfirmationToken;
import ma.project.GedforSaas.model.User;
import ma.project.GedforSaas.repository.ConfirmationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static ma.project.GedforSaas.controller.TranslateController.LOCALE;
import static ma.project.GedforSaas.utils.i18n.Translator.TO_LOCALE;
import static ma.project.GedforSaas.utils.i18n.TranslatorCode.TOKEN_NOT_FOUND_MSG;

@Service
public class ConfirmationTokenService {

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	public ConfirmationToken getToken(String token) {

		Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);

		if (!confirmationToken.isPresent()) {
			throw new ResourceNotFoundExceptionConstimized(TO_LOCALE(TOKEN_NOT_FOUND_MSG, LOCALE));
		}

		return confirmationToken.get();
	}

	public String addNewConfirmationToken(User userRequest) {

		// TODO: generate validation token
		String token = UUID.randomUUID().toString();

		// TODO: create a new confirmation token
		ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
				LocalDateTime.now().plusMinutes(10), userRequest);

		// TODO: save the generated token in db
		confirmationTokenRepository.save(confirmationToken);

		return token;
	}

}