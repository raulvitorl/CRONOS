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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sun.org.apache.bcel.internal.generic.GOTO;

import ufac.br.cronos.logic.Escolhedor;

public class FramePrincipal extends JFrame implements ActionListener {

	public static void main(String[] args) {
		FramePrincipal fp = new FramePrincipal();
		fp.setVisible(true);
	}

	private static final long serialVersionUID = 1L;

	static String caminho, separador, inicio;

	Escolhedor Es = new Escolhedor();

	JButton btnBusca = new JButton();
	JButton btnConfirma = new JButton();
	JPanel pnlBusca, pnlMetricas, pnlIntervalo, pnlRepartir;
	JComboBox<String> listaMetricas = new JComboBox<String>();
	JComboBox<String> listaDeUnidades = new JComboBox<String>();
	JComboBox<String> listaDeDuracao = new JComboBox<String>();
	JComboBox<String> listaDeFormatos = new JComboBox<String>();

	ButtonGroup bgFiltros;

	private boolean informou;
	// Define e adiciona dois menus drop down na barra de menus
	JMenu InfoMenu = new JMenu("Informacoes");
	JMenu UsabilidadeMenu = new JMenu("Informacoes");
	// Cria uma barra de menu para o JFrame
	JMenuBar menuBar = new JMenuBar();
	// Cria e adiciona um item simples para o menu
	JMenuItem SobreAction = new JMenuItem("Sobre");
	JMenuItem TutorialAction = new JMenuItem("Tutorial");

	public FramePrincipal() {
		super("Particionador de Bases de Dados");
		this.setBackground(new Color(245));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 550);
		this.setResizable(false);
		// Adiciona a barra de menu ao  frame
		setJMenuBar(menuBar);
		menuBar.add(InfoMenu);
		InfoMenu.add(SobreAction);
		InfoMenu.addSeparator();
		InfoMenu.add(TutorialAction);
		getContentPane().setLayout(new GridLayout(7, 1));
		pnlBusca = new JPanel();
		btnBusca.setText("Buscar Base");
		btnConfirma.setText("Confirmar Divisao");
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
		listaDeUnidades.addItem("Selecione...");
		listaDeUnidades.addItem("Mes(es)");
		listaDeUnidades.addItem("Ano(s)");
		pnlIntervalo.add(new JLabel("Duracao : "));
		pnlIntervalo.add(listaDeDuracao);
		pnlIntervalo.add(new JLabel("Formatos de Data aceitos : "));
		pnlIntervalo.add(listaDeFormatos);

		getContentPane().add(pnlBusca);
		add(new JLabel("                                       Selecione um atributo temporal do tipo DATA"));
		getContentPane().add(pnlMetricas);
		getContentPane().add(
				new JLabel("                                                  " + "                       Intervalo"));

		getContentPane().add(pnlIntervalo);
		getContentPane().add(pnlRepartir);
		listaDeDuracao.setEnabled(false);
		listaDeFormatos.setEnabled(false);
		listaDeUnidades.setEnabled(false);
		btnConfirma.setEnabled(false);
		listaMetricas.setEnabled(false);
		btnBusca.addActionListener(this);
		btnConfirma.addActionListener(this);
		listaDeUnidades.addActionListener(this);
		listaMetricas.addActionListener(this);
		SobreAction.addActionListener(this);
		TutorialAction.addActionListener(this);
		setLocationRelativeTo(null);
		setVisible(true);

	}
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==SobreAction) {
			JOptionPane.showMessageDialog(null, ""
					+ "Aplicacao desenvolvida para a disciplina de Engenharia de Software \n"
					+ "                   Equipe : Cleyciane Frias, Juliana Abreu, Raul Vitor \n"
					+ "                                      Universidade Federal do Acre \n"
					+ "                                                      2018 - 2019","Sobre",JOptionPane.PLAIN_MESSAGE);
		}

		if(e.getSource()==TutorialAction){
			Tutorial t = new Tutorial();
		}



		if (e.getSource() == btnBusca) {
			Es.DefineIntervalo(0, listaDeDuracao);
			Es.populaFormatos(listaDeFormatos);
			caminho = Es.EscolheArquivo();
			if (caminho != "") {			
				informou = false;

				while(informou == false){
					inicio = JOptionPane.showInputDialog(null, "Em que linha esta localizado o cabecalho do arquivo .csv?");
					if(inicio==null){
						JOptionPane.showMessageDialog(null, "Tente Novamente","Metrica Necessaria Para a Operacao",JOptionPane.ERROR_MESSAGE);
					}else{
						informou=true;
					}
				}
				if(inicio.equals("")){
					inicio="1";
				}

				informou = false;

				while(informou == false){
					separador = JOptionPane.showInputDialog(null, "Qual o simbolo separador do arquivo?");
					if(separador==null){
						JOptionPane.showMessageDialog(null, "Tente Novamente","Metrica Necessaria Para a Operacao",JOptionPane.ERROR_MESSAGE);
					}else{
						informou=true;
					}
				}
				if (separador.isEmpty()) {
					separador = ",";
				}				



				boolean success = Es.PreencheMetricas(listaMetricas, separador, caminho, Integer.parseInt(inicio));
				if (!success) {
					JOptionPane.showMessageDialog(null, "Falha na captura do arquivo");
				}
				if (success) {
					listaMetricas.setEnabled(true);
					listaDeUnidades.setEnabled(true);
					listaDeDuracao.setEnabled(true);
					listaDeFormatos.setEnabled(true);
					btnConfirma.setEnabled(true);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Selecione um arquivo!");
			}
		}
		if (e.getSource() == btnConfirma) {
			String campos = "";
			int invalido=0;

			if(listaDeUnidades.getSelectedIndex()<1){
				campos = "\nUnidade\n";
				invalido=1;
			}

			if(listaDeFormatos.getSelectedIndex()<1){
				campos += "Formato";
				invalido=1;
			}

			if(invalido==1){
				JOptionPane.showMessageDialog(null, "Preencha os campos :"+campos, "Erro na parametrizacao", JOptionPane.ERROR_MESSAGE);

			}

			if (invalido!=1) {
				String caminhoFinal = Es.EscolheArquivo();

				if (!caminhoFinal.equals("")) {
					File f = Es.geraArquivo(caminho);
					String d = null;
					try {
						d = Es.pegaPrimeiraData(f, listaMetricas.getSelectedIndex(), separador, Integer.parseInt(inicio));
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Voce precisa confirmar a operacao","Falha no particionamento",JOptionPane.ERROR_MESSAGE);
					}


					String d2 = Es.pegaUltimaData(f, listaMetricas.getSelectedIndex(), separador, Integer.parseInt(inicio));

					try {

						if (Es.Particiona(f, d, Integer.parseInt(listaDeDuracao.getSelectedItem().toString()),
								listaMetricas.getSelectedIndex(), caminhoFinal, listaDeFormatos.getSelectedItem().toString(),
								separador, listaDeUnidades.getSelectedIndex(), Integer.parseInt(inicio), d2)) {

							JOptionPane.showMessageDialog(null, "Processamento concluido","Operacao bem sucedida",JOptionPane.PLAIN_MESSAGE);
							int i = JOptionPane.showConfirmDialog(null, "Deseja Realizar Outra Operação?", "Opções", JOptionPane.YES_NO_OPTION);
							if(i == JOptionPane.YES_OPTION) {

							}
							else if(i == JOptionPane.NO_OPTION) {
								JOptionPane.showMessageDialog(null, "Obrigado por utilizar o Cronos!", "Agradecimento", JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}

						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}


			}

		}
		if (e.getSource() == listaDeUnidades) {
			Es.DefineIntervalo(listaDeUnidades.getSelectedIndex(), listaDeDuracao);
		}
	}

}
