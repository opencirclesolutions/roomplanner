package nl.ocs.roomplanner.domain.comparator;

import java.util.Comparator;

import nl.ocs.roomplanner.domain.Meeting;

/**
 * Comparator for ordering meetings - smaller meetings are easier to schedule
 * 
 * @author bas.rutten
 *
 */
public class MeetingSizeComparator implements Comparator<Meeting> {

	@Override
	public int compare(Meeting m1, Meeting m2) {
		return m2.getNumberOfAttendees() - m1.getNumberOfAttendees();
	}

}
