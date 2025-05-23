package model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import utils.Response;

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
	public Response<Void> addCourse(String name, String desc, String code, int credits, String professorId) {
		Course foundCourse = searchCourse(code);

		if (foundCourse != null) {
			return Response.failure("El código " + code + " ya existe.");
		}

		Course course = new Course(name, desc, code, credits);
		courses.add(course);

		Professor foundProfessor = searchProfessor(professorId);
		if (foundProfessor != null) {
			foundProfessor.setCourse(course);
			return Response
					.success("Curso " + name + " creado con éxito y asignado al profesor " + foundProfessor.getName() + ".");
		} else {
			return Response.failure(
					"Curso " + name + " creado con éxito pero no se encontró el profesor con ID: " + professorId);
		}
	}

	/**
	 * 
	 * @param idTypeIndex
	 * @param idNumber
	 * @param name
	 * @param email
	 */
	public Response<Void> addProfessor(int idTypeIndex, String idNumber, String name, String email) {
		Professor foundProfessor = searchProfessor(idNumber);

		if (foundProfessor != null) {
			return Response.failure("El profesor con ID " + idNumber + " ya existe.");
		}

		DocumentType idType = DocumentType.getByIndex(idTypeIndex);
		Professor professor = new Professor(idNumber, idType, name, email);
		professors.add(professor);

		return Response.success("Profesor " + name + " creado con éxito.");
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

		professor.setCourse(course);

		return Response.success("Profesor " + professor.getName() + " asignado al curso " + course.getName() + ".");
	}

	/**
	 * 
	 * @param courseId
	 * @param name
	 * @param benefCompany
	 * @param keyWords
	 * @param desc
	 * @param statementURL
	 */
	public Response<Void> addProject(String courseId, String name, String benefCompany,
			ArrayList<String> keyWords, String desc, String statementURL, int projectTypeIndex) {
		Course course = searchCourse(courseId);
		if (course == null) {
			return Response.failure("El curso con código " + courseId + " no existe.");
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
	 * @param courseId
	 * @param name
	 * @param benefCompany
	 * @param keyWords
	 * @param desc
	 * @param statementURL
	 * @param orgProjectId
	 */
	public Response<Void> addProject(String courseId, String name, String benefCompany,
			ArrayList<String> keyWords, String desc, String statementURL, int projectTypeIndex, String orgProjectId) {
		Course course = searchCourse(courseId);
		if (course == null) {
			return Response.failure("El curso con código " + courseId + " no existe.");
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
		Project foundProject = searchProject(id);
		if (foundProject == null) {
			return "El proyecto con ID " + id + " no existe.";
		}

		return foundProject.toString();
	}

	/**
	 * 
	 * @param name
	 */
	public String getProjectDataByName(String name) {
		Project foundProject = searchProjectByName(name);
		if (foundProject == null) {
			return "El proyecto con nombre " + name + " no existe.";
		}

		return foundProject.toString();
	}

	public String getProjectDataByKeyword(String keyword) {
		StringBuilder sb = new StringBuilder();
		sb.append("Proyectos que contienen la palabra clave ").append(keyword).append(":\n");
		for (Project project : projects) {
			if (project.getKeyWords().contains(keyword)) {
				sb.append(project.toString()).append("\n");
			}
		}
		return sb.toString();
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
	public Response<Void> addProjectResult(String projectId, LocalDate date, int group, String url, int phaseIndex,
			int typeIndex) {
		Project foundProject = searchProject(projectId);
		if (foundProject == null) {
			return Response.failure("El proyecto con ID " + projectId + " no existe.");
		}
		ResultType type = ResultType.getByIndex(typeIndex);
		CyclePhase phase = CyclePhase.getByIndex(phaseIndex);

		Result result = new Result(projectId, group, url, phase, type);

		foundProject.addResult(result);
		return Response.success("Resultado del proyecto " + foundProject.getName() + " agregado con éxito.");
	}

	/**
	 * 
	 * @param projectId
	 */
	public String deleteProject(String projectId) {
		Project foundProject = searchProject(projectId);
		if (foundProject == null) {
			return "El proyecto con ID " + projectId + " no existe.";
		}

		foundProject.setActive(false);
		return "Proyecto " + foundProject.getName() + " eliminado con éxito.";
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
		ArrayList<String> projectsWithoutResult = new ArrayList<>();

		for (Project project : projects) {
			if (project.getResults().isEmpty()) {
				projectsWithoutResult.add(project.toString());
			}
		}

		return projectsWithoutResult;
	}

	public String listProfessors() {
		StringBuilder sb = new StringBuilder();
		sb.append("Lista de profesores:\n");
		for (int i = 0; i < professors.size(); i++) {
			Professor professor = professors.get(i);
			sb.append(i + 1).append(" ").append(professor.toString()).append("\n");
		}
		return sb.toString();
	}

	public String listCourses() {
		StringBuilder sb = new StringBuilder();
		sb.append("Lista de cursos:\n");
		for (int i = 0; i < courses.size(); i++) {
			Course course = courses.get(i);
			sb.append(i + 1).append(" ").append(course.toString()).append("\n");
		}
		return sb.toString();
	}

	public String listProjects() {
		StringBuilder sb = new StringBuilder();
		sb.append("Lista de proyectos:\n");
		for (int i = 0; i < projects.size(); i++) {
			Project project = projects.get(i);
			sb.append(i + 1).append(" ").append(project.toString()).append("\n");
		}
		return sb.toString();
	}

	public Response<Void> setProjectName(String projectId, String name) {
		Project foundProject = searchProject(projectId);

		if (foundProject == null) {
			return Response.failure("El proyecto con ID " + projectId + " no existe.");
		}

		foundProject.setName(name);
		return Response.success("Nombre del proyecto " + projectId + " cambiado a " + name + ".");
	}

	public Response<Void> setProjectBeneficiaryCompany(String projectId, String beneficiaryCompany) {
		Project foundProject = searchProject(projectId);

		if (foundProject == null) {
			return Response.failure("El proyecto con ID " + projectId + " no existe.");
		}

		foundProject.setBeneficiaryCompany(beneficiaryCompany);
		return Response
				.success("Empresa beneficiaria del proyecto " + projectId + " fue cambiada a " + beneficiaryCompany + ".");
	}

	public Response<Void> setProjectKeyWords(String projectId, ArrayList<String> keyWords) {
		Project foundProject = searchProject(projectId);

		if (foundProject == null) {
			return Response.failure("El proyecto con ID " + projectId + " no existe.");
		}

		foundProject.setKeyWords(keyWords);
		return Response.success("Palabras clave del proyecto " + projectId + " cambiadas a " + keyWords.toString() + ".");
	}

	public Response<Void> setProjectDescription(String projectId, String description) {
		Project foundProject = searchProject(projectId);

		if (foundProject == null) {
			return Response.failure("El proyecto con ID " + projectId + " no existe.");
		}

		foundProject.setDescription(description);
		return Response.success("Descripción del proyecto " + projectId + " cambiada a " + description + ".");
	}

	public Response<Void> setProjectStatementURL(String projectId, String statementURL) {
		Project foundProject = searchProject(projectId);

		if (foundProject == null) {
			return Response.failure("El proyecto con ID " + projectId + " no existe.");
		}

		foundProject.setStatementURL(statementURL);
		return Response.success("URL del enunciado del proyecto " + projectId + " cambiado a " + statementURL + ".");
	}

	public int getCoursesSize() {
		return courses.size();
	}

	public int getProfessorsSize() {
		return professors.size();
	}

	public int getProjectsSize() {
		return projects.size();
	}
}