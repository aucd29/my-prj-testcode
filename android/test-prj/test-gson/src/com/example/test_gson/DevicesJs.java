package com.example.test_gson;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Devices resource provides an interface to manage installed devices in Smart
 * Home. Devices resource has multiple Device resources. If a Smart Home Device
 * represent only itself, the device SHALL has a Device resource and &ldquo;
 * /device&rdquo; URI. In case of a Smart Home Gateway, it can represent other
 * devices simultaneously and have a &ldquo;Devices&rdquo; resource and &ldquo;
 * /devices&rdquo; URI.
 */
@JsonSerialize (include=JsonSerialize.Inclusion.NON_NULL)
@JsonRootName("Devices")
public class DevicesJs {
    @JsonProperty("Devices")
    public List<DeviceJs> Devices;  // burke
}