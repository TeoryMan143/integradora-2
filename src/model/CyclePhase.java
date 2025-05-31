package model;

public enum CyclePhase {
	REQ_ANALYSIS("análisis de requerimientos"),
	DESING("diseño"),
	BUILD("construcción"),
	TEST("pruebas"),
	DEPLOYMENT("despliegue");

	private String text;

	CyclePhase(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public static CyclePhase getByIndex(int index) {
		CyclePhase[] values = CyclePhase.values();
		return index >= 0 && index < values.length ? values[index] : CyclePhase.REQ_ANALYSIS;
	}
}