package ufac.br.cronos.gui;

import javax.swing.JFrame;

public class LoadingFrame extends JFrame{
private static final long serialVersionUID = 1L;

	public LoadingFrame(){
		super("Processando...");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(272,50);
		setLocationRelativeTo(null);
	}

}
