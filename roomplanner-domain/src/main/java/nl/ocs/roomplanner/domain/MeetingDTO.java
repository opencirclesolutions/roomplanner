package nl.ocs.roomplanner.domain;

import java.util.Date;

import com.ocs.dynamo.domain.AbstractEntity;

public class MeetingDTO extends AbstractEntity<Integer> {

	private static final long serialVersionUID = 2458497365410294535L;

	private Date from;

	private Date to;

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub

	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

}
