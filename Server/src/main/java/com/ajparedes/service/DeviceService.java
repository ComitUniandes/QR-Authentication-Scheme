package com.ajparedes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajparedes.data.IDeviceRepository;
import com.ajparedes.model.Device;

@Service
public class DeviceService implements IDeviceService {

	@Autowired
	private IDeviceRepository repo;
	
	@Override
	public void checkDevice(String idDevice, String username) throws Exception {
		//  verifica si isRegistered y si no lo esta addDevice
		Device dev = isRegistered(idDevice);
		if (dev == null) {
			Device d = new Device();
			d.setId(idDevice);
			d.setUsername(username);
			addDevice(d);
		}
		else if (!dev.getUsername().equals(username))
			throw new Exception("The user logged dosn't match with the user associated to this device.\nTo continue with this user, first log in with the linked account and unlink the device.");
	}
	@Override
	public Device isRegistered(String idDevice){
		Optional<Device> dev = repo.findById(idDevice);
		return (dev.isPresent()) ? dev.get() : null;
	}
	
	public void addDevice(Device dev) {
		repo.save(dev);
	}

}
