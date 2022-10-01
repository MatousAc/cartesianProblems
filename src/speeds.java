public enum speeds {
	PROMPT {
		@Override
		public speeds decrease() {
				return PROMPT;
		};
	},
	SLOTH,
	SLOW,
	MEDIUM,
	FAST,
	LIGHTNING,
	UNRESTRAINED {
		@Override
			public speeds increase() {
					return UNRESTRAINED;
		};
	};

	public speeds increase() {
			return values()[ordinal() + 1];
	}
	public speeds decrease() {
		return values()[ordinal() - 1];
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}