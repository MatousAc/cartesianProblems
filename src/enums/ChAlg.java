package enums;
public enum ChAlg {
	JARVIS_MARCH,
	GRAHAM_SCAN {
			@Override
			public ChAlg next() {
					return JARVIS_MARCH;
			};
	};

	public ChAlg next() {
		return GRAHAM_SCAN;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace("_", " ");
	}
}