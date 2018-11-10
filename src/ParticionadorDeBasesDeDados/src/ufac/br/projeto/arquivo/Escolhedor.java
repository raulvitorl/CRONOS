package ufac.br.projeto.arquivo;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
public class Escolhedor extends JFrame implements ActionListener{
	private static final long serialVersionUID = -7214032754820796452L;
	static  String caminho,separador;
	static int N;

	//Variavel de controle para evitar nullpointer, o processo só possegue se houver um arquivo selecionado
	public boolean arquvoSelecionado=false,sucesso=true;

	static ImageIcon icon = new ImageIcon(Escolhedor.class.getResource("loading.gif").getFile());
	JButton btnBusca = new JButton();
	JButton btnConfirma = new JButton();

	JPanel pnlBusca,pnlMetricas,pnlIntervalo,pnlRepartir;
	JComboBox<String> listaMetricas = new JComboBox<>();	

	//Vetores para armazenar as unidades e intervalos
	JComboBox<String> listaDeUnidades = new JComboBox<String>();

	JComboBox<String> listaDeDuracao = new JComboBox<String>();

	ButtonGroup bgFiltros;
	public Escolhedor(){
		//TESTE DE VERSÃO
		super("Particionador de Bases de Dados");
		this.setBackground(new Color(245));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 550);
		this.setResizable(false);
		this.setLayout(new GridLayout(7, 1));
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

		setListaDeDuracao(0);

		this.add(pnlBusca);
		this.add(new JLabel("                                   "
				+ "                                      Atributos"));
		this.add(pnlMetricas);
		this.add(new JLabel("                                                  "
				+ "                       Intervalo"));

		this.add(pnlIntervalo);
		this.add(pnlRepartir);
		btnBusca.addActionListener(this);
		btnConfirma.addActionListener(this);
		listaDeUnidades.addActionListener(this);
		listaMetricas.addActionListener(this);
		setLocationRelativeTo(null);
		setVisible(true); 
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		Escolhedor e = new Escolhedor();
		e.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {

		if(arg0.getSource()==listaDeUnidades){
			listaDeDuracao.removeAllItems();
			setListaDeDuracao(listaDeUnidades.getSelectedIndex());
		}		
		if(arg0.getSource()==btnBusca){
			listaMetricas.removeAllItems();			
			JFileChooser escolheArquivo = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("*csv", "csv");
			escolheArquivo.setFileFilter(filtro);
			int resposta = escolheArquivo.showOpenDialog(new JDialog());
			if (resposta == JFileChooser.APPROVE_OPTION) {
				caminho = escolheArquivo.getSelectedFile().getPath().toString();
				File file = new File(caminho);
				N =Integer.parseInt(JOptionPane.showInputDialog(null,"Em que linha do arquivo as classes iniciam?"));
				separador =JOptionPane.showInputDialog(null,"Qual caracter separador do arquivo?"); 
				InputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String s = null;
				int cont = 0,atributos=0;
				String[] textoSeparado= {};
				try {
					s = br.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				while (s != null) {
					textoSeparado=s.split(separador);
					cont++;
					if(cont==N){
						atributos = textoSeparado.length;
						for(int i=0;i<atributos;i++){
							try {
								listaMetricas.addItem(textoSeparado[i]);
							} catch (NullPointerException npe) {
								npe.printStackTrace();
							}

						}
					}				

					try {
						s = br.readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			arquvoSelecionado=true;


			btnConfirma.setFocusable(true);
		}

		if(arg0.getSource()==btnConfirma && !arquvoSelecionado){
			JOptionPane.showMessageDialog(null, "Selecione uma base!");
		}

		if(arg0.getSource()==btnConfirma && arquvoSelecionado){
			JFileChooser escolheArquivo = new JFileChooser();
			FileNameExtensionFilter filtro = new FileNameExtensionFilter("*csv", "csv");
			escolheArquivo.setFileFilter(filtro);
			int resposta = escolheArquivo.showOpenDialog(new JDialog());
			String caminhofinal = "";
			if (resposta == JFileChooser.APPROVE_OPTION) {
				caminhofinal = escolheArquivo.getSelectedFile().getPath().toString();
			}
			JOptionPane.showMessageDialog(null, "Efetuando Partição", "Aguarde", JOptionPane.INFORMATION_MESSAGE, icon);
			try {
				GeraIntervalo(listaDeUnidades.getSelectedItem().toString(), listaDeDuracao.getSelectedItem().toString(),listaMetricas.getSelectedIndex(),caminhofinal);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}	

	}

	public void setListaDeDuracao(int n) {

		String[] vetMeses = {"1","2","3","4","5","6","7","8","9","10","11"};
		String[] vetDias = 
			{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
					"16","17","18","19","20","21","22","23","24","25","26","27","28","29"
					,"30","31"};
		String[] vetAnos = {"1","2","3","4","5","6","7","8","9","10"};

		if(n==0){
			for(int i=0;i<vetDias.length;i++){
				listaDeDuracao.addItem(vetDias[i]);
			}
		}

		if(n==1){
			for(int i=0;i<vetMeses.length;i++){
				listaDeDuracao.addItem(vetMeses[i]);
			}
		}

		if(n==2){
			for(int i=0;i<vetAnos.length;i++){
				listaDeDuracao.addItem(vetAnos[i]);
			}
		}
	}

	public void GeraIntervalo(String primeiraData,String segundaData,int coluna,String caminhoFinal) throws Exception{


		Date d1 = formataData(primeiraData);

		Date d2 = formataData(segundaData);
		File file = new File(caminho);
		InputStream is = null;
		File file2 = new File(caminhoFinal);
		OutputStream os = null;
		try {
			os = new FileOutputStream(file2, true);
		} catch (FileNotFoundException e2) {e2.printStackTrace();}
		try {
			os = new FileOutputStream(file2, true);
		} catch (FileNotFoundException e2) {e2.printStackTrace();}
		OutputStreamWriter osw = new OutputStreamWriter(os);
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e1) {e1.printStackTrace();}
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String s = null;
		int cont = 0;
		String[] textoSeparado= {};
		try {
			s = br.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (s != null) {
			textoSeparado=s.split(separador);
			cont++;
			//Escrevendo o cabeçalho
			if(cont==1){
				for(int i=0;i<textoSeparado.length;i++){
					if(i==textoSeparado.length){
						osw.write(textoSeparado[i]);
					}
					if(i!=textoSeparado.length){
						osw.write(textoSeparado[i]+",");
					}

				}
				osw.write("\r\n");
			}
			if(cont>1){
				Date temp = null;
				try {
					try {

				temp = formataData(textoSeparado[coluna]);
					} catch (ParseException pe) {JOptionPane.showMessageDialog(null, "Atributo não temporal!");sucesso=false;break;}
					if(temp.after(d1) && temp.before(d2)){	
						try {
							for(int i=0;i<textoSeparado.length;i++){
								System.out.println(i);
								if(i==textoSeparado.length){
									osw.write(textoSeparado[i]);
								}
								if(i!=textoSeparado.length){
									osw.write(textoSeparado[i]+",");
								}

							}
							osw.write("\r\n");
						} catch (IOException e){e.printStackTrace();}
					}
				} catch (NullPointerException npe) {JOptionPane.showMessageDialog(null, "Linha de inicio dos dados incorreta!");
				sucesso=false;
				break;
				}
			}
			try {
				s = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}	
		if(sucesso) {JOptionPane.showMessageDialog(null, "Concluido");}
		try {
			osw.close();
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static java.sql.Date formataData(String data) throws Exception { 
		if (data == null || data.equals(""))
			return null;
		java.sql.Date date = null;
		try {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'");
			date = new java.sql.Date( ((java.util.Date)formatter.parse(data)).getTime() );
		} catch (ParseException e){            

		}
		return date;
	}
}