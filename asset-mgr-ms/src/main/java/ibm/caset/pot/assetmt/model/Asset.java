package ibm.caset.pot.assetmt.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
@Table("assets")
public class Asset {
  @PrimaryKey
  public long id;
  public String os;
  public String type;
  public int unsuccessfulLogin = 0;
  public String ipAddress;
  public String antivirus;
  
    public Asset(long id,String os,String type) {
    	this.id = id;
    	this.os= os;
    	this.type = type;
    }
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
}
