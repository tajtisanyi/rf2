package codemetropolis.toolchain.commons.cmxml;

public class Attribute {
	
	private String name;
	private String value;
	/**
	 * Stores the original value of the different property
	 */
	private String original;

	public Attribute(String name, String value, String original) {
		this.name = name;
		this.value = value;
		this.original = original;
	}

	/**
	 * Getter for the original value
	 *
	 * @return String
	 */
	public String getOriginal() {
		return original;
	}

	/**
	 * Setter for the original value
	 *
	 * @param original the new value of the original
	 */
	public void setOriginal(String original) {
		this.original = original;
	}

	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
