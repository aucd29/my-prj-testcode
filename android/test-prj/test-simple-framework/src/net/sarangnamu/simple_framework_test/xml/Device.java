package net.sarangnamu.simple_framework_test.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;


@Root(name = "Device", strict = false)
public class Device {
    @Attribute(required = false)
    public String version;

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

    @Element
    @Convert(ParameterConverter.class)
    public Parameter parameter;

    //@ElementList(name="parameters", required = false)
    //public ArrayList<Parameter> parameters = new ArrayList<Parameter>();
}
