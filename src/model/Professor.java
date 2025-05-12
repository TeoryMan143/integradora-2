package model;

import java.util.ArrayList;

public class Professor {

	private String idNumber;
	private String name;
	private String email;
	private DocumentType type;
	private ArrayList<Course> courses;

	public Professor(String idNumber, DocumentType type, String name, String email) {
		this.idNumber = idNumber;
		this.type = type;
		this.name = name;
		this.email = email;
		this.courses = new ArrayList<Course>();
	}

	/**
	 * 
	 * @param course
	 */
	public void setCourse(Course course) {
		this.courses.add(course);
	}

	public String getIdNumber() {
		return this.idNumber;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public DocumentType getType() {
		return type;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	@Override
	public String toString() {
		return "ID: " + idNumber + ", Nombre: " + name + ", Email: " + email;
	}
}