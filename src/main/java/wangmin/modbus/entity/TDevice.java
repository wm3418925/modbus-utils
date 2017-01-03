package wangmin.modbus.entity;

public class TDevice {
	private Integer id;
    private String name;
    private String protocolInfo;


	@Override
	public String toString() {
		return "id="+id
				+",name="+name
				+",protocolInfo="+protocolInfo;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TDevice) {
			return this.id != null && ((TDevice) obj).id != null && ((TDevice) obj).id == this.id;
		}
		return false;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProtocolInfo() {
		return protocolInfo;
	}

	public void setProtocolInfo(String protocolInfo) {
		this.protocolInfo = protocolInfo;
	}
}
