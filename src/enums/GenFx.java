package enums;
public enum GenFx {
	RADIAL,
	RECTANGULAR,
	CIRCULAR {
		@Override
			public GenFx next() {
					return RADIAL;
		};
	};

public GenFx next() {
	return values()[ordinal() + 1];
}

@Override
public String toString() {
	return super.toString().toLowerCase();
}
}