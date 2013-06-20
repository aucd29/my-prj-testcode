package net.sarangnamu.simple_framework_test.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name="Notification")
public class Notification {
    @Element
    public String subscribedResource;

    @Element
    public String event;

    @Element
    public Resource Resource;

    @Element
    public String subscriptionURI;
}
