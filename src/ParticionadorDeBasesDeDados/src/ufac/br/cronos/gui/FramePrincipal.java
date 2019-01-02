package ufac.br.cronos.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import ufac.br.cronos.logic.Escolhedor;

public class FramePrincipal extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;

	static  String caminho,separador,inicio;

	Escolhedor Es = new Escolhedor();

	JButton btnBusca = new JButton();
	JButton btnConfirma = new JButton();
	JPanel pnlBusca,pnlMetricas,pnlIntervalo,pnlRepartir;
	JComboBox<String> listaMetricas = new JComboBox<>();	

	//Vetores para armazenar as unidades e intervalos
	JComboBox<String> listaDeUnidades = new JComboBox<String>();

	JComboBox<String> listaDeDuracao = new JComboBox<String>();
	JComboBox<String> listaDeFormatos = new JComboBox<String>();

	ButtonGroup bgFiltros;



	public FramePrincipal(){
		super("Particionador de Bases de Dados");
		this.setBackground(new Color(245));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 550);
		this.setResizable(false);
		getContentPane().setLayout(new GridLayout(7, 1));
		pnlBusca = new JPanel();
		btnBusca.setText("Buscar Base");
		btnConfirma.setText("Confirmar Divisão");
		pnlBusca.add(btnBusca);
		pnlMetricas = new JPanel();
		pnlRepartir = new JPanel();
		pnlRepartir.setLayout(new GridLayout(1, 3));
		pnlRepartir.add(new JLabel(""));
		pnlRepartir.add(btnConfirma);
		pnlRepartir.add(new JLabel(""));
		pnlMetricas.add(listaMetricas);
		pnlIntervalo = new JPanel();
		pnlIntervalo.setLayout(new GridLayout(4, 2));

		pnlIntervalo.add(new JLabel("Unidade : "));
		pnlIntervalo.add(listaDeUnidades);
		listaDeUnidades.addItem("Dia(s)");
		listaDeUnidades.addItem("Mês(es)");
		listaDeUnidades.addItem("Ano(s)");
		pnlIntervalo.add(new JLabel("Duracao : "));		
		pnlIntervalo.add(listaDeDuracao);
		pnlIntervalo.add(new JLabel("Formatos de Data aceitos : "));	
		pnlIntervalo.add(listaDeFormatos);

		getContentPane().add(pnlBusca);
		getContentPane().add(pnlMetricas);
		getContentPane().add(new JLabel("                                                  "
				+ "                       Intervalo"));

		getContentPane().add(pnlIntervalo);
		getContentPane().add(pnlRepartir);
		listaDeDuracao.setEnabled(false);
		listaDeFormatos.setEnabled(false);
		listaDeUnidades.setEnabled(false);
		btnBusca.addActionListener(this);
		btnConfirma.addActionListener(this);
		listaDeUnidades.addActionListener(this);
		listaMetricas.addActionListener(this);
		setLocationRelativeTo(null);
		setVisible(true); 

	}


	public static void main(String[] args) {
		FramePrincipal fp = new FramePrincipal();
		fp.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnBusca){
			Es.DefineIntervalo(0, listaDeDuracao);
			Es.populaFormatos(listaDeFormatos);
			caminho = Es.EscolheArquivo();
			if(caminho!="") {
				inicio = JOptionPane.showInputDialog(null,"Em que linha iniciam os dados do arquivo?");
				
				if(inicio.isEmpty()){
					inicio="1";
				}
				
				separador = JOptionPane.showInputDialog(null,"Qual o simbolo separador do arquivo?");
				
				if(separador.isEmpty()){
					separador=",";
				}
				
				boolean success = Es.PreencheMetricas(listaMetricas, separador, caminho, Integer.parseInt(inicio));
				if(!success){JOptionPane.showMessageDialog(null,"Falha na captura do arquivo");}
				if(success){
					listaDeUnidades.setEnabled(true);
					listaDeDuracao.setEnabled(true);
					listaDeFormatos.setEnabled(true);
				}
			}else{
				JOptionPane.showMessageDialog(null,"Selecione um arquivo!");
			}
		}
		if(e.getSource()==btnConfirma){
			String caminhoFinal = Es.EscolheArquivo();
			File f = Es.geraArquivo(caminho);
	
			String d = Es.pegaPrimeiraData(f, listaMetricas.getSelectedIndex(), separador,Integer.parseInt(inicio));

			try {
				Es.Particiona(f, d, Integer.parseInt(listaDeDuracao.getSelectedItem().toString()), listaMetricas.getSelectedIndex(), caminhoFinal, "YYYY-MM-DD", separador, listaDeUnidades.getSelectedIndex(),Integer.parseInt(inicio));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource()==listaDeUnidades){
			Es.DefineIntervalo(listaDeUnidades.getSelectedIndex(), listaDeDuracao);
		}
	}


}
