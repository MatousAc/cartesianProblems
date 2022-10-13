package enums;
public enum VcAlg {
	BRUTE_FORCE,
	TWO_FACTOR_APPROXIMATION {
			@Override
			public VcAlg next() {
					return BRUTE_FORCE;
			};
	};

	public VcAlg next() {
		return TWO_FACTOR_APPROXIMATION;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace("_", " ");
	}
}