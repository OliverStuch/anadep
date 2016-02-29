package org.anadep;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.Type;

public class NameConverter {
    public static final String REQ1 = "REQ1";
    private static final String CLASS_METHOD_SEPARATOR = ":";

    public static String extractAsmPackageFromAsmClassName(String asmClassName) {
        int positionOfLastSlash = asmClassName.lastIndexOf("/");
        return asmClassName.substring(0, positionOfLastSlash);
    }

    public static String extractShortClassNameFromAsmClassName(String asmClassName) {
        int positionOfLastSlash = asmClassName.lastIndexOf("/");
        return asmClassName.substring(positionOfLastSlash, asmClassName.length());
    }

    // @Requirement(description = "convert a jvm (asm) classname to human readable java-source file name. Array braces are dropped, so an int[] results in int", id = REQ1)
    /**
     * @param asmClassDescription
     *            is the description of a class name or attribute in jvm syntax
     */
    public static String asmClassDescription2className(String asmClassDescription) {
        // System.out.println("asm2className: " + asmClassName);
        String className;
        String asmClassNameAfterArray;
        boolean isArray;
        if (asmClassDescription.startsWith("[")) {
            isArray = true;
            asmClassNameAfterArray = asmClassDescription.substring(1);
        } else {
            asmClassNameAfterArray = asmClassDescription;
        }

        if (asmClassNameAfterArray.startsWith("L")) {
            // reference
            className = asmClassNameAfterArray.substring(1, asmClassNameAfterArray.length() - 1); // Strip 'L' and ';'
//            className = className.replaceAll("/", "\\.");
            className = className.replace('/', '.');
        } else { // primitive
            switch (asmClassNameAfterArray.charAt(0)) {
            case 'V':
                className = "void";
                break;
            case 'B':
                className = "byte";
                break;
            case 'C':
                className = "char";
                break;
            case 'S':
                className = "short";
                break;
            case 'I':
                className = "int";
                break;
            case 'J':
                className = "long";
                break;
            case 'F':
                className = "float";
                break;
            case 'D':
                className = "double";
                break;
            case 'Z':
                className = "boolean";
                break;
            case 'T':
                className = "T";
                break;
            default:
                if (asmClassNameAfterArray.length() == 1) {
                    throw new UnsupportedOperationException(NameConverter.class.getName() + " does not support type " + asmClassNameAfterArray.charAt(0));
                }
                className = asmClassNameAfterArray.replaceAll("/", "\\.");
            }
        }
        return className;
    }

    public static String asmMethodDescriptor2methodSignature(String asmDesc) {
        StringBuffer result = new StringBuffer();
        result.append("(");
        Type[] argumentTypes = Type.getArgumentTypes(asmDesc);
        for (int i = 0; i < argumentTypes.length; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append(argumentTypes[i].getClassName());
        }
        result.append("):");
        Type returnType = Type.getReturnType(asmDesc);
        result.append(returnType.getClassName());

        return result.toString();
    }

    // public static String NichtSinnvollNutzbar asmClassDescription2className(String asmClassName) {
    // String asmTypeString = Type.getType(asmClassName).getClassName();
    // }

    // public String className2asm(String className){

    public static String slash2dot(String in) {
//        return in.replaceAll("/", ".");
        return in.replace('/', '.');
    }

    public static String dot2slash(String in) {
        return in.replace('.', '/');
    }

    /**
     * gibt den reinen Typnamen zurück (ohne "[" und "L" und ";"), aber konvertiert nicht / -> . o.ä.
     */
    public static String pureAsmType(String asmClassName) {
        // System.out.println("asm2className: " + asmClassName);
        String className;
        String asmClassNameAfterArray = asmClassName.substring(0);
        boolean isArray;
        while (asmClassNameAfterArray.startsWith("[")) {
            isArray = true;
            asmClassNameAfterArray = asmClassNameAfterArray.substring(1);
        }

        if (asmClassNameAfterArray.startsWith("L")) {
            // reference
            className = asmClassNameAfterArray.substring(1, asmClassNameAfterArray.length() - 1); // Strip 'L' and ';'
            return className;
        } else {
            return asmClassNameAfterArray;
        }

    }

    public static String access2String(int access, List<Integer> asmAccList, Map<Integer, String> asmAcc2Name) {
        StringBuffer result = new StringBuffer();
        boolean first = true;
        for (Integer integer : asmAccList) {
            int bitSet = access & integer;
            if (bitSet > 0) {
                if (!first) {
                    result.append(" ");
                } else {
                    first = false;
                }
                result.append(asmAcc2Name.get(integer));
                access -= integer;
                if (access == 0) {
                    break;
                }
            }
        }

        return result.toString();
    }

    public static String asmMethod2fullQualifiedMethodName(String ownerName, String methodName, String desc) {
        return NameConverter.asmClassDescription2className(ownerName) + "." + methodName + NameConverter.asmMethodDescriptor2methodSignature(desc);
    }

    /*
     * int ACC_PUBLIC = 0x0001; // class, field, method int ACC_PRIVATE = 0x0002; // class, field, method int ACC_PROTECTED = 0x0004; // class, field, method int ACC_STATIC = 0x0008; //
     * field, method int ACC_FINAL = 0x0010; // class, field, method int ACC_SUPER = 0x0020; // class int ACC_SYNCHRONIZED = 0x0020; // method int ACC_VOLATILE = 0x0040; // field int
     * ACC_BRIDGE = 0x0040; // method int ACC_VARARGS = 0x0080; // method int ACC_TRANSIENT = 0x0080; // field int ACC_NATIVE = 0x0100; // method int ACC_INTERFACE = 0x0200; // class int
     * ACC_ABSTRACT = 0x0400; // class, method int ACC_STRICT = 0x0800; // method int ACC_SYNTHETIC = 0x1000; // class, field, method int ACC_ANNOTATION = 0x2000; // class int ACC_ENUM =
     * 0x4000; // class(?) field inner
     */
}
