package model;

import java.time.LocalDate;
import java.time.ZoneId;

public class Result {

	private String id;
	private LocalDate date;
	private int group;
	private String url;
	private CyclePhase phase;
	private ResultType type;

	/**
	 * 
	 * @param date
	 * @param group
	 * @param url
	 * @param phase
	 * @param type
	 */
	public Result(String id, int group, String url, CyclePhase phace, ResultType type) {
		this.id = id;
		this.date = LocalDate.now(ZoneId.of("America/Bogota"));
		this.group = group;
		this.url = url;
		this.phase = phace;
		this.type = type;
	}

	public Result(String id, LocalDate date, int group, String url, CyclePhase phase, ResultType type) {
		this.id = id;
		this.date = date;
		this.group = group;
		this.url = url;
		this.phase = phase;
		this.type = type;
	}

	public String getId() {
		return this.id;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public int getGroup() {
		return this.group;
	}

	public String getUrl() {
		return this.url;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("=====================================\n");
		sb.append("ID: ").append(id).append("\n");
		sb.append("Fecha: ").append(date).append("\n");
		sb.append("Grupo: ").append(group).append("\n");
		sb.append("URL: ").append(url).append("\n");
		sb.append("Fase: ").append(phase).append("\n");
		sb.append("Tipo: ").append(type).append("\n");
		sb.append("=====================================\n");
		return sb.toString();
	}

}