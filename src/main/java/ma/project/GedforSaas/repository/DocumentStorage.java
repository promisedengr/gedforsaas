package ma.project.GedforSaas.repository;


import org.springframework.content.commons.repository.ContentStore;

import java.util.UUID;

import ma.project.GedforSaas.model.Document;
public interface DocumentStorage extends ContentStore<Document, UUID> {

}
