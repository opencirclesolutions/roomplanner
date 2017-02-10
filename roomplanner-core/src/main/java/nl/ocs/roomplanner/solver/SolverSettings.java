package nl.ocs.roomplanner.solver;

public class SolverSettings {

	private boolean checkEmployeeDoubleBooking = false;

	private boolean optimizeCosts = false;
	
	private boolean optimizeCapacity = false;
	
	public boolean isCheckEmployeeDoubleBooking() {
		return checkEmployeeDoubleBooking;
	}

	public void setCheckEmployeeDoubleBooking(boolean checkEmployeeDoubleBooking) {
		this.checkEmployeeDoubleBooking = checkEmployeeDoubleBooking;
	}

	public boolean isOptimizeCosts() {
		return optimizeCosts;
	}

	public void setOptimizeCosts(boolean optimizeCosts) {
		this.optimizeCosts = optimizeCosts;
	}

	public boolean isOptimizeCapacity() {
		return optimizeCapacity;
	}

	public void setOptimizeCapacity(boolean optimizeCapacity) {
		this.optimizeCapacity = optimizeCapacity;
	}
	
	
}
