package ibm.cte.pot.assetmgr.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("assets")
public class Asset {
  @PrimaryKey
  public String id;
  @NotBlank
  @Size(max = 15)
  public String os;
  public String version;
  public String type;
  public String ipAddress;
  public String antivirus;
  protected long rotation;
  protected long current;
  protected long pressure;
  protected long flowRate;
  protected long temperature;
  protected double latitude;
  protected double longitude;
  protected Date creationDate;

  public Asset() {}

  public Asset(String id,String os,String type) {
  	this.id = id;
  	this.os= os;
  	this.type = type;
    this.creationDate = new Date();
  }

  public Asset(String id, String type, String os, String antivirus, String ipAddress, String unsuccessfulLogin, String version) {
      this.id = id;
      this.type = type;
      this.os = os;
      this.antivirus = antivirus;
      this.ipAddress = ipAddress;
      this.version = "unknown";
      this.creationDate = new Date();
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof Asset ) {
			Asset oa = (Asset) o;
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
