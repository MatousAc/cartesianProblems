package enums;
public enum VcAlg {
	BRUTE_FORCE,
	TWO_FACTOR_APPROXIMATION,
	REMOVE_ONE_BY_ONE {
			@Override
			public VcAlg next() {
					return BRUTE_FORCE;
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