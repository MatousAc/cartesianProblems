public enum distributions {
	RADIAL,
	RECTANGULAR{
		@Override
		public distributions toggle() {
				return RADIAL;
		};
};

public distributions toggle() {
	return RECTANGULAR;
}

@Override
public String toString() {
	return super.toString().toLowerCase();
}
}