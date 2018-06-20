package ibm.caset.pot.assetmt.model;

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
  public int unsuccessfulLogin = 0;
  public String ipAddress;
  public String antivirus;
  
  public Asset() {}
  
    public Asset(String id,String os,String type) {
    	this.id = id;
    	this.os= os;
    	this.type = type;
    }
    
    public Asset(String id, String type, String os, String antivirus, String ipAddress, String unsuccessfulLogin, String version) {
        this.id = id;
        this.type = type;
        this.os = os;
        this.antivirus = antivirus;
        this.ipAddress = ipAddress;
        this.version = "unknown";
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
	public int getUnsuccessfulLogin() {
		return unsuccessfulLogin;
	}
	public void setUnsuccessfulLogin(int unsuccessfulLogin) {
		this.unsuccessfulLogin = unsuccessfulLogin;
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
}
