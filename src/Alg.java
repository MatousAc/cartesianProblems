public enum Alg {
	JARVIS,
	GRAHAM {
			@Override
			public Alg toggle() {
					return JARVIS;
			};
	};

	public Alg toggle() {
		return GRAHAM;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}