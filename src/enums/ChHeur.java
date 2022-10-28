package enums;
public enum ChHeur {
	NONE,
	AKL_TOUSSAINT {
			@Override
			public ChHeur next() {
					return NONE;
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