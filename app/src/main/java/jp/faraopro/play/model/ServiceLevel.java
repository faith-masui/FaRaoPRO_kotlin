package jp.faraopro.play.model;

public enum ServiceLevel {
	HLS {
		@Override
		public int getPermission() {
			return 0x0002;
		}

		@Override
		public boolean isContained(int target) {
			return (getPermission() & target) > 0;
		}
	},
	SEARCH {
		@Override
		public int getPermission() {
			return 0x0004;
		}

		@Override
		public boolean isContained(int target) {
			return (getPermission() & target) > 0;
		}

	};
	public abstract int getPermission();

	public abstract boolean isContained(int target);
}
