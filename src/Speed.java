public enum Speed {
	PROMPT {
		@Override
		public Speed decrease() {
				return PROMPT;
		};
	},
	SLOTH,
	SLOW,
	MEDIUM,
	FAST,
	SUPER,
	LIGHTNING,
	UNRESTRAINED {
		@Override
			public Speed increase() {
					return UNRESTRAINED;
		};
	};

	public Speed increase() {
			return values()[ordinal() + 1];
	}
	public Speed decrease() {
		return values()[ordinal() - 1];
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}