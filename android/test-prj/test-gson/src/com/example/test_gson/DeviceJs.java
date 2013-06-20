package com.example.test_gson;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize (include=JsonSerialize.Inclusion.NON_NULL)
@JsonRootName("Device")
public class DeviceJs {
    @JsonUnwrapped
    public String type;

    @JsonUnwrapped
    public String time;

    @JsonUnwrapped
    public String connected;		// true, false

    @JsonUnwrapped
    public String name;

    @JsonProperty("resources")
    public List<String> resources;

    @JsonUnwrapped
    public String uuid;
}