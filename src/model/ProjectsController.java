package model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import ui.Response;

public class ProjectsController {

	private ArrayList<Course> courses;
	private ArrayList<Professor> professors;
	private ArrayList<Project> projects;

	public ProjectsController() {
		this.courses = new ArrayList<Course>();
		this.professors = new ArrayList<Professor>();
		this.projects = new ArrayList<Project>();
	}

	/**
	 * 
	 * @param code
	 */
	private Course searchCourse(String code) {
		return courses.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
	}

	/**
	 * 
	 * @param id
	 */
	private Professor searchProfessor(String id) {
		return professors.stream().filter(p -> p.getIdNumber().equals(id)).findFirst().orElse(null);
	}

	/**
	 * 
	 * @param id
	 */
	private Project searchProject(String id) {
		return projects.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
	}

	/**
	 * 
	 * @param name
	 */
	private Project searchProjectByName(String name) {
		return projects.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
	}

	/**
	 * 
	 * @param name
	 * @param desc
	 * @param code
	 * @param credits
	 */
	public Response<Void> addCourse(String name, String desc, String code, int credits) {
		Course foundCourse = searchCourse(code);

		if (foundCourse != null) {
			return Response.failure("El código " + code + " ya existe.");
		}

		Course course = new Course(name, desc, code, credits);
		courses.add(course);

		return Response.success("Curso " + name + " creado con éxito.");
	}

	/**
	 * 
	 * @param name
	 * @param desc
	 * @param code
	 * @param credits
	 * @param professorId
	 */
	public String addCourse(String name, String desc, String code, String credits, String professorId) {
		Course foundCourse = searchCourse(code);

		if (foundCourse != null) {
			return "El código " + code + " ya existe.";
		}

		Course course = new Course(name, desc, code, Integer.parseInt(credits));
		courses.add(course);

		Professor foundProfessor = searchProfessor(professorId);
		if (foundProfessor != null) {
			foundProfessor.setCourse(course);
			course.setProfessor(foundProfessor);
			return "Curso " + name + " creado con éxito y asignado al profesor " + foundProfessor.getName() + ".";
		} else {
			return "Curso " + name + " creado con éxito pero no se encontró el profesor con ID: " + professorId;
		}
	}

	/**
	 * 
	 * @param idTypeIndex
	 * @param idNumber
	 * @param name
	 * @param email
	 */
	public String addProfessor(int idTypeIndex, String idNumber, String name, String email) {
		Professor foundProfessor = searchProfessor(idNumber);

		if (foundProfessor != null) {
			return "El ID " + idNumber + " ya existe.";
		}

		DocumentType idType = DocumentType.getByIndex(idTypeIndex);
		Professor professor = new Professor(idNumber, idType, name, email);
		professors.add(professor);

		return "Profesor " + name + " creado con éxito.";
	}

	/**
	 * 
	 * @param professorId
	 * @param courseCode
	 */
	public Response<Void> assignProfessorToCourse(String professorId, String courseCode) {
		Course course = searchCourse(courseCode);
		if (course == null) {
			return Response.failure("El curso con código " + courseCode + " no existe.");
		}

		Professor professor = searchProfessor(professorId);
		if (professor == null) {
			return Response.failure("El profesor con ID " + professorId + " no existe.");
		}

		course.setProfessor(professor);
		professor.setCourse(course);

		return Response.success("Profesor " + professor.getName() + " asignado al curso " + course.getName() + ".");
	}

	/**
	 * 
	 * @param professorId
	 * @param courseId
	 * @param name
	 * @param benefCompany
	 * @param keyWords
	 * @param desc
	 * @param statementURL
	 */
	public Response<String> addProject(String professorId, String courseId, String name, String benefCompany,
			ArrayList<String> keyWords, String desc, String statementURL, int projectTypeIndex) {
		Course course = searchCourse(courseId);
		if (course == null) {
			return Response.failure("El curso con código " + courseId + " no existe.");
		}

		Professor professor = searchProfessor(professorId);
		if (professor == null) {
			return Response.failure("El profesor con ID " + professorId + " no existe.");
		}

		if (professor.getCourses().contains(course)) {
			return Response.failure("El profesor " + professor.getName() + " no está asignado al curso " + courseId + ".");
		}

		Project foundProject = searchProjectByName(name);
		if (foundProject != null) {
			return Response.failure("El proyecto " + name + " ya existe.");
		}

		ProjectType projectType = ProjectType.getByIndex(projectTypeIndex);

		Project project = new Project(name, benefCompany, keyWords, desc, statementURL, projectType);

		projects.add(project);
		course.addProject(project);

		return Response.success("Proyecto " + name + " creado con éxito y asignado al curso " + course.getName() + ".");
	}

	/**
	 * 
	 * @param professorId
	 * @param courseId
	 * @param name
	 * @param benefCompany
	 * @param keyWords
	 * @param desc
	 * @param statementURL
	 * @param orgProjectId
	 */
	public Response<Void> addProject(String professorId, String courseId, String name, String benefCompany,
			ArrayList<String> keyWords, String desc, String statementURL, int projectTypeIndex, String orgProjectId) {
		Course course = searchCourse(courseId);
		if (course == null) {
			return Response.failure("El curso con código " + courseId + " no existe.");
		}

		Professor professor = searchProfessor(professorId);
		if (professor == null) {
			return Response.failure("El profesor con ID " + professorId + " no existe.");
		}

		if (professor.getCourses().contains(course)) {
			return Response.failure("El profesor " + professor.getName() + " no está asignado al curso " + courseId + ".");
		}

		Project foundProject = searchProjectByName(name);
		if (foundProject != null) {
			return Response.failure("El proyecto " + name + " ya existe.");
		}

		Project orgProject = searchProject(orgProjectId);
		if (orgProject == null) {
			return Response.failure("El proyecto original con ID " + orgProjectId + " no existe.");
		}

		ProjectType projectType = ProjectType.getByIndex(projectTypeIndex);

		Project project = new Project(name, benefCompany, keyWords, desc, statementURL, projectType, orgProject);

		projects.add(project);
		course.addProject(project);

		return Response.success("Proyecto " + name + " creado con éxito y asignado al curso " + course.getName() + ".");
	}

	/**
	 * 
	 * @param id
	 */
	public String getProjectDataById(String id) {
		// TODO - implement ProjectsController.getProjectDataById
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param name
	 */
	public String getProjectDataByName(String name) {
		// TODO - implement ProjectsController.getProjectDataByName
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param projectId
	 * @param name
	 * @param benefCompany
	 * @param keyWords
	 * @param desc
	 * @param statementURL
	 */
	public String editProject(String projectId, String name, String benefCompany, ArrayList<String> keyWords, String desc,
			String statementURL) {
		// TODO - implement ProjectsController.editProject
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param projectId
	 * @param date
	 * @param group
	 * @param url
	 * @param phaseIndex
	 * @param typeIndex
	 */
	public String addProjectResult(String projectId, LocalDate date, int group, String url, int phaseIndex,
			int typeIndex) {
		// TODO - implement ProjectsController.addProjectResult
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param projectId
	 */
	public String deleteProject(String projectId) {
		// TODO - implement ProjectsController.deleteProject
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param file
	 */
	public String loadTestData(File file) {
		// TODO - implement ProjectsController.loadTestData
		throw new UnsupportedOperationException();
	}

	public ArrayList<String> getProjectsWithoutResult() {
		// TODO - implement ProjectsController.getProjectsWithoutResult
		throw new UnsupportedOperationException();
	}

}