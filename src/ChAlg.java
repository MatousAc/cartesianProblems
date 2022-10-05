public enum ChAlg {
	JARVIS_MARCH,
	GRAHAM_SCAN {
			@Override
			public ChAlg toggle() {
					return JARVIS_MARCH;
			};
	};

	public ChAlg toggle() {
		return GRAHAM_SCAN;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace("_", " ");
	}
}