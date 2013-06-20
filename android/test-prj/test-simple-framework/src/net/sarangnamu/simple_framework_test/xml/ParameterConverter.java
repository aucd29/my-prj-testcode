
package net.sarangnamu.simple_framework_test.xml;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import android.util.Log;

public class ParameterConverter implements Converter<Parameter> {
    @Override
    public Parameter read(InputNode node) throws Exception {
        if (node.getAttribute("name").equals("t1")) {
            Log.e("test", "t1");

            return new T1Param();
        }

        Log.e("test", "t2");
        return new T2Param();
    }

    @Override
    public void write(OutputNode node, Parameter param) throws Exception {
        if (param.name.equals("t1")) {

        }
    }
}
