package model;

import java.util.ArrayList;
import java.util.UUID;

public class Project {

	private String id;
	private String name;
	private String beneficiaryCompany;
	private ArrayList<String> keyWords;
	private String description;
	private String statementURL;
	private boolean active;
	private ProjectType projectType;
	private ArrayList<Result> results;
	private Project orgProject;

	/**
	 * 
	 * @param name
	 * @param benefCompany
	 * @param keyWords
	 * @param desc
	 * @param statementURL
	 */
	public Project(String name, String benefCompany, ArrayList<String> keyWords, String desc, String statementURL,
			ProjectType projectType) {
		this.name = name;
		this.beneficiaryCompany = benefCompany;
		this.keyWords = keyWords;
		this.description = desc;
		this.statementURL = statementURL;
		this.active = true;
		this.projectType = projectType;
		this.results = new ArrayList<Result>();
		this.id = UUID.randomUUID().toString();
	}

	/**
	 * 
	 * @param name
	 * @param benefCompany
	 * @param keyWords
	 * @param desc
	 * @param statementURL
	 * @param orgProject
	 */
	public Project(
			String name,
			String benefCompany,
			ArrayList<String> keyWords,
			String desc,
			String statementURL,
			ProjectType projectType,
			Project orgProject) {

		this.name = name;
		this.beneficiaryCompany = benefCompany;
		this.keyWords = keyWords;
		this.description = desc;
		this.statementURL = statementURL;
		this.active = true;
		this.projectType = projectType;
		this.results = new ArrayList<Result>();
		this.id = UUID.randomUUID().toString();
		this.orgProject = orgProject;
	}

	public Project(String id, String name, String benefCompany, ArrayList<String> keyWords, String desc,
			String statementURL,
			boolean active, ProjectType projectType, Project orgProject) {
		this.id = id;
		this.name = name;
		this.beneficiaryCompany = benefCompany;
		this.keyWords = keyWords;
		this.description = desc;
		this.statementURL = statementURL;
		this.active = active;
		this.projectType = projectType;
		this.orgProject = orgProject;
	}

	public Project(String id, String name, String benefCompany, ArrayList<String> keyWords, String desc,
			String statementURL,
			boolean active, ProjectType projectType) {
		this.id = id;
		this.name = name;
		this.beneficiaryCompany = benefCompany;
		this.keyWords = keyWords;
		this.description = desc;
		this.statementURL = statementURL;
		this.active = active;
		this.projectType = projectType;
	}

	/**
	 * 
	 * @param res
	 */
	public void addResult(Result res) {
		this.results.add(res);
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getBeneficiaryCompany() {
		return this.beneficiaryCompany;
	}

	/**
	 * 
	 * @param beneficiaryCompany
	 */
	public void setBeneficiaryCompany(String beneficiaryCompany) {
		this.beneficiaryCompany = beneficiaryCompany;
	}

	public ArrayList<String> getKeyWords() {
		return this.keyWords;
	}

	/**
	 * 
	 * @param keyWords
	 */
	public void setKeyWords(ArrayList<String> keyWords) {
		this.keyWords = keyWords;
	}

	public String getDescription() {
		return this.description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatementURL() {
		return this.statementURL;
	}

	/**
	 * 
	 * @param statementURL
	 */
	public void setStatementURL(String statementURL) {
		this.statementURL = statementURL;
	}

	public boolean getActive() {
		return this.active;
	}

	/**
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id del proyecto: ").append(id).append("\n");
		sb.append("Nombre: ").append(name).append("\n");
		sb.append("Empresa beneficiaria: ").append(beneficiaryCompany).append("\n");
		sb.append("Palabras clave: ").append(keyWords).append("\n");
		sb.append("Descripción: ").append(description).append("\n");
		sb.append("URL del enunciado: ").append(statementURL).append("\n");
		sb.append("Tipo de projecto: ").append(projectType).append("\n");
		if (orgProject != null) {
			sb.append("Proyecto original: ").append(orgProject.getId()).append("\n");
		}
		return sb.toString();
	}

	public ArrayList<Result> getResults() {
		return this.results;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}
}