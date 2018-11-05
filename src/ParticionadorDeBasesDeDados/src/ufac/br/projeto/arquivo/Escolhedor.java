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
	static ImageIcon icon = new ImageIcon(Escolhedor.class.getResource("loading.gif").getFile());
	JButton btnBusca = new JButton();
	JButton btnConfirma = new JButton();
	JPanel pnlBusca,pnlMetricas,pnlIntervalo,pnlRepartir;
	JComboBox<String> listaMetricas = new JComboBox<>();
	JComboBox<String> listaIntervalos1 = new JComboBox<>();
	JComboBox<String> listaIntervalos2 = new JComboBox<>();
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
		pnlIntervalo.add(new JLabel("Entre : "));
		pnlIntervalo.add(listaIntervalos1);
		pnlIntervalo.add(new JLabel("E : "));		
		pnlIntervalo.add(listaIntervalos2);
		this.add(pnlBusca);
		this.add(new JLabel("                                   "
				+ "                                      Métricas"));
		this.add(pnlMetricas);
		this.add(new JLabel("                                                  "
				+ "                       Intervalo"));

		this.add(pnlIntervalo);
		this.add(pnlRepartir);
		btnBusca.addActionListener(this);
		btnConfirma.addActionListener(this);
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
		if(arg0.getSource()==btnBusca){
			listaIntervalos1.removeAllItems();
			listaIntervalos2.removeAllItems();
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
		}
		if(arg0.getSource()==listaMetricas){
			listaIntervalos1.removeAllItems();
			listaIntervalos2.removeAllItems();
			getElementos(listaMetricas.getSelectedIndex());
		}
		if(arg0.getSource()==listaIntervalos1 || arg0.getSource()==listaIntervalos2){
			getElementos(listaMetricas.getSelectedIndex());
		}
		if(arg0.getSource()==btnConfirma){
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
				GeraIntervalo(listaIntervalos1.getSelectedItem().toString(), listaIntervalos2.getSelectedItem().toString(),listaMetricas.getSelectedIndex(),caminhofinal);
			} catch (Exception e) {
				e.printStackTrace();
			}


		}	




	}

	public void getElementos(int n){
		File file = new File(caminho);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
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
			if(cont>1){
				try {
					listaIntervalos1.addItem(textoSeparado[n]);
					listaIntervalos2.addItem(textoSeparado[n]);

				} catch (NullPointerException npe) {
					JOptionPane.showMessageDialog(null, "O indice informado no inicio está incorreto");
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
		int cont2=0;
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
					} catch (ParseException pe) {JOptionPane.showMessageDialog(null, "Filtro Inválido!");}
					if(temp.after(d1) && temp.before(d2)){	
						cont2++;
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
				} catch (NullPointerException npe) {JOptionPane.showMessageDialog(null, "O indice informado no inicio está incorreto");}
			}
			try {
				s = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}		
		JOptionPane.showMessageDialog(null, "Concluido");
		JOptionPane.showMessageDialog(null, cont2+" Registros no intervalo");
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
		} catch (ParseException e) {            
			throw e;
		}
		return date;
	}
}