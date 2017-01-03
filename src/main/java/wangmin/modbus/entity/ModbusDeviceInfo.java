package wangmin.modbus.entity;

public class ModbusDeviceInfo {
	private Integer id;
    private String name;
    private ModbusDeviceProtocolInfo protocolInfo;


	@Override
	public String toString() {
		return "id="+id
				+",name="+name
				+",protocolInfo="+protocolInfo;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModbusDeviceInfo) {
			return this.id != null && ((ModbusDeviceInfo) obj).id != null && ((ModbusDeviceInfo) obj).id == this.id;
		}
		return false;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ModbusDeviceProtocolInfo getProtocolInfo() {
		return protocolInfo;
	}

	public void setProtocolInfo(ModbusDeviceProtocolInfo protocolInfo) {
		this.protocolInfo = protocolInfo;
	}
}
