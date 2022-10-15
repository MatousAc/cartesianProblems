package enums;
public enum VcAlg {
	EXACT_EXHAUSTIVE,
	EXACT_INCREASING_SIZE,
	APPROXIMATION_TWO_FACTOR,
	APPROXIMATION_ONE_BY_ONE {
			@Override
			public VcAlg next() {
					return EXACT_EXHAUSTIVE;
			};
	};

	public VcAlg next() {
		return values()[ordinal() + 1];
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace("_", " ");
	}
}