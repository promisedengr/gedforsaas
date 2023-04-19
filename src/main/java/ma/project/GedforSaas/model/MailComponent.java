package ma.project.GedforSaas.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailComponent {

	private String link;

	private String subject;

	private String content;

	private String to;

	private String from;
}
