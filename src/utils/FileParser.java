package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ui.Validator;
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
      throw new IOException("File is empty");
    }

    if (!lines.get(0).equals("id,date,group,url,phase,type")) {
      throw new IOException("File format is incorrect");
    }

    ArrayList<Result> results = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 6) {
        throw new IOException("File format is incorrect");
      }
      String id = parts[0];
      String dateText = parts[1];

      if (!Validator.isLocalDateParsable(dateText)) {
        throw new IOException("Date format is incorrect");
      }

      LocalDate date = LocalDate.parse(dateText);

      String groupText = parts[2];

      if (!Validator.isIntegerParsable(groupText)) {
        throw new IOException("Group format is incorrect, it should be integer");
      }

      int group = Integer.parseInt(groupText);

      String url = parts[3];

      String phaseText = parts[4];

      if (!Validator.isIntegerParsable(phaseText)) {
        throw new IOException("Phase format is incorrect, it should be integer");
      }

      CyclePhase phase = CyclePhase.getByIndex(Integer.parseInt(phaseText));

      String typeText = parts[5];

      if (!Validator.isIntegerParsable(typeText)) {
        throw new IOException("Type format is incorrect, it should be integer");
      }
      ResultType type = ResultType.getByIndex(Integer.parseInt(typeText));

      Result result = new Result(id, date, group, url, phase, type);
      results.add(result);
    }

    return results;
  }

  // ! unfinished
  public ArrayList<Project> loadProjects() throws IOException {
    List<String> lines = Files.readAllLines(dataPath.resolve("projects.csv"));

    if (lines.isEmpty()) {
      throw new IOException("File is empty");
    }

    if (!lines.get(0)
        .equals("id,name,beneficiaryCompany,keywords,description,statementURL,active,projectType,results,orgProject")) {
      throw new IOException("File format is incorrect");
    }

    ArrayList<Project> projects = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      String[] parts = line.split(",");
      if (parts.length < 7) {
        throw new IOException("File format is incorrect");
      }
      String id = parts[0];
      String name = parts[1];
      String benefCompany = parts[2];
      String keyWordsText = parts[3];
      String desc = parts[4];
      String statementURL = parts[5];
      String projectTypeText = parts[6];

      ProjectType projectType = ProjectType.getByIndex(Integer.parseInt(projectTypeText));

      ArrayList<String> keyWords = new ArrayList<>();
      for (String keyWord : keyWordsText.split(";")) {
        keyWords.add(keyWord);
      }

      Project project = new Project(id, name, benefCompany, keyWords, desc, statementURL, projectType);
      projects.add(project);
    }

    return projects;
  }
}
