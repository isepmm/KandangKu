package com.example.isepmm.kandangku;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Isepmm on 17/05/2018.
 */

public class Device {
    private String deviceName;
    private String deviceId;
    private boolean status;

    public Device(){}

    public Device(String deviceName, boolean status) {
        this.deviceName = deviceName;
        this.status = status;
    }

    public Device(Device device,String id){
        this.deviceName = device.getDeviceName();
        this.status = device.isStatus();
        this.deviceId = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public boolean isStatus() {return status;}
}
