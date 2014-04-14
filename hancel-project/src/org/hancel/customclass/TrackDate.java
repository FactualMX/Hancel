package org.hancel.customclass;
/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
Created by Javier Mejia @zenyagami
zenyagami@gmail.com
	*/
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
