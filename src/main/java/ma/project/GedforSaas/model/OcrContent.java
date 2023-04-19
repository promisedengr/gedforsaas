package ma.project.GedforSaas.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class OcrContent {
	private String id;
	private String fileName;
	private String document_id ; 
	private String text;
}
