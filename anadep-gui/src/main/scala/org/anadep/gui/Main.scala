package org.anadep.gui

object Main {
    def main(args:String[Array]) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new JungGraphApplet());
        f.pack();
        f.setVisible(true);
    }
}
