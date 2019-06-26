package ufac.br.cronos.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tutorial extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel p = new JPanel();
	ImageIcon imagfundo = new ImageIcon(getClass().getResource("tutorial.gif"));
	JLabel lbfundo = new JLabel(imagfundo);//DEFININDO ELA COMO FUNDO
	public Tutorial(){
		super("Tutorial");
		setSize(580,550);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		p.add(lbfundo);
		this.add(p);
		setLocationRelativeTo(null);
		this.setVisible(true);
		
	}

}
