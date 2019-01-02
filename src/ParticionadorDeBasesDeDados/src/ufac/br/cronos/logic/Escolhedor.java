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
			
			try {
				s = br.readLine();
				
			} catch (IOException e1) {
				return false;
			}
			String[] textoSeparado = s.split(separador);
			while (s != null){
				cont++;
				if(cont==inicio){
					atributos = textoSeparado.length;
					for(int i=0;i<atributos;i++){
						try {
							listaMetricas.addItem(textoSeparado[i]);
							listaMetricas.setSelectedItem(i);
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

	public boolean Particiona(File arquivoOrigem,String primeiraData,int QuantidadeIntervalo,int coluna,String caminhoFinal,String formato,String separador,int tipoIntervalo,int inicio) throws Exception{		
		int numeroDoArquivo=0;		
		Date d1 = formataData(primeiraData,formato);
		Calendar c2 = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		
		c1.setTime(d1);
		c2.setTime(d1);

		if(tipoIntervalo==0){
			c2.add(Calendar.DAY_OF_MONTH, QuantidadeIntervalo-1);
			c2.add(Calendar.SECOND, 2);
		}
		if(tipoIntervalo==1){
			c2.add(Calendar.MONTH, QuantidadeIntervalo-1);
			c2.add(Calendar.SECOND, 2);
		}
		if(tipoIntervalo==2){
			c2.add(Calendar.YEAR, QuantidadeIntervalo-1);
			c2.add(Calendar.SECOND,2);
		}
		
		InputStream is = null;
		File file = new File(caminhoFinal+numeroDoArquivo+".csv");
		OutputStream os = null;
		try {
			os = new FileOutputStream(file, true);
		} catch (FileNotFoundException e2) {e2.printStackTrace();}
		try {
			os = new FileOutputStream(file, true);
		} catch (FileNotFoundException e2) {e2.printStackTrace();}
		OutputStreamWriter osw = new OutputStreamWriter(os);
		try {
			is = new FileInputStream(arquivoOrigem);
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
			//Cabeçalho do arquivo
			if(cont==1){
				for(int i=0;i<textoSeparado.length;i++){
					if(i==textoSeparado.length){osw.write(textoSeparado[i]);}
					if(i!=textoSeparado.length){osw.write(textoSeparado[i]+",");}
				}
				osw.write("\r\n");
				//Depois que escreve o cabeçalho, pula uma linha no arquivo
				s = br.readLine();
				
			}
			
			
			Date temp = null;
			Calendar ctemp = Calendar.getInstance();

			//Dados de fato
			if(cont>inicio){
				try {
					try {
						if(textoSeparado.length<2){return false;}		
						temp = formataData(textoSeparado[coluna],formato);
						ctemp.setTime(temp);
						//Adicionando um milisegundo a data temporaria que foi tirada do arquivo para auxiliar na comparação
						ctemp.add(Calendar.SECOND,1);
						System.out.println("ANTES DO SE");
						System.out.println("De : "+c1.getTime()+" até "+c2.getTime());
						
					} catch (ParseException pe) {pe.printStackTrace();}
					if((ctemp.compareTo(c1)>0) && (ctemp.compareTo(c2)<0)){	
						System.out.println("Data aceita: "+ctemp.getTime());
						try {
							for(int i=0;i<textoSeparado.length;i++){
								if(i==textoSeparado.length){
									osw.write(textoSeparado[i]);
								}
								if(i!=textoSeparado.length){osw.write(textoSeparado[i]+",");}
							}
							osw.write("\r\n");
						} catch (IOException e){e.printStackTrace();}
					}else {
						numeroDoArquivo++;					
						System.out.println("-----------------------------INTERVALO "+numeroDoArquivo+"-----------------------------");
						
						System.out.println("Ultima data desse intervalo"+ctemp.getTime());
						System.out.println("Gerar proximo arquivo e novo intervalo");
												
						if(tipoIntervalo==0){c1.add(Calendar.DAY_OF_MONTH,1);}
						if(tipoIntervalo==1){c1.add(Calendar.MONTH,1);}
						if(tipoIntervalo==2){c1.add(Calendar.YEAR,1);}
						
						if(tipoIntervalo==0){c2.add(Calendar.DAY_OF_MONTH,1);}
						if(tipoIntervalo==1){c2.add(Calendar.MONTH,1);}
						if(tipoIntervalo==2){c2.add(Calendar.YEAR,1);}
						System.out.println("De : "+c1.getTime()+" até "+c2.getTime());
						file.delete();
						
						
						}							
					
				} catch (NullPointerException npe) {
					npe.printStackTrace();								
				}
			try {
				s = br.readLine();
								
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
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
		int cont=1;
		while(s!=null){
			cont++;
			String[] textoSeparado= s.split(separador);
			//NÃ£o sei por que mas o mÃ©todo sÃ³ funcionou assim
			if(cont>inicio+1){
				return textoSeparado[posicao];
			}

			try {
				s = br.readLine();
			} catch (IOException e){
				e.printStackTrace();
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
		//JOptionPane.showMessageDialog(null, "Efetuando PartiÃƒÂ§ÃƒÂ£o", "Aguarde", JOptionPane.INFORMATION_MESSAGE, icon);
		/*try {
			GeraIntervalo(listaDeUnidades.getSelectedItem().toString(), listaDeDuracao.getSelectedItem().toString(),listaMetricas.getSelectedIndex(),caminhofinal);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return caminhofinal;
	}

	public boolean DefineIntervalo(int tipoIntervalo,JComboBox<String> listaDeDuracao){
		listaDeDuracao.removeAllItems();
		switch (tipoIntervalo) {
		case 0:
			for(int i=1;i<31;i++){
				listaDeDuracao.addItem(Integer.toString(i));
			}
			break;
		case 1:
			for(int i=1;i<12;i++){
				listaDeDuracao.addItem(Integer.toString(i));
			}
			break;
		case 2:
			for(int i=1;i<12;i++){
				listaDeDuracao.addItem(Integer.toString(i));
			}
			break;
		default:
			break;
		}
			
		return true;
	}

	public static java.sql.Date formataData(String data,String formato) throws Exception { 		
		if (data == null || data.equals(""))
			return null;
		java.sql.Date date = null;
 		try {
 			//DJANGO
 			//DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
 			//Arquivo Teste
 			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 			date = new java.sql.Date( ((java.util.Date)formatter.parse(data)).getTime() );
 		} catch (ParseException e){            
 
 		}
 		return date;
 	}
	
	public void populaFormatos(JComboBox<String> listaDeFormatos){
		listaDeFormatos.addItem("yyyy-MM-dd HH:mm");
		listaDeFormatos.addItem("yyyy/MM/dd HH:mm");
		listaDeFormatos.addItem("yyyy.MM.dd HH:mm");
		listaDeFormatos.addItem("YYYY-MM-DD");
		listaDeFormatos.addItem("YYYY/MM/DD");
		listaDeFormatos.addItem("YYYY.MM.DD");		
	}

}