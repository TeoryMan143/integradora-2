package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ui.Validator;
import model.Course;
import model.CyclePhase;
import model.Project;
import model.ProjectType;
import model.Result;
import model.ResultType;

public class FileParser {
  private Path dataPath;

  public FileParser() {
    this.dataPath = Path.of(System.getProperty("user.dir"), "data");
  }

  public FileParser(String dataPath) {
    this.dataPath = Path.of(dataPath);
  }

  public ArrayList<Result> loadResults() throws IOException {
    List<String> lines = Files.readAllLines(dataPath.resolve("results.csv"));

    if (lines.isEmpty()) {
      throw new IOException("El archivo está vacío");
    }

    if (!lines.get(0).equals("id,date,group,url,phase,type")) {
      throw new IOException("El formato del archivo es incorrecto");
    }

    ArrayList<Result> results = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 6) {
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

      Result result = new Result(id, date, group, url, phase, type);
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
        .equals("id,name,beneficiaryCompany,keywords,description,statementURL,active,projectType,results,orgProject")) {
      throw new IOException("El formato del archivo es incorrecto");
    }

    ArrayList<Project> projects = new ArrayList<>();
    ArrayList<Result> results = loadResults();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 10) {
        throw new IOException("El formato del archivo es incorrecto");
      }

      String id = parts[0];
      String name = parts[1];
      String benefCompany = parts[2];
      String keyWordsText = parts[3];

      ArrayList<String> keyWords = new ArrayList<>();
      for (String keyWord : keyWordsText.split("|")) {
        keyWords.add(keyWord);
      }

      String desc = parts[4];
      String statementURL = parts[5];

      String projectTypeText = parts[6];

      if (!Validator.isIntegerParsable(projectTypeText)) {
        throw new IOException("El formato del tipo es incorrecto, debe ser un número entero");
      }

      ProjectType projectType = ProjectType.getByIndex(Integer.parseInt(projectTypeText));

      Optional<Project> orgProject = projects.stream()
          .filter(p -> p.getId().equals(parts[8]))
          .findFirst();

      Project project = null;

      if (orgProject.isPresent()) {
        project = new Project(id, name, benefCompany, keyWords, desc, statementURL, projectType,
            orgProject.get());
      } else {
        project = new Project(id, name, benefCompany, keyWords, desc, statementURL, projectType);
      }

      String resultsText = parts[7];

      if (!resultsText.equals("")) {
        for (String resultId : resultsText.split("|")) {
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
}
