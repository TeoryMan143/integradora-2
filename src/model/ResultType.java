package model;

public enum ResultType {
	CLASS_DIAGRAM("diagrama de clases"),
	INFOGRAFY("infografía"),
	DATA_MODEL("modelo de datos"),
	TESTING_PLAN("plan de pruebas"),
	DEPLOYMENT_DIAGRAM("diagrama de despliegue"),;

	private String text;

	ResultType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public static ResultType getByIndex(int index) {
		ResultType[] values = ResultType.values();
		return index >= 0 && index < values.length ? values[index] : ResultType.CLASS_DIAGRAM;
	}
}