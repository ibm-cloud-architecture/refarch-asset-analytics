package ibm.cte.esp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("assets")
public class AssetEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 @PrimaryKey
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
	protected Double latitude;
	protected Double longitude;
	protected Date creationDate;
	  
	public AssetEvent() {}
	
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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AssetEvent ) {
			AssetEvent oa = (AssetEvent) o;
			return (oa.latitude == this.latitude) && (oa.longitude == this.longitude);
		}
		return false;
	}
	

	public Date getCreationDate() {
		return creationDate;
	}



	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
