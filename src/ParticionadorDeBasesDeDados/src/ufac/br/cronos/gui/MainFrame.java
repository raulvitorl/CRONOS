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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ufac.br.cronos.logic.Picker;

public class MainFrame extends JFrame implements ActionListener {

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainFrame fp = new MainFrame();
	}

	private static final long serialVersionUID = 1L;

	static String caminho, separador, inicio;

	Picker Es = new Picker();

	JButton btnBusca = new JButton();
	JButton btnConfirma = new JButton();
	JPanel pnlBusca, pnlMetricas, pnlIntervalo, pnlRepartir;
	JComboBox<String> listaMetricas = new JComboBox<String>();
	JComboBox<String> listaDeUnidades = new JComboBox<String>();
	JComboBox<String> listaDeDuracao = new JComboBox<String>();
	JComboBox<String> tamanhoIntervalo = new JComboBox<String>();
	JComboBox<String> listaDeFormatos = new JComboBox<String>();

	ButtonGroup bgFiltros;

	private boolean informou;
	// Define e adiciona dois menus drop down na barra de menus
	JMenu InfoMenu = new JMenu("Info");
	JMenu UsabilidadeMenu = new JMenu("Info");
	// Cria uma barra de menu para o JFrame
	JMenuBar menuBar = new JMenuBar();
	// Cria e adiciona um item simples para o menu
	JMenuItem SobreAction = new JMenuItem("About");

	public MainFrame() {
		JFrame frame = new JFrame();
		frame.setTitle("Database Partitioner");
		frame.setBackground(new Color(245));
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(500, 550);
		frame.setResizable(false);

		JComboBox<String> cmbDemo = new JComboBox<String>();
		cmbDemo.addItem("One");
		cmbDemo.addItem("Two");
		cmbDemo.addItem("Three");

		// Adiciona a barra de menu ao frame
		frame.setJMenuBar(menuBar);

		JMenu mnuLaf = new JMenu("Look and feel");
		JRadioButtonMenuItem mniNimbus = new JRadioButtonMenuItem("Nimbus");
		JRadioButtonMenuItem mniMetal = new JRadioButtonMenuItem("Metal");
		JRadioButtonMenuItem mniSystem = new JRadioButtonMenuItem("Systems");

		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(mniNimbus);
		btnGroup.add(mniMetal);
		btnGroup.add(mniSystem);
		menuBar.add(mnuLaf);

		mnuLaf.add(mniNimbus);
		mnuLaf.add(mniMetal);
		mnuLaf.add(mniSystem);

		mniNimbus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLaf(frame, "nimbus");
			}
		});
		mniMetal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLaf(frame, "metal");
			}
		});
		mniSystem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeLaf(frame, "system");
			}
		});

		menuBar.add(InfoMenu);
		InfoMenu.add(SobreAction);
		frame.setLayout(new GridLayout(7, 1));
		pnlBusca = new JPanel();
		btnBusca.setText("Search Base");
		btnBusca.setToolTipText("Here you search for your .CSV file that will be partitioned.");
		btnConfirma.setText("Confirm Division");
		pnlBusca.add(btnBusca);
		pnlMetricas = new JPanel();
		pnlRepartir = new JPanel();
		pnlRepartir.setLayout(new GridLayout(1, 3));
		pnlRepartir.add(new JLabel(""));
		pnlRepartir.add(btnConfirma);
		pnlRepartir.add(new JLabel(""));

		pnlMetricas.add(listaMetricas);
		listaMetricas.setToolTipText("Here you select the attribute you will use as the basis for partitioning");
		pnlIntervalo = new JPanel();
		pnlIntervalo.setLayout(new GridLayout(4, 2));

		pnlIntervalo.add(new JLabel("Unit Type : "));
		pnlIntervalo.add(listaDeUnidades);
		listaDeUnidades.setToolTipText("Set here if it will be Months or Years in your division");
		listaDeUnidades.addItem("Select...");
		listaDeUnidades.addItem("Month(s)");
		listaDeUnidades.addItem("Year(s)");
		pnlIntervalo.add(new JLabel("Duration : "));
		pnlIntervalo.add(listaDeDuracao);
		pnlIntervalo.add(new JLabel("Interval : "));
		pnlIntervalo.add(tamanhoIntervalo);
		tamanhoIntervalo.setToolTipText("Select an integer to jump between ranges");
		listaDeDuracao.setToolTipText("Set range as a integer");
		pnlIntervalo.add(new JLabel("Date Formats Accepted : "));
		pnlIntervalo.add(listaDeFormatos);
		listaDeFormatos.setToolTipText(
				"Here you select the format of your temporal attribute. \\ N Take a look at the file first to avoid errors!");

		frame.add(pnlBusca);
		frame.add(new JLabel("                                     Select a DATE type temporal attribute below"));
		frame.add(pnlMetricas);
		frame.add(new JLabel("                                                  " + "                       Interval"));

		frame.add(pnlIntervalo);
		frame.add(pnlRepartir);

		btnConfirma.setToolTipText("Select the save directory and default name of the resulting files");

		listaDeDuracao.setEnabled(false);
		tamanhoIntervalo.setEnabled(false);
		listaDeFormatos.setEnabled(false);
		listaDeUnidades.setEnabled(false);
		btnConfirma.setEnabled(false);
		listaMetricas.setEnabled(false);
		btnBusca.addActionListener(this);
		btnConfirma.addActionListener(this);
		listaDeUnidades.addActionListener(this);
		listaMetricas.addActionListener(this);
		SobreAction.addActionListener(this);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == SobreAction) {
			JOptionPane.showMessageDialog(null,
					"" + "Application developed for the Software Engineering discipline\n"
							+ "                 Team: Cleyciane Frias, Juliana Abreu, Raul Vitor\n"
							+ "                               Universidade Federal do Acre \n"
							+ "                                            2018 - 2019",
					"About", JOptionPane.PLAIN_MESSAGE);
		}

		if (e.getSource() == btnBusca) {
			Es.DefineIntervalo(1, listaDeDuracao);
			Es.DefineIntervalo(3, tamanhoIntervalo);
			Es.populaFormatos(listaDeFormatos);
			caminho = Es.EscolheArquivo();
			if (caminho != "") {
				informou = false;

				while (informou == false) {
					inicio = JOptionPane.showInputDialog(null,
							"Enter the line where the header of the .csv file is located.");
					if (inicio == null) {
						JOptionPane.showMessageDialog(null, "Try Again", "Metric Needed For Operation",
								JOptionPane.ERROR_MESSAGE);
					} else {
						informou = true;
					}
				}
				if (inicio.equals("")) {
					inicio = "1";
				}

				informou = false;

				while (informou == false) {
					separador = JOptionPane.showInputDialog(null, "Enter file separator symbol");
					if (separador == null) {
						JOptionPane.showMessageDialog(null, "Try Again", "Metric Needed For Operation",
								JOptionPane.ERROR_MESSAGE);
					} else {
						informou = true;
					}
				}
				if (separador.isEmpty()) {
					separador = ",";
				}

				boolean success = Es.PreencheMetricas(listaMetricas, separador, caminho, Integer.parseInt(inicio));
				if (!success) {
					JOptionPane.showMessageDialog(null, "File capture failed");
				}
				if (success) {
					listaMetricas.setEnabled(true);
					listaDeUnidades.setEnabled(true);
					listaDeDuracao.setEnabled(true);
					listaDeFormatos.setEnabled(true);
					btnConfirma.setEnabled(true);
					tamanhoIntervalo.setEnabled(true);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Select a file!");
			}
		}
		if (e.getSource() == btnConfirma) {
			String campos = "";
			int invalido = 0;

			if (listaDeUnidades.getSelectedIndex() < 1) {
				campos = "\nUnity\n";
				invalido = 1;
			}

			if (listaDeFormatos.getSelectedIndex() < 1) {
				campos += "Format";
				invalido = 1;
			}

			if (invalido == 1) {
				JOptionPane.showMessageDialog(null, "Fill in the fields :" + campos, "Error in parameterization",
						JOptionPane.ERROR_MESSAGE);

			}

			if (invalido != 1) {
				String caminhoFinal = Es.EscolheArquivo();

				if (!caminhoFinal.equals("")) {
					File f = Es.geraArquivo(caminho);
					String d = null;
					try {
						d = Es.pegaPrimeiraData(f, listaMetricas.getSelectedIndex(), separador,
								Integer.parseInt(inicio));
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "You need to confirm the operation", "Partitioning failed",
								JOptionPane.ERROR_MESSAGE);
					}

					String d2 = Es.pegaUltimaData(f, listaMetricas.getSelectedIndex(), separador,
							Integer.parseInt(inicio));

					try {

						if (Es.Particiona(f, d, Integer.parseInt(listaDeDuracao.getSelectedItem().toString()),
								listaMetricas.getSelectedIndex(), caminhoFinal,
								listaDeFormatos.getSelectedItem().toString(), separador,
								listaDeUnidades.getSelectedIndex(), Integer.parseInt(inicio), d2,
								Integer.parseInt(tamanhoIntervalo.getSelectedItem().toString()))) {

							JOptionPane.showMessageDialog(null, "Processing completed", "Successful operation",
									JOptionPane.PLAIN_MESSAGE);
							int i = JOptionPane.showConfirmDialog(null, "Do You Want to Perform Another Operation?",
									"Options", JOptionPane.YES_NO_OPTION);
							if (i == JOptionPane.YES_OPTION) {

							} else if (i == JOptionPane.NO_OPTION) {
								JOptionPane.showMessageDialog(null, "Thank you for using Cronos", "Thanks",
										JOptionPane.INFORMATION_MESSAGE);
								System.exit(0);
							}

						}

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			}

		}

	}

	// Teste
	public static void changeLaf(JFrame frame, String laf) {
		if (laf.equals("metal")) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		}
		if (laf.equals("nimbus")) {
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		}
		if (laf.equals("system")) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		}

		SwingUtilities.updateComponentTreeUI(frame);
	}

}
