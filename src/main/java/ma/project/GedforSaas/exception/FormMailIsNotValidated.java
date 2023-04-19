package ma.project.GedforSaas.exception;

import java.sql.SQLException;

import org.hibernate.exception.ConstraintViolationException;

public class FormMailIsNotValidated extends ConstraintViolationException {

	public FormMailIsNotValidated(String message, SQLException root, String constraintName) {
		super(message, root, constraintName);
		// TODO Auto-generated constructor stub
	}

}
