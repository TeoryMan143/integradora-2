package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ui.Validator;
import model.Course;
import model.CyclePhase;
import model.DocumentType;
import model.Professor;
import model.Project;
import model.ProjectType;
import model.Result;
import model.ResultType;

public class FileParser {
  private Path dataPath;

  public FileParser() {
    this.dataPath = Path.of(System.getProperty("user.dir"), "data");
  }

  public FileParser(String dataPath) throws InvalidPathException {
    this.dataPath = Path.of(dataPath);
  }

  public ArrayList<Result> loadResults() throws IOException {
    List<String> lines = Files.readAllLines(dataPath.resolve("results.csv"));

    if (lines.isEmpty()) {
      throw new IOException("El archivo está vacío");
    }

    if (!lines.get(0).equals("id,date,group,url,phase,type,active")) {
      throw new IOException("El formato del archivo es incorrecto");
    }

    ArrayList<Result> results = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 7) {
        throw new IOException("El formato del archivo es incorrecto");
      }
      String id = parts[0];
      String dateText = parts[1];

      if (!Validator.isLocalDateParsable(dateText)) {
        throw new IOException("La fecha tiene un formato incorrecto, debe ser yyyy-mm-dd");
      }

      LocalDate date = LocalDate.parse(dateText);

      String groupText = parts[2];

      if (!Validator.isIntegerParsable(groupText)) {
        throw new IOException("El formato del grupo es incorrecto, debe ser un número entero");
      }

      int group = Integer.parseInt(groupText);

      String url = parts[3];

      String phaseText = parts[4];

      if (!Validator.isIntegerParsable(phaseText)) {
        throw new IOException("El formato de la fase es incorrecto, debe ser un número entero");
      }

      CyclePhase phase = CyclePhase.getByIndex(Integer.parseInt(phaseText));

      String typeText = parts[5];

      if (!Validator.isIntegerParsable(typeText)) {
        throw new IOException("El formato del tipo es incorrecto, debe ser un número entero");
      }
      ResultType type = ResultType.getByIndex(Integer.parseInt(typeText));

      String activeText = parts[6];

      if (!activeText.equals("true") && !activeText.equals("false")) {
        throw new IOException("El formato del estado activo es incorrecto, debe ser true o false");
      }
      boolean active = Boolean.parseBoolean(activeText);

      Result result = new Result(id, date, group, url, phase, type, active);
      results.add(result);
    }

    return results;
  }

  public ArrayList<Project> loadProjects() throws IOException {
    List<String> lines = Files.readAllLines(dataPath.resolve("projects.csv"));

    if (lines.isEmpty()) {
      throw new IOException("El archivo está vacío");
    }

    if (!lines.get(0)
        .equals("id,name,beneficiaryCompany,keywords,description,statementURL,projectType,results,orgProject")) {
      throw new IOException("(Projectos) El formato del archivo es incorrecto, no coincide con el encabezado esperado");
    }

    ArrayList<Project> projects = new ArrayList<>();
    ArrayList<Result> results = loadResults();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 7) {
        throw new IOException(
            "(Projectos) El formato del archivo es incorrecto, no hay campos suficientes");
      }

      String id = parts[0];
      String name = parts[1];
      String benefCompany = parts[2];
      String keyWordsText = parts[3];

      ArrayList<String> keyWords = new ArrayList<>();

      for (String keyWord : keyWordsText.split("\\|")) {
        keyWords.add(keyWord);
      }

      String desc = parts[4];
      String statementURL = parts[5];

      String projectTypeText = parts[6];

      if (!Validator.isIntegerParsable(projectTypeText)) {
        throw new IOException("El formato del tipo es incorrecto, debe ser un número entero");
      }

      ProjectType projectType = ProjectType.getByIndex(Integer.parseInt(projectTypeText));

      Optional<Project> orgProject = Optional.empty();

      if (parts.length > 8) {
        String orgProjectId = parts[8];

        if (!orgProjectId.equals("")) {
          orgProject = projects.stream()
              .filter(p -> p.getId().equals(orgProjectId))
              .findFirst();
        }
      }

      Project project = null;

      if (orgProject.isPresent()) {
        project = new Project(id, name, benefCompany, keyWords, desc, statementURL, projectType,
            orgProject.get());
      } else {
        project = new Project(id, name, benefCompany, keyWords, desc, statementURL, projectType);
      }

      String resultsText = null;

      if (parts.length > 7) {
        resultsText = parts[7];
      }

      if (resultsText != null && !resultsText.equals("")) {
        for (String resultId : resultsText.split("\\|")) {
          Optional<Result> result = results.stream()
              .filter(r -> r.getId().equals(resultId))
              .findFirst();

          if (result.isPresent()) {
            project.addResult(result.get());
          }
        }
      }

      projects.add(project);
    }

    return projects;
  }

  public ArrayList<Course> loadCourses(ArrayList<Project> allProjects) throws IOException {
    List<String> lines = Files.readAllLines(dataPath.resolve("courses.csv"));

    if (lines.isEmpty()) {
      throw new IOException("El archivo está vacío");
    }

    if (!lines.get(0).equals("code,name,description,credits,projects")) {
      throw new IOException("El formato del archivo es incorrecto");
    }

    ArrayList<Course> courses = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 5) {
        throw new IOException("El formato del archivo es incorrecto");
      }

      String code = parts[0];
      String name = parts[1];
      String description = parts[2];
      String creditsText = parts[3];

      if (!Validator.isIntegerParsable(creditsText)) {
        throw new IOException("El formato de los créditos es incorrecto, debe ser un número entero");
      }

      int credits = Integer.parseInt(creditsText);

      String projectsText = parts[4];

      ArrayList<Project> projects = new ArrayList<>();

      if (!projectsText.equals("")) {
        for (String projectId : projectsText.split("\\|")) {
          Optional<Project> project = allProjects.stream()
              .filter(p -> p.getId().equals(projectId))
              .findFirst();

          if (project.isPresent()) {
            projects.add(project.get());
          }
        }
      }

      courses.add(new Course(name, description, code, credits, projects));
    }

    return courses;
  }

  public ArrayList<Professor> loadProfessors(ArrayList<Course> allCourses) throws IOException {
    List<String> lines = Files.readAllLines(dataPath.resolve("professors.csv"));

    if (lines.isEmpty()) {
      throw new IOException("El archivo está vacío");
    }

    if (!lines.get(0).equals("idNumber,name,email,type,courses")) {
      throw new IOException("El formato del archivo es incorrecto");
    }

    ArrayList<Professor> professors = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 5) {
        throw new IOException("El formato del archivo es incorrecto");
      }

      String idNumber = parts[0];
      String name = parts[1];
      String email = parts[2];
      String typeText = parts[3];

      if (!Validator.isIntegerParsable(typeText)) {
        throw new IOException("El formato del tipo es incorrecto, debe ser un número entero");
      }

      DocumentType type = DocumentType.getByIndex(Integer.parseInt(typeText));

      ArrayList<Course> courses = new ArrayList<>();

      String coursesText = parts[4];

      if (!coursesText.equals("")) {
        for (String courseCode : coursesText.split("\\|")) {
          Optional<Course> course = allCourses.stream()
              .filter(c -> c.getCode().equals(courseCode))
              .findFirst();

          if (course.isPresent()) {
            courses.add(course.get());
          }
        }
      }

      professors.add(new Professor(idNumber, type, name, email, courses));
    }

    return professors;
  }
}
