package model;

public enum ProjectType {
	INTEGRATED("integrador"),
	COURSE("de curso"),
	FINAL("final");

	private String text;

	ProjectType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public static ProjectType getByIndex(int index) {
		ProjectType[] values = ProjectType.values();
		return index >= 0 && index < values.length ? values[index] : ProjectType.COURSE;
	}
}