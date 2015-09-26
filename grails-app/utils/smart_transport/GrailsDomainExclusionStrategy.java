package smart_transport;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * This class is used because GSON seems to hate serializing the Grails errors
 * class and gets into an infinite loop. See:
 * https://groups.google.com/d/topic/google-gson/AX48GpVHjgQ/discussion
 *
 * @author bbonner
 *
 */
public class GrailsDomainExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        if (f.getName().equals("errors")) {
            return true;
        }
        return false;
    }

}