public enum speeds {
	PROMPT {
		@Override
		public speeds previous() {
				return PROMPT;
		};
	},
	SLOW,
	MEDIUM,
	FAST,
	UNRESTRAINED {
		@Override
			public speeds next() {
					return UNRESTRAINED;
		};
	};

	public speeds next() {
			return values()[ordinal() + 1];
	}
	public speeds previous() {
		return values()[ordinal() - 1];
	}
}