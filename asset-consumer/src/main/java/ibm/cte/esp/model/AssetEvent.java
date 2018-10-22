package ibm.cte.esp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class AssetEvent implements Serializable {
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
	protected BigDecimal rotation;
	protected BigDecimal current;
	protected BigDecimal pressure;
	protected BigDecimal flowRate;
	protected BigDecimal temperature;
	protected BigDecimal riskRating = new BigDecimal(-1);
	protected String latitude;
	protected String longitude;
	protected Date creationDate;
	
	public AssetEvent() {
		this.creationDate = new Date();
	}
	
	public String toString() {
		return getId() + " " + getIpAddress() + " C:" + getCurrent() + " P:" + getPressure() + " T:" + getTemperature();
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

	
	
	public Date getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getRotation() {
		return rotation;
	}

	public void setRotation(BigDecimal rotation) {
		this.rotation = rotation;
	}

	public BigDecimal getCurrent() {
		return current;
	}

	public void setCurrent(BigDecimal current) {
		this.current = current;
	}

	public BigDecimal getPressure() {
		return pressure;
	}

	public void setPressure(BigDecimal pressure) {
		this.pressure = pressure;
	}

	public BigDecimal getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(BigDecimal flowRate) {
		this.flowRate = flowRate;
	}

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public BigDecimal getRiskRating() {
		return riskRating;
	}

	public void setRiskRating(BigDecimal riskRating) {
		this.riskRating = riskRating;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
