
package net.sarangnamu.simple_framework_test.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Resource")
public class Resource {
    @Attribute
    public String type;

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TYPE Operation
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Element(required=false)
    public String power;

    @Element(required=false)
    public String state;

    @Element(required=false)
    public String remainingTime;

    @Element(required=false)
    public String progressPercentage;

    @Element(required=false)
    public String progress;

    @Element(required=false)
    public String supportedProgress;

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TYPE Chore
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Element(required=false)
    public String id;

    @Element(required=false)
    public String description;

    @Element(required=false)
    public String owner;

    @Element(required=false)
    public String startTime;

    @Element(required=false)
    public String event;


    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TYPE OverUsage
    //
    ////////////////////////////////////////////////////////////////////////////////////

    //public int id;

    @Element(required=false)
    public String deviceID;

    @Element(required=false)
    public String deviceName;

    @Element(required=false)
    public String code;

    //public String description;

    @Element(required=false)
    public String cause;

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TYPE Schedule
    //
    ////////////////////////////////////////////////////////////////////////////////////

    //public int id;
    //public String description;
    //public String state;
    //public long startTime;

    @Element(required=false)
    public String repeatPeriod;
}
