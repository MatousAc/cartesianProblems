package enums;
public enum Mode { 
	VISUAL, 
	AUTO;

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}