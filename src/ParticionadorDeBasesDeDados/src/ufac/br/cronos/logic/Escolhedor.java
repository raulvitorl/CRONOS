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
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import ufac.br.cronos.gui.LoadingFrame;

public class Escolhedor {

	private BufferedReader br;

	@SuppressWarnings("resource")
	public boolean PreencheMetricas(JComboBox<String> listaMetricas, String separador, String caminho, int inicio) {
		listaMetricas.removeAllItems();
		JFileChooser escolheArquivo = new JFileChooser();
		FileNameExtensionFilter filtro = new FileNameExtensionFilter("*csv", "csv");

		escolheArquivo.setFileFilter(filtro);
		File file = new File(caminho);

		if (!((caminho.charAt(caminho.length() - 1) == 'v') && (caminho.charAt(caminho.length() - 2) == 's')
				&& (caminho.charAt(caminho.length() - 3) == 'c'))) {
			JOptionPane.showMessageDialog(null,
					"Formato : " + caminho.charAt(caminho.length() - 3) + caminho.charAt(caminho.length() - 2)
							+ caminho.charAt(caminho.length() - 1) + "   Identificado \n Selecione um arquivo .CSV",
					"Arquivo nao CSV selecionado", JOptionPane.ERROR_MESSAGE);
			return false;
		}

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
			String caminhoFinal, String formato, String separador, int tipoIntervalo, int inicio, String ultimaData,
			int tamanhoIntervalo) throws Exception {

		final int valor = tamanhoIntervalo;

		JOptionPane.showMessageDialog(null, "VALOR : " + valor);

		System.out.println("PULAR DE :" + tamanhoIntervalo + " EM " + tamanhoIntervalo);
		System.out.println("FORMATO: " + formato);

		LoadingFrame lf = new LoadingFrame();
		lf.setVisible(true);
		int numeroDoArquivo = 0;
		Date d1 = formataData(primeiraData, formato);
		if (d1 == null) {
			lf.setVisible(false);
			return false;
		}

		Calendar c2 = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();

		try {
			c1.setTime(d1);
		} catch (NullPointerException npe) {
			lf.setVisible(false);
			JOptionPane.showMessageDialog(null, "Atributo nao temporal selecionado");
			return false;
		}

		c2.setTime(d1);

		if (formato.length() == 19) {
			if (tipoIntervalo == 1) {
				c2.add(Calendar.MONTH, QuantidadeIntervalo);
				c2.add(Calendar.SECOND, 2);
			}
			if (tipoIntervalo == 2) {
				c2.add(Calendar.YEAR, QuantidadeIntervalo);
				c2.add(Calendar.SECOND, 2);
			}

		} else {

			if (tipoIntervalo == 1) {
				c2.add(Calendar.MONTH, QuantidadeIntervalo);
				c2.add(Calendar.SECOND, 1);
			}
			if (tipoIntervalo == 2) {
				c2.add(Calendar.YEAR, QuantidadeIntervalo);
				c2.add(Calendar.SECOND, 1);
			}
		}

		final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		System.out.println("DATA INICIAL : " + df.format(d1));
		System.out.println("DATA FINAL" + df.format(c2.getTime()));

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
			br.mark(10000000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int numeroDaLinha = 0;
		while (s != null) {
			cont++;
			if (cont == 1) {
				cabecalho = s.split(separador);

				for (int i = 0; i < cabecalho.length; i++) {

					if (i == cabecalho.length - 1) {
						osw.get(numeroDoArquivo).write(cabecalho[i]);
					}

					if (i != cabecalho.length - 1) {
						osw.get(numeroDoArquivo).write(cabecalho[i] + ",");
					}
				}
				osw.get(numeroDoArquivo).write("\r\n");
				numeroDaLinha++;
			}

			Calendar ctemp = Calendar.getInstance();
			if (cont > inicio) {
				textoSeparado = s.split(separador);
				Date temp = new Date();

				try {
					if (textoSeparado.length < 2) {
						lf.setVisible(false);
						return false;
					}

					temp = formataData(textoSeparado[coluna], formato);

					if (temp == null)
						return false;

					ctemp.setTime(temp);
					ctemp.add(Calendar.SECOND, 1);

					System.out.println(
							"SE " + temp + " ENTRE " + df.format(c1.getTime()) + " E " + df.format(c2.getTime()));

					Calendar ctest = Calendar.getInstance();
					ctest = (Calendar) c2.clone();
					ctest.add(Calendar.SECOND, -2);

					System.out.println(temp.compareTo(ctest.getTime()));

					System.out.println("	TEMP : " + temp + " CTEST :  " + df.format(ctest.getTime()));
					System.out.println(" ANTES DE ENTRAR NO IGUAIS :" + df.format(c1.getTime()));

					if (temp.compareTo(ctest.getTime()) == 0 || temp.compareTo(ctest.getTime()) == 1) {

						br.reset();
						System.out.println("IGUAIS!!!!!!");
						numeroDaLinha = 0;

						if (tipoIntervalo == 1) {
							c1.add(Calendar.MONTH, valor);
							c2.add(Calendar.MONTH, valor);

						}
						if (tipoIntervalo == 2) {
							c1.add(Calendar.YEAR, valor);
							c2.add(Calendar.YEAR, valor);

						}

						System.out.println("DEPOIS DE ENTRAR NO IGUAIS :" + df.format(c1.getTime()));

						numeroDoArquivo++;

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
					}
					if ((ctemp.compareTo(c1) > 0) && (ctemp.compareTo(c2) < 0)) {
						numeroDaLinha++;

						// Se for o primeiro arquivo pode fazer tudo sem problemas
						if (numeroDoArquivo == 0) {
							System.out.println("DATA ACEITA: " + df.format(ctemp.getTime()));
							try {
								for (int i = 0; i < textoSeparado.length; i++) {
									if (i == textoSeparado.length - 1) {
										osw.get(numeroDoArquivo).write(textoSeparado[i]);
									}
									if (i != textoSeparado.length - 1) {
										osw.get(numeroDoArquivo).write(textoSeparado[i] + ",");
									}
								}
								osw.get(numeroDoArquivo).write("\r\n");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						//Se o arquivo for outro, escreve a linha 0 que e o cabecalho, conta uma linha pra ignorar que eh a 1,
						//e depois disso pode escrever tranquilo
						if (numeroDoArquivo > 0 && numeroDaLinha>1) {
							System.out.println("DATA ACEITA: " + df.format(ctemp.getTime()));
							try {
								for (int i = 0; i < textoSeparado.length; i++) {
									if (i == textoSeparado.length - 1) {
										osw.get(numeroDoArquivo).write(textoSeparado[i]);
									}
									if (i != textoSeparado.length - 1) {
										osw.get(numeroDoArquivo).write(textoSeparado[i] + ",");
									}
								}
								osw.get(numeroDoArquivo).write("\r\n");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

					} else {

						if (textoSeparado[coluna].equals(ultimaData)) {
							break;
						}

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
		lf.setVisible(false);
		return true;
	}

	public String pegaPrimeiraData(File arquivo, int posicao, String separador, int inicio) {
		InputStream is = null;
		try {
			is = new FileInputStream(arquivo);
		} catch (FileNotFoundException e1) {
			return null;
		}
		InputStreamReader isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
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
		return caminhofinal;
	}

	public boolean DefineIntervalo(int tipoIntervalo, JComboBox<String> lista) {
		lista.removeAllItems();
		switch (tipoIntervalo) {
		case 0:
			break;
		case 1:
			for (int i = 1; i < 12; i++) {
				lista.addItem(Integer.toString(i));
			}
			break;
		case 2:
			for (int i = 1; i < 51; i++) {
				lista.addItem(Integer.toString(i));
			}
			break;
		case 3:
			for (int i = 1; i < 13; i++) {
				lista.addItem(Integer.toString(i));
			}
			break;
		default:
			break;
		}

		return true;
	}

	public static java.sql.Date formataData(String data, String formato) {
		if (data == null || data.equals(""))
			return null;
		java.sql.Date date = null;
		DateFormat formatter;
		formatter = new SimpleDateFormat(formato);

		try {
			date = new java.sql.Date(((java.util.Date) formatter.parse(data)).getTime());
		} catch (ParseException pe) {
			JOptionPane.showMessageDialog(null, "Verifique: \nAtributo Selecionado\nFormato da Data",
					"Falha ao Acessar Datas", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return date;
	}

	public void populaFormatos(JComboBox<String> listaDeFormatos) {
		listaDeFormatos.addItem("Formatos BRASILEIROS de Datas: ");
		listaDeFormatos.addItem("dd-MM-yyyy'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("dd/MM/yyyy'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("dd.MM.yyyy'T'HH:mm:ss'Z'");
		listaDeFormatos.addItem("dd-MM-yyyy HH:mm:ss");
		listaDeFormatos.addItem("dd/MM/yyyy HH:mm:ss");
		listaDeFormatos.addItem("dd.MM.yyyy HH:mm:ss");
		listaDeFormatos.addItem("dd-MM-yyyy HH:mm");
		listaDeFormatos.addItem("dd/MM/yyyy HH:mm");
		listaDeFormatos.addItem("dd.MM.yyyy HH:mm");
		listaDeFormatos.addItem("dd-MM-yyyy");
		listaDeFormatos.addItem("dd/MM/yyyy");
		listaDeFormatos.addItem("dd.MM.yyyy");
		listaDeFormatos.addItem(("Formato NORTE AMERICANOS de Datas:"));
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
	}

}