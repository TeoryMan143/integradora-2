package model;

public enum DocumentType {
	CEDULA("cédula"),
	EXTCEDULA("cédula de extranjería"),
	PASSPORT("pasaporte");

	private String text;

	DocumentType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	/**
	 * 
	 * @param index
	 */
	public static DocumentType getByIndex(int index) {
		DocumentType[] values = DocumentType.values();
		return index >= 0 && index < values.length ? values[index] : DocumentType.CEDULA;
	}
}