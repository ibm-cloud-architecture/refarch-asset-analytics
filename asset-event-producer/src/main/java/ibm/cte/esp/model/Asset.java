package ibm.cte.esp.model;

import java.io.Serializable;
import java.util.Date;


public class Asset implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String os;
	protected String version;
	protected String type;
	protected String ipAddress;
	protected String antivirus;
	protected long rotation;
	protected long current;
	protected long pressure;
	protected long flowRate;
	protected long temperature;
	protected double latitude;
	protected double longitude;
	protected Date timestamp;
	
	public Asset() {
		this.timestamp = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAntivirus() {
		return antivirus;
	}

	public void setAntivirus(String antivirus) {
		this.antivirus = antivirus;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
