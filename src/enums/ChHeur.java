package enums;
public enum ChHeur {
	NO_HEURISTIC,
	AKL_TOUSSAINT {
		@Override
		public ChHeur next() {
				return NO_HEURISTIC;
		};
	};

	public ChHeur next() {
		return values()[ordinal() + 1];
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace("_", " ");
	}
}