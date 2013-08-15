package org.hancel.customclass;

import java.io.Serializable;
import java.util.Calendar;

public class TrackDate implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2476423033230441909L;


	private Calendar startDateTrack;
	private Calendar endDateTrack;
	private Calendar startTimeTrack;
	private Calendar endTimeTrack;
	
	public TrackDate() {
	}

	/**
	 * @return the startDateTrack
	 */
	public Calendar getStartDateTrack() {
		return startDateTrack;
	}

	/**
	 * @param startDateTrack the startDateTrack to set
	 */
	public void setStartDateTrack(Calendar startDateTrack) {
		this.startDateTrack = startDateTrack;
	}

	/**
	 * @return the endDateTrack
	 */
	public Calendar getEndDateTrack() {
		return endDateTrack;
	}

	/**
	 * @param endDateTrack the endDateTrack to set
	 */
	public void setEndDateTrack(Calendar endDateTrack) {
		this.endDateTrack = endDateTrack;
	}

	/**
	 * @return the startTimeTrack
	 */
	public Calendar getStartTimeTrack() {
		return startTimeTrack;
	}

	/**
	 * @param startTimeTrack the startTimeTrack to set
	 */
	public void setStartTimeTrack(Calendar startTimeTrack) {
		this.startTimeTrack = startTimeTrack;
	}

	/**
	 * @return the endTimeTrack
	 */
	public Calendar getEndTimeTrack() {
		return endTimeTrack;
	}

	/**
	 * @param endTimeTrack the endTimeTrack to set
	 */
	public void setEndTimeTrack(Calendar endTimeTrack) {
		this.endTimeTrack = endTimeTrack;
	}

}
