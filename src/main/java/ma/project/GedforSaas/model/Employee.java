package ma.project.GedforSaas.model;

public class Employee {
	
	private String name; 
	private String birthdate;
	private String payment;
	private String bonus;
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Employee(String name, String birthdate, String payment, String bonus) {
		super();
		this.name = name;
		this.birthdate = birthdate;
		this.payment = payment;
		this.bonus = bonus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	} 
}
