package net.sarangnamu.simple_framework_test.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "Device", strict = false)
public class Device {
    @Element(required = false)
    public String type;
    @Element(required = false)
    public String time;
    @Element(name = "ID")
    public String ID;
    @Element(required = false)
    public String description;
    @Element(required = false)
    public String ipAddress;
}
