public enum algs {
	JARVIS,
	GRAHAM {
			@Override
			public algs toggle() {
					return JARVIS;
			};
	};

	public algs toggle() {
		return GRAHAM;
	}
}