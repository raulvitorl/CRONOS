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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Escolhedor {

	@SuppressWarnings("resource")
	public boolean PreencheMetricas(JComboBox<String> listaMetricas, String separador, String caminho, int inicio) {
		listaMetricas.removeAllItems();
		JFileChooser escolheArquivo = new JFileChooser();
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("*csv", "csv");
		escolheArquivo.setFileFilter(filtro);
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
		int cont = 0, atributos = 0;

		try {
			s = br.readLine();

		} catch (IOException e1) {
			return false;
		}
		String[] textoSeparado = s.split(separador);
		while (s != null) {
			cont++;
			if (cont == inicio) {
				atributos = textoSeparado.length;
				for (int i = 0; i < atributos; i++) {
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

	public boolean Particiona(File arquivoOrigem, String primeiraData, int QuantidadeIntervalo, int coluna,
			String caminhoFinal, String formato, String separador, int tipoIntervalo, int inicio, String ultimaData)
			throws Exception {
		int numeroDoArquivo = 0;
		Date d1 = formataData(primeiraData, formato);
		Calendar c2 = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		Calendar ctemp2 = Calendar.getInstance();

		c1.setTime(d1);
		c2.setTime(d1);
		ctemp2.setTime(d1);

		if (formato.length()==19) {
			if (tipoIntervalo == 0) {
				c2.add(Calendar.DAY_OF_MONTH, QuantidadeIntervalo - 1);
				ctemp2.add(Calendar.DAY_OF_MONTH, 1);
				ctemp2.add(Calendar.SECOND, 1);
				c2.add(Calendar.SECOND, 2);
			}
			if (tipoIntervalo == 1) {
				c2.add(Calendar.MONTH, QuantidadeIntervalo - 1);
				ctemp2.add(Calendar.MONTH, 1);
				ctemp2.add(Calendar.SECOND, 1);
				c2.add(Calendar.SECOND, 2);
			}
			if (tipoIntervalo == 2) {
				c2.add(Calendar.YEAR, QuantidadeIntervalo );
				ctemp2.add(Calendar.YEAR, 1);
				ctemp2.add(Calendar.SECOND, 1);
				c2.add(Calendar.SECOND, 2);
			}

		}else{
			if (tipoIntervalo == 0) {
				c2.add(Calendar.DAY_OF_MONTH, QuantidadeIntervalo - 1);
				ctemp2.add(Calendar.DAY_OF_MONTH, 1);
				ctemp2.add(Calendar.SECOND, 1);
				c2.add(Calendar.SECOND, 1);
			}
			if (tipoIntervalo == 1) {
				c2.add(Calendar.MONTH, QuantidadeIntervalo - 1);
				ctemp2.add(Calendar.MONTH, 1);
				ctemp2.add(Calendar.SECOND, 1);
				c2.add(Calendar.SECOND, 1);
			}
			if (tipoIntervalo == 2) {
				c2.add(Calendar.YEAR, QuantidadeIntervalo );
				ctemp2.add(Calendar.YEAR, 1);
				ctemp2.add(Calendar.SECOND, 1);
				c2.add(Calendar.SECOND, 1);
			}
		}

		InputStream is = null;
		ArrayList<File> file = new ArrayList<File>();
		ArrayList<OutputStream> os = new ArrayList<OutputStream>();
		ArrayList<OutputStreamWriter> osw = new ArrayList<OutputStreamWriter>();
		file.add(new File(caminhoFinal + numeroDoArquivo + ".csv"));

		try {
			os.add(new FileOutputStream(file.get(numeroDoArquivo), true));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		osw.add(new OutputStreamWriter(os.get(numeroDoArquivo)));
		try {
			is = new FileInputStream(arquivoOrigem);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String s = null;
		int cont = 0;
		String[] textoSeparado = {};
		String[] cabecalho = {};
		try {
			s = br.readLine();
			br.mark(5000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (s != null) {
			cont++;

			// Cabeçalho do arquivo
			if (cont == 1) {
				cabecalho = s.split(separador);

				for (int i = 0; i < cabecalho.length; i++) {

					if (i == cabecalho.length) {
						osw.get(numeroDoArquivo).write(cabecalho[i]);
					}

					if (i != cabecalho.length) {
						osw.get(numeroDoArquivo).write(cabecalho[i] + ",");
					}
				}
				osw.get(numeroDoArquivo).write("\r\n");
				// Depois que escreve o cabeçalho, pula uma linha no arquivo

			}
			// Dados de fato
			if (cont > inicio) {
				textoSeparado = s.split(separador);
				Date temp = new Date();
				Calendar ctemp = Calendar.getInstance();

				try {
					try {
						if (textoSeparado.length < 2) {
							return false;
						}
						temp = formataData(textoSeparado[coluna], formato);
						ctemp.setTime(temp);
						// Adicionando um milisegundo a data temporaria que foi tirada do arquivo para
						// auxiliar na comparação
						ctemp.add(Calendar.SECOND, 1);
						// System.out.println("ANTES DO SE");
						System.out.println("De : " + c1.getTime() + " até " + c2.getTime());

					} catch (ParseException pe) {
						pe.printStackTrace();
					}
					// System.out.println(
					// "DATA TEMPORARIA : " + ctemp.getTime() + " E A DATA DE DIVISÃO :" +
					// ctemp2.getTime());
					if (ctemp.equals((ctemp2))) {
						br.mark(5000);
						//JOptionPane.showMessageDialog(null, "DATA PARA ONDE DEVE VOLTAR É: " + textoSeparado[coluna]);
					}

					if ((ctemp.compareTo(c1) > 0) && (ctemp.compareTo(c2) < 0)) {
						// System.out.println("Data aceita: " + ctemp.getTime());
						try {
							for (int i = 0; i < textoSeparado.length; i++) {
								if (i == textoSeparado.length) {
									osw.get(numeroDoArquivo).write(textoSeparado[i]);
								}
								if (i != textoSeparado.length) {
									osw.get(numeroDoArquivo).write(textoSeparado[i] + ",");
								}
							}
							osw.get(numeroDoArquivo).write("\r\n");
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {

						if (!textoSeparado[coluna].equals(ultimaData)) {
							br.reset();
							// System.out.println("REGISTRO MARCADO: " + s);
							numeroDoArquivo++;
							// System.out.println("-----------------------------INTERVALO " +
							// numeroDoArquivo
							// + "-----------------------------");

							// System.out.println("Ultima data desse intervalo" + ctemp.getTime());
							System.out.println("Gerar proximo arquivo e novo intervalo");

							if (tipoIntervalo == 0) {
								c1.add(Calendar.DAY_OF_MONTH, 1);
								c2.add(Calendar.DAY_OF_MONTH, 1);
								ctemp2.add(Calendar.DAY_OF_MONTH, 1);

							}
							if (tipoIntervalo == 1) {
								c1.add(Calendar.MONTH, 1);
								c2.add(Calendar.MONTH, 1);
								ctemp2.add(Calendar.MONTH, 1);
							}
							if (tipoIntervalo == 2) {
								c1.add(Calendar.YEAR, 1);
								c2.add(Calendar.YEAR, 1);
								ctemp2.add(Calendar.YEAR, 1);
							}

							System.out.println("De : " + c1.getTime() + " até " + c2.getTime());
							file.add(new File(caminhoFinal + numeroDoArquivo + ".csv"));
							try {
								os.add(new FileOutputStream(file.get(numeroDoArquivo), true));
							} catch (FileNotFoundException e2) {
								e2.printStackTrace();
							}
							osw.add(new OutputStreamWriter(os.get(numeroDoArquivo)));

							try {
								for (int i = 0; i < cabecalho.length; i++) {
									if (i == cabecalho.length) {
										osw.get(numeroDoArquivo).write(cabecalho[i]);
									}
									if (i != cabecalho.length) {
										osw.get(numeroDoArquivo).write(cabecalho[i] + ",");
									}
								}
								osw.get(numeroDoArquivo).write("\r\n");
							} catch (IOException e) {
								e.printStackTrace();
							}

							/*
							 * try { for (int i = 0; i < textoSeparado.length; i++) { if (i ==
							 * textoSeparado.length) { osw.get(numeroDoArquivo).write(textoSeparado[i]); }
							 * if (i != textoSeparado.length) {
							 * osw.get(numeroDoArquivo).write(textoSeparado[i] + ","); } }
							 * osw.get(numeroDoArquivo).write("\r\n"); } catch (IOException e) {
							 * e.printStackTrace(); }
							 */

						}

						if (textoSeparado[coluna].equals(ultimaData)) {
							break;
						}

						// System.out.println("Data atual armazenada: " + s);
					}

				} catch (NullPointerException npe) {
					npe.printStackTrace();
				}
			}
			try {
				s = br.readLine();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			for (OutputStreamWriter osr : osw) {
				osr.close();
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return true;
	}

	public String pegaPrimeiraData(File arquivo, int posicao, String separador, int inicio) {
		InputStream is = null;
		try {
			is = new FileInputStream(arquivo);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String s = null;
		try {
			s = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int cont = 1;
		while (s != null) {
			cont++;
			String[] textoSeparado = s.split(separador);
			// NÃ£o sei por que mas o mÃ©todo sÃ³ funcionou assim
			if (cont > inicio + 1) {
				return textoSeparado[posicao];
			}

			try {
				s = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	public String pegaUltimaData(File arquivo, int posicao, String separador, int inicio) {
		InputStream is = null;

		try {
			is = new FileInputStream(arquivo);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String s = null;
		try {
			s = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (s != null) {
			String[] textoSeparado = s.split(separador);
			try {
				s = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (s == null) {
				return textoSeparado[posicao];
			}

		}

		return null;
	}

	public File geraArquivo(String caminho) {
		File arquivo = new File(caminho);
		return arquivo;
	}

	public String EscolheArquivo() {
		JFileChooser escolheArquivo = new JFileChooser();
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("*csv", "csv");
		escolheArquivo.setFileFilter(filtro);
		int resposta = escolheArquivo.showOpenDialog(new JDialog());
		String caminhofinal = "";
		if (resposta == JFileChooser.APPROVE_OPTION) {
			caminhofinal = escolheArquivo.getSelectedFile().getPath().toString();
		}
		// JOptionPane.showMessageDialog(null, "Efetuando PartiÃÂ§ÃÂ£o", "Aguarde",
		// JOptionPane.INFORMATION_MESSAGE, icon);
		/*
		 * try { GeraIntervalo(listaDeUnidades.getSelectedItem().toString(),
		 * listaDeDuracao.getSelectedItem().toString(),listaMetricas.getSelectedIndex(),
		 * caminhofinal); } catch (Exception e) { e.printStackTrace(); }
		 */
		return caminhofinal;
	}

	public boolean DefineIntervalo(int tipoIntervalo, JComboBox<String> listaDeDuracao) {
		listaDeDuracao.removeAllItems();
		switch (tipoIntervalo) {
		case 0:
			for (int i = 1; i < 31; i++) {
				listaDeDuracao.addItem(Integer.toString(i));
			}
			break;
		case 1:
			for (int i = 1; i < 12; i++) {
				listaDeDuracao.addItem(Integer.toString(i));
			}
			break;
		case 2:
			for (int i = 1; i < 12; i++) {
				listaDeDuracao.addItem(Integer.toString(i));
			}
			break;
		default:
			break;
		}

		return true;
	}

	public static java.sql.Date formataData(String data, String formato) throws Exception {
		if (data == null || data.equals(""))
			return null;
		java.sql.Date date = null;
		try {
			// DJANGO
			// DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			// Arquivo Teste
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			date = new java.sql.Date(((java.util.Date) formatter.parse(data)).getTime());
		} catch (ParseException e) {

		}
		return date;
	}

	public void populaFormatos(JComboBox<String> listaDeFormatos) {
		listaDeFormatos.addItem(("EUA"));
		listaDeFormatos.addItem("yyyy-MM-dd'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("yyyy/MM/dd'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("yyyy.MM.dd'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("yyyy-MM-dd HH:mm:ss");
		listaDeFormatos.addItem("yyyy/MM/dd HH:mm:ss");
		listaDeFormatos.addItem("yyyy.MM.dd HH:mm:ss");
		listaDeFormatos.addItem("yyyy-MM-dd HH:mm");
		listaDeFormatos.addItem("yyyy/MM/dd HH:mm");
		listaDeFormatos.addItem("yyyy.MM.dd HH:mm");
		listaDeFormatos.addItem("YYYY-MM-DD");
		listaDeFormatos.addItem("YYYY/MM/DD");
		listaDeFormatos.addItem("YYYY.MM.DD");
		listaDeFormatos.addItem("BR");
		listaDeFormatos.addItem("dd-MM-yyyy'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("dd/MM/yyyy'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("dd.MM.yyyy'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("dd-MM-yyyy HH:mm:ss");
		listaDeFormatos.addItem("dd/MM/yyyy HH:mm:ss");
		listaDeFormatos.addItem("dd.MM.yyyy HH:mm:ss");
		listaDeFormatos.addItem("dd-MM-yyyy HH:mm");
		listaDeFormatos.addItem("d/MM/yyyy HH:mm");
		listaDeFormatos.addItem("d.MM.yyyy HH:mm");
		listaDeFormatos.addItem("dd-MM-yyyy");
		listaDeFormatos.addItem("dd/MM/yyyy");
		listaDeFormatos.addItem("dd.MM.yyyy");
	}

}