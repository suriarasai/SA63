package sg.edu.nus.jpademo.model;

public class SampleForm {
	
	private String first_name;
	private String surname;
	private String email;
	public SampleForm(String first_name, String surname, String email) {
		super();
		this.first_name = first_name;
		this.surname = surname;
		this.email = email;
	}
	public SampleForm() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "SampleForm [first_name=" + first_name + ", surname=" + surname + ", email=" + email + "]";
	}
	

}
