
package net.sarangnamu.simple_framework_test.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "parameter")
public class Parameter {
    @Attribute(required = true)
    public String name;
}
