package ufac.br.cronos.gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadingFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	JPanel p = new JPanel();
	ImageIcon imagfundo = new ImageIcon(getClass().getResource("carregamento.gif"));
	JLabel lbfundo = new JLabel(imagfundo);//DEFININDO ELA COMO FUNDO
	public LoadingFrame(){
		super("Carregando...");
		this.setBackground(new Color(245));
		setSize(250, 250);
		p.add(lbfundo);
		this.add(p);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);	
	}	


}
