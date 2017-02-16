package sep_intro.model.migrations;

import java.time.LocalDateTime;

public class MigrationEntry {
	private int id;
	private long version;
	private LocalDateTime runAt;
	
	public MigrationEntry() {
		
	}
	
	public MigrationEntry(long version) {
		this.version = version;
		runAt = LocalDateTime.now();
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
		this.runAt = runAt;
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
	
	
}
