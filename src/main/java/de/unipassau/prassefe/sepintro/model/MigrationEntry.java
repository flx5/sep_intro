package de.unipassau.prassefe.sepintro.model;

import java.time.LocalDateTime;

public class MigrationEntry {
	private int id;
	private long version;
	private LocalDateTime runAt;
	
	public MigrationEntry(long version) {
		this.version = version;
		
		setRunAt(LocalDateTime.now());
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the runAt
	 */
	public LocalDateTime getRunAt() {
		return runAt;
	}
	/**
	 * @param runAt the runAt to set
	 */
	public void setRunAt(LocalDateTime runAt) {
		// Nano seconds would be dropped by db anyways
		this.runAt = runAt.withNano(0);
	}
	/**
	 * @return the version
	 */
	public long getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(long version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((runAt == null) ? 0 : runAt.hashCode());
		result = prime * result + (int) (version ^ (version >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof MigrationEntry)) {
			return false;
		}
		
		MigrationEntry other = (MigrationEntry)obj;
		return version == other.version && runAt.equals(other.runAt);
	}

	@Override
	public String toString() {
		return String.format("MigrationEntry [id=%s, version=%s, runAt=%s]", id, version, runAt);
	}
}
