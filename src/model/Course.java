package model;

import java.util.ArrayList;

public class Course {

	private String name;
	private String description;
	private String code;
	private int credits;
	private ArrayList<Professor> professors;
	private ArrayList<Project> projects;

	public Course(String name, String description, String code, int credits) {
		this.name = name;
		this.description = description;
		this.code = code;
		this.credits = credits;
		this.professors = new ArrayList<Professor>();
	}

	/**
	 * 
	 * @param professor
	 */
	public void setProfessor(Professor professor) {
		this.professors.add(professor);
	}

	/**
	 * 
	 * @param project
	 */
	public void addProject(Project project) {
		this.projects.add(project);
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public String getCode() {
		return this.code;
	}

	public int getCredits() {
		return this.credits;
	}

	@Override
	public String toString() {
		return "Nombre: " + name + ", Descripción: " + description + ", Código: " + code + ", Créditos: " + credits;
	}
}