package ufac.br.cronos.logic;

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
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
public class Escolhedor{


	@SuppressWarnings("resource")
	public boolean PreencheMetricas(JComboBox<String> listaMetricas,String separador,String caminho,int inicio){
		listaMetricas.removeAllItems();			
		JFileChooser escolheArquivo = new JFileChooser();
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("*csv", "csv");
		escolheArquivo.setFileFilter(filtro);
		//int resposta = escolheArquivo.showOpenDialog(new JDialog());
			File file = new File(caminho);			 
			InputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e1) {
				return false;
			}
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = null;
			int cont = 0,atributos=0;
			String[] textoSeparado= {};
			try {
				s = br.readLine();
			} catch (IOException e1) {
				return false;
			}
			while (s != null) {
				textoSeparado=s.split(separador);
				cont++;
				if(cont==inicio){
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
		
		return true;

	}
	
	public boolean Particiona(File file,String primeiraData,int QuantidadeIntervalo,int coluna,String caminhoFinal,String formato,String separador,String tipoIntervalo) throws Exception{
		formato = "YYYY-MM-DD";
		Date d1 = formataData(primeiraData,formato);
		Calendar c2 = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d1);
		if(tipoIntervalo.equals("Dia(s)")){
			c2.add(Calendar.DAY_OF_MONTH, QuantidadeIntervalo);
		}
		if(tipoIntervalo.equals("Mês(es)")){
			c2.add(Calendar.MONTH, QuantidadeIntervalo);
		}
		if(tipoIntervalo.equals("Ano(s)")){
			c2.add(Calendar.YEAR, QuantidadeIntervalo);
		}
		

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
			Date temp = null;
			Calendar ctemp = Calendar.getInstance();
			if(cont>1)
				try {
					try {

				temp = formataData(textoSeparado[coluna],formato);
				ctemp.setTime(temp);
				
					} catch (ParseException pe) {return false;}
					if(ctemp.after(c1.getTime()) && ctemp.before(c2.getTime())){	
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
				return  false;
				}
			}
			try {
				s = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				osw.close();
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return true;
		}
	
	
	public String pegaPrimeiraData(File arquivo,int posicao,String separador,int inicio){
		InputStream is = null;
		try {
			is = new FileInputStream(arquivo);
		} catch (FileNotFoundException e1) {e1.printStackTrace();}
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String s = null;
		try {
			s = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int cont=0;
		while(s!=null){
			cont++;
			String[] textoSeparado= s.split(separador);
			if(cont>inicio){
				return textoSeparado[posicao];
			}
				
		}
		
		return null;
			
	}
			
	public File geraArquivo(String caminho){
		File arquivo = new File(caminho);
		return arquivo;
	}
		
	public String EscolheArquivo(){
		JFileChooser escolheArquivo = new JFileChooser();
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("*csv", "csv");
		escolheArquivo.setFileFilter(filtro);
		int resposta = escolheArquivo.showOpenDialog(new JDialog());
		String caminhofinal = "";
		if (resposta == JFileChooser.APPROVE_OPTION) {
			caminhofinal = escolheArquivo.getSelectedFile().getPath().toString();
		}
		//JOptionPane.showMessageDialog(null, "Efetuando Partição", "Aguarde", JOptionPane.INFORMATION_MESSAGE, icon);
		/*try {
			GeraIntervalo(listaDeUnidades.getSelectedItem().toString(), listaDeDuracao.getSelectedItem().toString(),listaMetricas.getSelectedIndex(),caminhofinal);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return caminhofinal;
	}
	
	public boolean DefineIntervalo(String tipoIntervalo,JComboBox<String> listaDeDuracao){
		listaDeDuracao.removeAllItems();
		if(tipoIntervalo.equals("Dia(s)")){
			for(int i=1;i<30;i++){
				listaDeDuracao.addItem(Integer.toString(i));
			}
		}
		if(tipoIntervalo.equals("Mês(es)")){
			for(int i=1;i<12;i++){
				listaDeDuracao.addItem(Integer.toString(i));
			}
		}
		if(tipoIntervalo.equals("Ano(s)")){
			for(int i=1;i<11;i++){
				listaDeDuracao.addItem(Integer.toString(i));
			}
		}
		return true;
	}
	
	public static java.sql.Date formataData(String data,String formato) throws Exception { 
		if (data == null || data.equals(""))
			return null;
		java.sql.Date date = null;
		try {
			//DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'");
			DateFormat formatter = new SimpleDateFormat(formato);
			date = new java.sql.Date( ((java.util.Date)formatter.parse(data)).getTime() );
		} catch (ParseException e){            

		}
		return date;
	}
}