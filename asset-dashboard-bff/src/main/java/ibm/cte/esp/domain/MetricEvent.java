package ibm.cte.esp.domain;

import java.util.Date;

public class MetricEvent {
	protected String assetId;
	protected long rotation;
	protected long current;
	protected long pressure;
	protected long flowRate;
	protected long temperature;
	protected Date timeStamp;
	
	public MetricEvent() {
		this.timeStamp = new Date();
	}
	
	public String toString() {
		return "Id:" + getAssetId() + " C:" + getCurrent() + " P:" + getPressure() + " T:" + getTemperature() + "Flow:" + getFlowRate();
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public long getRotation() {
		return rotation;
	}

	public void setRotation(long rotation) {
		this.rotation = rotation;
	}

	public long getCurrent() {
		return current;
	}

	public void setCurrent(long current) {
		this.current = current;
	}

	public long getPressure() {
		return pressure;
	}

	public void setPressure(long pressure) {
		this.pressure = pressure;
	}

	public long getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(long flowRate) {
		this.flowRate = flowRate;
	}

	public long getTemperature() {
		return temperature;
	}

	public void setTemperature(long temperature) {
		this.temperature = temperature;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}
