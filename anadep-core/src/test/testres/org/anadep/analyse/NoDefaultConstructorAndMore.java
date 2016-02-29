package org.anadep.analyse;

public class NoDefaultConstructorAndMore {
    public NoDefaultConstructorAndMore(String arg){ // ArgumentTypeDependency NoDefaultConstructorAndMore -> String
	this.arg = arg; //  LocalVariableDependency: <init> -> NoDefaultConstructorAndMore !
    }
    private String arg;
}
