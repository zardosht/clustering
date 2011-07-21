package org.clustering.mode;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AbstractMode {

	private final String modePrefix;
	private final String description;

	public AbstractMode(String modePrefix, String description) {
		this.modePrefix = modePrefix;
		this.description = description;

	}

	public boolean run(String[] args) throws Exception {
		Method execMethod = null;
		Object[] eArgs = null;

		try {
			execMethod = getModeExecMethod();
			if (execMethod == null)
				return false;

			// check modeprefix. e.g. "-cluster"
			if (modePrefix != null && !modePrefix.isEmpty()) {
				if (args.length == 0 || !modePrefix.equals(args[0])) {
					return false;
				}
				args = Arrays.copyOfRange(args, 1, args.length);
			}
			eArgs = new Object[execMethod.getParameterTypes().length];
			int i = 0;
			for (@SuppressWarnings("rawtypes") Class clazz : execMethod.getParameterTypes()) {
				if(i >= args.length) {
					return false;
				}
				
				if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
					eArgs[i] = Integer.parseInt(args[i]);
				} else if(Double.class.equals(clazz) || double.class.equals(clazz)) {
					eArgs[i] = Double.parseDouble(args[i]);
				} else if(String.class.equals(clazz)) {
					eArgs[i] = args[i];
				} else if(Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
					eArgs[i] = Boolean.parseBoolean(args[i]);
				} else {
					return false;
				}
				
				i++;
			}
		} catch (Exception e) {
			return false;
		}

		// invoke actual method
		execMethod.invoke(this, eArgs);
		return true;
	}

	private Method getModeExecMethod() {
		for (Method method : getClass().getMethods()) {
			if(method.getName().startsWith("_run")) {
				return method;
			}
			// WHY AREN'T THE F!&?%$§G ANNOTATIONS NOT RECOGNIZED
//			if (method.getAnnotation(ModeExec.class) != null) {
//				return method;
//			}
//			for(Annotation an : method.getAnnotations()) {
//				System.out.println(an);
//			}
		}
		return null;
	}


	public String getDescriptino() {
		return description;
	}
}
