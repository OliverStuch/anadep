package testutil.a;

import javax.swing.JFrame;

import testutil.c.C;
import testutil.c.CImpl;
import testutil.c.ext.CImplExtended;
import testutil.d.D;
import testutil.dimpl.DImpl;

public class AImpl implements A {
    private static CImplExtended implExtended;
    private JFrame jframe;

    public String operation() {
        C c = new CImpl();
        D d = new DImpl();

        return d.operationD();
    }

    public String toString() {
        return operation();
    }
}
