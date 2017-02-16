package sep_intro.model.migrations;

import java.time.LocalDate;

public class MigrationEntry {
	private int id;
	private long version;
	private LocalDate runAt;
	
	public MigrationEntry() {
		
	}
	
	public MigrationEntry(long version) {
		runAt = LocalDate.now();
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
	public LocalDate getRunAt() {
		return runAt;
	}
	/**
	 * @param runAt the runAt to set
	 */
	public void setRunAt(LocalDate runAt) {
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
