package ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.ProjectsController;

public class Executable {

	public ProjectsController control;
	public Scanner reader;
	private boolean isAdmin;

	public Executable() {
		control = new ProjectsController();
		reader = new Scanner(System.in);
		isAdmin = false;
	}

	public boolean getAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int showMenu() {
		int option = 0;
		System.out.println("1. Registar curso");
		System.out.println("2. Registar profesor");
		System.out.println("3. Asignar profesor a curso");
		System.out.println("4. Registar proyecto");
		System.out.println("5. Encontrar proyecto");
		System.out.println("6. Modificar datos del proyecto");
		System.out.println("7. Añadir resultado");
		System.out.println("8. Eliminar proyecto");
		System.out.println("9. Cargar datos de prueba");
		System.out.println("10. Encontrar proyectos sin resultado");
		System.out.println("11. Salir");

		option = Validator.cleanInput(
				"Selecione una opción",
				"opción invalida",
				(input) -> {
					boolean isInteger = Validator.isIntegerParsable(input);

					int sOption = Integer.parseInt(input);

					return isInteger && sOption >= 1 && sOption <= 11;
				},
				Integer::parseInt);

		return option;
	}

	public void registerCourse() {

		if (!isAdmin) {
			System.out.println("No tienes permisos para registrar un curso.");
			return;
		}

		System.out.print("Ingrese el nombre del curso:");
		String name = reader.nextLine();

		System.out.print("Ingrese la descripción del curso:");
		String description = reader.nextLine();
		String code = Validator.cleanInput("Ingresa el código del curso (numero de 5 digitos)", "Código invalido",
				(input) -> Validator.isIntegerParsable(input) && input.length() == 5);

		int credits = Validator.cleanInput("Ingrese la cantidad de creditos del curso", "Creditos invalidos", (input) -> {
			boolean isInteger = Validator.isIntegerParsable(input);
			return isInteger && Integer.parseInt(input) > 0 && Integer.parseInt(input) <= 10;
		}, Integer::parseInt);

		System.out.print("Ingrese el identificador del profesor (Presione enter para omitir): ");
		String professorId = reader.nextLine();

		Response<Void> response = null;

		if (professorId.isEmpty()) {
			response = control.addCourse(name, description, code, credits);
		} else {
			response = control.addCourse(name, description, code, credits, professorId);
		}

		System.out.println("\n" + response.getMessage());
	}

	public void registerProfessor() {

		if (!isAdmin) {
			System.out.println("No tienes permisos para registrar un profesor.");
			return;
		}

		boolean run = true;

		do {
			System.out.println("Ingrese el tipo de documento:");
			System.out.println("1. Cédula de ciudadanía");
			System.out.println("2. Cédula de extranjería");
			System.out.println("3. Pasaporte");

			int idTypeIndex = Validator.cleanInput("Seleccione una opción", "Opción inválida", (input) -> {
				boolean isInteger = Validator.isIntegerParsable(input);
				return isInteger && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 3;
			}, Integer::parseInt) - 1;

			System.out.print("Ingrese el número de documento:");
			String idNumber = reader.nextLine();

			String name = Validator.cleanInput("Ingrese el nombre del profesor", "Nombre inválido",
					(input) -> !input.isEmpty());

			String email = Validator.cleanInput("Ingrese el correo electrónico del profesor", "Correo inválido",
					(input) -> !input.isEmpty());

			Response<Void> res = control.addProfessor(idTypeIndex, idNumber, name, email);

			System.out.println("\n" + res.getMessage());
			run = !res.getSuccess();
		} while (run);
	}

	public void assignProfessorToCourse() {

		if (!isAdmin) {
			System.out.println("No tienes permisos para asignar un profesor a un curso.");
			return;
		}

		boolean run = true;

		do {
			System.out.println(control.listProfessors());
			System.out.print("Ingrese el ID del profesor:");
			String professorId = reader.nextLine();

			System.out.println(control.listCourses());
			System.out.print("Ingrese el código del curso:");
			String courseCode = reader.nextLine();

			Response<Void> res = control.assignProfessorToCourse(professorId, courseCode);

			System.out.println("\n" + res.getMessage());
			run = !res.getSuccess();
		} while (run);
	}

	public void registerProject() {
		boolean run = true;
		do {
			System.out.println(control.listCourses());
			System.out.print("Ingrese el código del curso:");
			String courseCode = reader.nextLine();

			System.out.print("Ingrese el nombre del proyecto:");
			String name = reader.nextLine();

			System.out.print("Ingrese la empresa beneficiaria:");
			String beneficiaryCompany = reader.nextLine();

			ArrayList<String> keyWords = Validator.cleanInput("Ingrese las palabras clave (separadas por comas)",
					"Palabras clave inválidas",
					(t) -> !t.isEmpty(), (t) -> new ArrayList<>(List.of(t.split(","))));

			System.out.print("Ingrese la descripción del proyecto:");
			String description = reader.nextLine();

			System.out.print("Ingrese la URL del enunciado:");
			String statementURL = reader.nextLine();

			System.out.println("Tipo de proyecto: ");
			System.out.println("1. Integrador");
			System.out.println("2. De curso");
			System.out.println("3. Final");
			int type = Validator.cleanInput(
					"Ingresa el tipo de proyecto",
					"Opción inválida",
					(t) -> {
						if (!Validator.isIntegerParsable(t)) {
							return false;
						}

						int index = Integer.parseInt(t) - 1;

						return index >= 0 && index < 3;
					}, Integer::parseInt);

			System.out.println("Ingrese el id del projecto base (Presione enter para saltar):");
			String baseProjectId = reader.nextLine();

			Response<Void> res;

			if (baseProjectId.isEmpty()) {
				res = control.addProject(courseCode, name, beneficiaryCompany, keyWords, description, statementURL,
						type);
			} else {
				res = control.addProject(courseCode, name, beneficiaryCompany, keyWords, description, statementURL,
						type, baseProjectId);
			}

			System.out.println("\n" + res.getMessage());
			run = !res.getSuccess();
		} while (run);
	}

	public void findProject() {
		System.out.println("Encuentra un proyecto por: ");
		System.out.println("1. ID");
		System.out.println("2. Nombre");
		System.out.println("3. Palabra clave");

		String data = switch (Validator.cleanInput(
				"Seleccione una opción",
				"Opción inválida",
				(input) -> {
					boolean isInteger = Validator.isIntegerParsable(input);
					return isInteger && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 3;
				},
				Integer::parseInt)) {
			case 1 -> {
				System.out.print("Ingrese el ID del proyecto:");
				yield control.getProjectDataById(reader.nextLine());
			}
			case 2 -> {
				System.out.print("Ingrese el nombre del proyecto:");
				yield control.getProjectDataByName(reader.nextLine());
			}
			case 3 -> {
				System.out.print("Ingrese la palabra clave:");
				yield control.getProjectDataByKeyword(reader.nextLine());
			}
			default -> null;
		};

		System.out.println("\n" + data);
	}

	public void modifyProjectData() {
		System.out.println(control.listProjects());
		System.out.println("Ingresa el id del proyecto a modificar:");
		String projectId = reader.nextLine();

		boolean run = true;

		do {
			System.out.println("¿Que deseas modificar?");
			System.out.println("1. Nombre");
			System.out.println("2. Empresa beneficiaria");
			System.out.println("3. Palabras clave");
			System.out.println("4. Descripción");
			System.out.println("5. URL del enunciado");

			int option = Validator.cleanInput(
					"Seleccione una opción",
					"Opción inválida",
					(input) -> {
						boolean isInteger = Validator.isIntegerParsable(input);
						return isInteger && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 5;
					},
					Integer::parseInt);

			String info = switch (option) {
				case 1 -> {
					System.out.print("Ingrese el nuevo nombre:");
					String newName = reader.nextLine();
					yield control.setProjectName(projectId, newName).getMessage();
				}
				case 2 -> {
					System.out.print("Ingrese la nueva empresa beneficiaria:");
					String newBeneficiaryCompany = reader.nextLine();
					yield control.setProjectBeneficiaryCompany(projectId, newBeneficiaryCompany).getMessage();
				}
				case 3 -> {
					System.out.print("Ingrese las nuevas palabras clave (separadas por comas):");
					String newKeyWords = reader.nextLine();
					yield control.setProjectKeyWords(projectId, new ArrayList<>(List.of(newKeyWords.split(",")))).getMessage();
				}
				case 4 -> {
					System.out.print("Ingrese la nueva descripción:");
					String newDescription = reader.nextLine();
					yield control.setProjectDescription(projectId, newDescription).getMessage();
				}
				case 5 -> {
					System.out.print("Ingrese la nueva URL del enunciado:");
					String newStatementURL = reader.nextLine();
					yield control.setProjectStatementURL(projectId, newStatementURL).getMessage();
				}
				default -> null;
			};

			System.out.println("\n" + info);

			run = Validator.askYesNo("¿Desea modificar otro dato? (si/no)");
		} while (run);

	}

	public void addResult() {
		System.out.println("Ingrese el id del proyecto:");
		String projectId = reader.nextLine();

		LocalDate date = Validator.cleanInput("Ingrese la fecha del resultado (YYYY-MM-DD)", "Fecha inválida",
				Validator::isLocalDateParsable, LocalDate::parse);

		int group = Validator.cleanInput("Ingrese el grupo", "Grupo inválido",
				(input) -> Validator.isIntegerParsable(input) && Integer.parseInt(input) > 0, Integer::parseInt);

		String url = Validator.cleanInput("Ingrese la URL del resultado", "URL inválida",
				(input) -> !input.isEmpty());

		System.out.println("1. Diagrama de clases");
		System.out.println("2. Infografía");
		System.out.println("3. Modelo de datos");
		System.out.println("4. Plan de pruebas");
		System.out.println("5. Diagrama de despliegue");
		int typeIndex = Validator.cleanInput("Seleccione el tipo de resultado", "Opción inválida",
				(input) -> {
					boolean isInteger = Validator.isIntegerParsable(input);
					return isInteger && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 5;
				},
				Integer::parseInt) - 1;

		System.out.println("1. Análisis de requerimientos");
		System.out.println("2. Diseño");
		System.out.println("3. Construcción");
		System.out.println("4. Pruebas");
		System.out.println("5. Despliegue");
		int phaseIndex = Validator.cleanInput("Seleccione la fase del ciclo de vida", "Opción inválida",
				(input) -> {
					boolean isInteger = Validator.isIntegerParsable(input);
					return isInteger && Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 5;
				},
				Integer::parseInt) - 1;

		Response<Void> res = control.addProjectResult(projectId, date, group, url, phaseIndex, typeIndex);
		System.out.println("\n" + res.getMessage());
	}

	public void deleteProject() {
		System.out.println("Ingrese el id del proyecto a eliminar:");
		String projectId = reader.nextLine();

		String info = control.deleteProject(projectId);
		System.out.println("\n" + info);
	}

	public void loadTestData() {
		// TODO - implement Executable.loadTestData
		throw new UnsupportedOperationException();
	}

	public void findProjectsWithoutResult() {
		System.out.println("Projectos sin resultado:");
		System.out.println(control.getProjectsWithoutResult());
	}

	public static void main(String[] args) {
		Executable executable = new Executable();
		int option = 0;

		boolean admin = Validator.askYesNo("¿Es administrador? (si/no)");
		executable.setAdmin(admin);

		while (option != 11) {
			option = executable.showMenu();

			switch (option) {
				case 1 -> executable.registerCourse();
				case 2 -> executable.registerProfessor();
				case 3 -> executable.assignProfessorToCourse();
				case 4 -> executable.registerProject();
				case 5 -> executable.findProject();
				case 6 -> executable.modifyProjectData();
				case 7 -> System.out.println("Add result");
				case 8 -> executable.deleteProject();
				case 9 -> executable.loadTestData();
				case 10 -> executable.findProjectsWithoutResult();
				case 11 -> System.out.println("Saliendo...");
				default -> System.out.println("Opción inválida, por favor intente de nuevo.");
			}
		}

		executable.reader.close();
	}
}