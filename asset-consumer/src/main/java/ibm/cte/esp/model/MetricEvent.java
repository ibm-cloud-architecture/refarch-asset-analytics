package ibm.cte.esp.model;

import java.util.Date;

public class MetricEvent {
	protected String id;
	protected int rotation;
	protected double current;
	protected int pressure;
	protected int flowRate;
	protected int temperature;
	protected Date timeStamp;

	
	public MetricEvent() {
		this.timeStamp = new Date();
	}
	
	public String toString() {
		return "Id:" + getId() + " C:" + getCurrent() + " P:" + getPressure() + " T:" + getTemperature() + "Flow:" + getFlowRate();
	}

	public String getId() {
		return id;
	}

	public void setId(String assetId) {
		this.id = assetId;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(int flowRate) {
		this.flowRate = flowRate;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}
