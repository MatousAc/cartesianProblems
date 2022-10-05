public enum Problem {
	CONVEX_HULL,
	VERTEX_COVER {
		@Override
			public Problem next() {
					return CONVEX_HULL;
		};
	};

	public Problem next() {
		return values()[ordinal() + 1];
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace("_", " ");
	}
}
