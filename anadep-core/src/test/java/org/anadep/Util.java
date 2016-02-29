package org.anadep;

import java.io.InputStream;

public class Util {
    static InputStream getResourceAsStream(String resourceName) {
	return Util.class.getResourceAsStream(resourceName);
    }
}
