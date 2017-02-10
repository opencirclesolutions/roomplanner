package nl.ocs.roomplanner.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.ocs.roomplanner.domain.Employee;
import nl.ocs.roomplanner.domain.Meeting;
import nl.ocs.roomplanner.domain.Room;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

@PlanningSolution
public class RoomplanningSolution implements Solution<HardMediumSoftScore> {

	private List<Meeting> meetings;

	private List<Room> rooms;

	private List<Employee> employees;

	private SolverSettings solverSettings = new SolverSettings();
	
	private HardMediumSoftScore score;

	public RoomplanningSolution() {
	}

	public RoomplanningSolution(List<Room> rooms, List<Meeting> meetings, List<Employee> employees) {
		this.rooms = rooms;
		this.meetings = meetings;
		this.employees = employees;
	}

	@Override
	public Collection<? extends Object> getProblemFacts() {
		List<Object> facts = new ArrayList<Object>();
		facts.addAll(rooms);
		facts.addAll(employees);
		facts.add(solverSettings);
		
		return facts;
	}

	@Override
	public HardMediumSoftScore getScore() {
		return score;
	}

	@Override
	public void setScore(HardMediumSoftScore score) {
		this.score = score;
	}

	@PlanningEntityCollectionProperty
	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}

	@ValueRangeProvider(id = "roomRange")
	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public boolean isCheckEmployeeDoubleBooking() {
		return solverSettings.isCheckEmployeeDoubleBooking();
	}

	public void setCheckEmployeeDoubleBooking(boolean checkEmployeeDoubleBooking) {
		solverSettings.setCheckEmployeeDoubleBooking(checkEmployeeDoubleBooking);
	}

	public SolverSettings getSolverSettings() {
		return solverSettings;
	}

	public void setSolverSettings(SolverSettings solverSettings) {
		this.solverSettings = solverSettings;
	}

	
}
