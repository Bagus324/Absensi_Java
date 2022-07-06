package present;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

public class Presentation extends JFrame {
	public static final int DEFAULT_WIDTH = 1028;
	public static final int DEFAULT_HEIGHT = 768;
	public String[] columnNames = { "Waktu", "Nama Karyawan", "Departemen", "Keterangan" };
	public String[] NamaKaryawan = { "Bagus", "Tri", "Yulianto", "Darmawan" };
	private DefaultTableModel TableModel;
	private DefaultTableModel TableModelLoad;
	public int nRow = 0;
	public JTable table;
	public JTable tableLoad;
	public JPanel Layout;
	public JPanel PanelTopRow1,PanelTopRow2, PanelBot, Contain;
	public Calendar cal;
	public JLabel DTM, DTMLabel, DTMLabelLoad;
	public Object jam;
	public JFileChooser chose = new JFileChooser();
	public DateTimeFormatter formatterFrame = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	public DateTimeFormatter formatterLabel = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	public DateTimeFormatter formatterLabelBot = DateTimeFormatter.ofPattern("HH:mm:ss");
	public JComboBox cbKaryawan;
	public int index;
	private DocumentBuilder builder;
	private JFileChooser chooser = new JFileChooser();

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public String[] getNamaKaryawan() {
		return NamaKaryawan;
	}

	public void setNamaKaryawan(String[] namaKaryawan) {
		NamaKaryawan = namaKaryawan;
	}

	public DefaultTableModel getTableModel() {
		return TableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		TableModel = tableModel;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public void setLayout(JPanel layout) {
		Layout = layout;
	}



	public JLabel getDTM() {
		return DTM;
	}

	public void setDTM(JLabel dTM) {
		DTM = dTM;
	}

	public Object getJam() {
		return jam;
	}

	public void setJam(Object jam) {
		this.jam = jam;
	}

	public DateTimeFormatter getFormatterFrame() {
		return formatterFrame;
	}

	public void setFormatterFrame(DateTimeFormatter formatterFrame) {
		this.formatterFrame = formatterFrame;
	}

	public JComboBox getCbKaryawan() {
		return cbKaryawan;
	}

	public void setCbKaryawan(JComboBox cbKaryawan) {
		this.cbKaryawan = cbKaryawan;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public DocumentBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(DocumentBuilder builder) {
		this.builder = builder;
	}

	public static int getDefaultWidth() {
		return DEFAULT_WIDTH;
	}

	public static int getDefaultHeight() {
		return DEFAULT_HEIGHT;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			JFrame frame = new Presentation();
			frame.setTitle("Combined");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});

	}

	public Presentation() {
		setSize(1920, 1000);
		//tabel absen
		TableModel = new DefaultTableModel(columnNames, nRow);
		table = new JTable(TableModel);
		JScrollPane ScrollTable = new JScrollPane(table);
		//tabel load
		TableModelLoad = new DefaultTableModel(columnNames, nRow);
		tableLoad = new JTable(TableModelLoad);
		JScrollPane ScrollTableLoad = new JScrollPane(tableLoad);
		//cb nama karyawan
		cbKaryawan = new JComboBox(NamaKaryawan);
		//label
		DTMLabelLoad= new JLabel("Load Data Absen Karyawan");
		DTMLabel = new JLabel("Absensi Karyawan Tanggal : "+LocalDateTime.now().format(formatterLabel).toString());
		DTM = new JLabel(LocalDateTime.now().format(formatterLabelBot).toString());
		//label jam
		clock();
		//declare panel all
		PanelTopRow1 = new JPanel();
		PanelTopRow2 = new JPanel();
		PanelTopRow1.setLayout(new BorderLayout());
		PanelTopRow2.setLayout(new BorderLayout());
		PanelBot = new JPanel();
		PanelBot.setLayout(new FlowLayout());
		Contain = new JPanel();
		Contain.setLayout(new FlowLayout());
		//panel atas
		PanelTopRow1.add(DTMLabel,BorderLayout.NORTH);
		PanelTopRow2.add(DTMLabelLoad,BorderLayout.NORTH);
		//panel tengah
		PanelTopRow1.add(ScrollTable,BorderLayout.CENTER);
		PanelTopRow2.add(ScrollTableLoad,BorderLayout.CENTER);
		//panel bawah
		PanelBot.add(DTM);
		PanelBot.add(cbKaryawan);
		addButton(PanelBot, "Absen", event -> add());
		addButton(PanelBot, "Simpan", event -> saveDocument());
		addButton(PanelBot, "Open", event -> LoadData());
		//inserting 2 jframe
		Contain.add(PanelTopRow1);
		Contain.add(PanelTopRow2);
		add(Contain, BorderLayout.CENTER);
		add(PanelBot, BorderLayout.SOUTH);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

	public JButton addButton(Container c, String title, ActionListener listener) {
		JButton button = new JButton(title);
		c.add(button);
		button.addActionListener(listener);

		return button;
	}

	public void add() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		jam = LocalDateTime.now().format(formatterFrame).toString();
		index = cbKaryawan.getSelectedIndex();
		switch (index) {
		case 0:
			if (hour <= 10) {
				TableModel.addRow(new Object[] { jam, "Bagus", "IT", "Absen Masuk" });
			} else {
				TableModel.addRow(new Object[] { jam, "Bagus", "IT", "Absen Keluar" });
			}
			if (hour >= 10 && min>=01 && hour <=11) {
				TableModel.addRow(new Object[] { jam, "Bagus", "IT", "Absen Masuk (Terlambat)" });
			}
			break;
		case 1:
			if (hour <= 10) {
				TableModel.addRow(new Object[] { jam, "Tri", "SO", "Absen Masuk" });
			} else {
				TableModel.addRow(new Object[] { jam, "Tri", "SO", "Absen Keluar" });
			}
			if (hour >= 10 && min>=01 && hour <=11) {
				TableModel.addRow(new Object[] { jam, "Tri", "SO", "Absen Masuk (Terlambat)" });
			} 
			break;
		case 2:
			if (hour <= 10) {
				TableModel.addRow(new Object[] { jam, "Yulianto", "OB", "Absen Masuk" });
			} else {
				TableModel.addRow(new Object[] { jam, "Yulianto", "OB", "Absen Keluar" });
			}
			if (hour >= 10 && min>=01 && hour <=11) {
				TableModel.addRow(new Object[] { jam, "Yulianto", "OB", "Absen Masuk (Terlambat)" });
			}
			break;
		case 3:
			if (hour <= 10) {
				TableModel.addRow(new Object[] { jam, "Darmawan", "HR", "Absen Masuk" });
			} else {
				TableModel.addRow(new Object[] { jam, "Darmawan", "HR", "Absen Keluar" });
			}
			if (hour >= 10 && min>=01 && hour <=11) {
				TableModel.addRow(new Object[] { jam, "Darmawan", "HR", "Absen Masuk (Terlambat)" });
			}
			break;
		default:
			break;
		}

	}

	public void clock() {
		Thread t = new Thread() {
			public void run() {
				try {
					while (true) {
						cal = new GregorianCalendar();
						int day = cal.get(Calendar.DAY_OF_MONTH);
						int mon = cal.get(Calendar.MONTH);
						int year = cal.get(Calendar.YEAR);

						int sec = cal.get(Calendar.SECOND);
						int min = cal.get(Calendar.MINUTE);
						int hour = cal.get(Calendar.HOUR);
						DTM.setText(year + "/" + mon + "/" + day + "  " + hour + ":" + min + ":" + sec);
						sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	public void saveDocument() {
		try {
			if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			File file = chooser.getSelectedFile();
			Document doc = buildDocument();
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
					"http://www.w3.org/TR/2000/CR-SVG-20000802/DTD/svg-20000802.dtd");
			t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD SVG 20000802//EN");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			t.transform(new DOMSource(doc), new StreamResult(Files.newOutputStream(file.toPath())));
		} catch (TransformerException | IOException ex) {
			ex.printStackTrace();
		}
	}
	

	public Document buildDocument() {
		String namespace = "http://www.w3.org/2000/svg";
		Document doc = builder.newDocument();
		Element svgElement = doc.createElementNS(namespace, "svg");
		doc.appendChild(svgElement);

		for (int count = 0; count < table.getRowCount(); count++) {
			Element dataKaryawan = doc.createElementNS(namespace, "absen");
			dataKaryawan.setAttribute("waktu", "" + table.getValueAt(count, 0));
			dataKaryawan.setAttribute("nama", "" + table.getValueAt(count, 1));
			dataKaryawan.setAttribute("departemen", "" + table.getValueAt(count, 2));
			dataKaryawan.setAttribute("keterangan", "" + table.getValueAt(count, 3));
			svgElement.appendChild(dataKaryawan);
		}
		return doc;
	}
	

	public void LoadData() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("dom"));
		chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("XML files", "xml"));
		int r = chooser.showOpenDialog(this);
		if (r != JFileChooser.APPROVE_OPTION)
			return;
		final File file = chooser.getSelectedFile();

		new SwingWorker<Document, Void>() {
			protected Document doInBackground() throws Exception {
				if (builder == null) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					builder = factory.newDocumentBuilder();
				}
				return builder.parse(file);
			}

			protected void done() {

				try {
					Document doc = get();
					NodeList nl = doc.getElementsByTagName("absen");
					for (int x = 0, size = nl.getLength(); x < size; x++) {
						Object dep, ket, nama, waktu;
						// <absen departemen="IT" keterangan="Absen Keluar" nama="Bagus" waktu="04-07-2022 16:13:00"/>
						dep = nl.item(x).getAttributes().getNamedItem("departemen").getNodeValue();
						ket = nl.item(x).getAttributes().getNamedItem("keterangan").getNodeValue();
						nama = nl.item(x).getAttributes().getNamedItem("nama").getNodeValue();
						waktu = nl.item(x).getAttributes().getNamedItem("waktu").getNodeValue();
						TableModelLoad.addRow(new Object[] { waktu, nama, dep, ket });
						DTMLabelLoad.setText("Load Data Absen Karyawan Tanggal : "+waktu.toString().substring(0, 10));
						System.out.println(waktu.toString().substring(0, 10));
					}
					validate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.execute();
	}
}
