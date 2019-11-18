package com.ajparedes.service;

import com.ajparedes.model.Device;

public interface IDeviceService {

	public abstract void checkDevice(String idDevice, String username) throws Exception;

	public abstract Device isRegistered(String idDevice);

}
