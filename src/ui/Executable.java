package ui;

import java.util.Scanner;
import java.util.Set;

import model.ProjectsController;

public class Executable {

	public ProjectsController control;
	public Scanner reader;

	public Executable() {
		control = new ProjectsController();
		reader = new Scanner(System.in);
	}

	public int showMenu() {
		int option = 0;
		System.out.println("1. Registar curso");
		System.out.println("2. Registar profesor");
		System.out.println("3. Asignar profesor a curso");
		System.out.println("4. Registar proyecto");
		System.out.println("5. Encontrar proyecto");
		System.out.println("6. Modificar datos del proyecto");
		System.out.println("7. Eliminar proyecto");
		System.out.println("8. Cargar datos de prueba");
		System.out.println("9. Encontrar proyectos sin resultado");
		System.out.println("10. Salir");

		option = Validator.cleanInput(
				"Selecione una opción",
				"opción invalida",
				(input) -> {
					boolean isInteger = Validator.isIntegerParsable(input);

					Set<Integer> validOptions = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

					return isInteger && validOptions.contains(Integer.parseInt(input));
				},
				Integer::parseInt);

		return option;
	}

	public void registerCourse() {
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

		System.out.print("Ingrese el identificador del profesor (Presione enter para no añadir profesor): ");
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

	}

	public void findProject() {
		// TODO - implement Executable.findProject
		throw new UnsupportedOperationException();
	}

	public void modifyProjectData() {
		// TODO - implement Executable.modifyProjectData
		throw new UnsupportedOperationException();
	}

	public void deleteProject() {
		// TODO - implement Executable.deleteProject
		throw new UnsupportedOperationException();
	}

	public void loadTestData() {
		// TODO - implement Executable.loadTestData
		throw new UnsupportedOperationException();
	}

	public void findProjectsWithoutResult() {
		// TODO - implement Executable.findProjectsWithoutResult
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		Executable executable = new Executable();
		int option = 0;
		while (option != 10) {
			option = executable.showMenu();

			switch (option) {
				case 1 -> executable.registerCourse();
				case 2 -> executable.registerProfessor();
				case 3 -> executable.assignProfessorToCourse();
				case 4 -> executable.registerProject();
				case 5 -> executable.findProject();
				case 6 -> executable.modifyProjectData();
				case 7 -> executable.deleteProject();
				case 8 -> executable.loadTestData();
				case 9 -> executable.findProjectsWithoutResult();
				case 10 -> System.out.println("Saliendo...");
				default -> System.out.println("Opción inválida, por favor intente de nuevo.");
			}
		}

		executable.reader.close();
	}

}