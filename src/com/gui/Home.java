package com.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.algorithm.NeuralNetwork;
import com.database.DB_Connection;
import com.database.DB_FileUploader;
import com.database.DB_Import;
import com.database.DB_WrapperDelete;
import com.database.DB_WrapperLoad;
import com.database.spatialobject.AccessPoint;
import com.database.spatialobject.Connectivity;
import com.database.spatialobject.Connector;
import com.database.spatialobject.Floor;
import com.database.spatialobject.Partition;
import com.database.spatialobject.UploadObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.gui.util.InteractionState;
import com.gui.util.calendar.JTimeChooser;
import com.indoorobject.IndoorObjsFactory;
import com.indoorobject.movingobject.DstMovingObj;
import com.indoorobject.movingobject.MovingObj;
import com.indoorobject.movingobject.MovingObjResponse;
import com.indoorobject.movingobject.RegularMultiDestCustomer;
import com.indoorobject.station.Pack;
import com.indoorobject.station.Station;
import com.indoorobject.utility.IdrObjsUtility;
import com.json.TimeDeserializer;
import com.json.TimeSerializer;
import com.spatialgraph.D2DGraph;

import diva.util.java2d.Polygon2D;
import java.awt.GridLayout;

public class Home extends JApplet {

	private static String lastSelectedFileName = null;
	private JFrame frmTrajectoryGenerator;
	private JTextField txtStationMaxNumInPart;
	private JTextField txtStationMaxNumInArea;
	private JTextField txtScanRange;
	private JTextField txtScanRate;
	
	private JButton btnImport;
	private JButton btnDeleteFile;
	private JButton btnView;
	private JButton btnDecompAll;
	private JButton btnDeleteNav;
	private JButton btnStationGenerate;
	private JButton btnObjectInit;
	private JButton btnObjectStart;
	private JButton btnObjectStop;
	private JButton btnSnapShot;
	private JButton btnMovingObjUpload;

	private JPanel filePanel;
	private JPanel mapPanel;
	private JPanel controlPanel;
	private JPanel dbiPanel;
	private JPanel uclPanel;
	private JPanel movingObjectPanel;
	private JPanel playPanel;
	private JPanel positionAlgPanel;

	private JLabel lblConnectedPartitions1;
	private JLabel lblselectedEntityLabel2;
	private JLabel lblConnectedPartitions2;
	private JLabel lblStationDistributerType2;
	private JLabel lblMovingObjectType2;
	private JLabel lblMaximumLifeSpan2;
	private JLabel lblMaximumVelocity2;
	private JLabel lblPositionAlgorithm2;
	private JLabel lblmaximumNumberIn;
	private JLabel lblCommentsMaxMovObjNumInPart;
	private JLabel lblmaxStepLength;
	private JLabel lblmoveRate;
	private JLabel lblstartTime;
	private JLabel lblendTime;
	private JLabel lblStationDistributerType1;
	private JLabel lblMaxNumberIn;
	private JLabel lblScanRate1;
	private JLabel lblScanRate2;
	private JLabel lblMovingObjectType1;
	private JLabel lblPositioningParamenters;
	private JLabel lblPositionAlgorithm1;
	private JLabel lblPropertiesFile;
	private JLabel lblGenerationPeriod1;
	private JLabel lblMaximumVelocity1;
	private JLabel lblMaximumLifeSpan1;
	private JLabel lblMaxMovObjNumInPart;
	private JLabel lblMovingObjectParamenters;
	private JLabel lblScanRange2;
	private JLabel lblScanRange1;
	private JLabel lblCommentStationMaxNumInPart;
	private JLabel lblStationType;
	private JLabel lblStationParameters;
	private JLabel lblUserConfigurationLoader;
	private JLabel lblselectedEntityLabel1;
	private JLabel lblselectedFloorLabel;
	private JLabel lblDBIEntities;
	private JLabel lblInputRssiPath;

	private JCheckBox chckbxPositiongData;
	private JCheckBox chckbxTrajectory;
	private JCheckBox chckbxTracking;
	private JCheckBox chckbxPositioningDevice;
	private JCheckBox chckbxEnvironment;
	
	private ButtonGroup visualButtonGroup;
	private ButtonGroup generateButtonGroup;

	private JScrollPane scrollPaneConsole;
	private JScrollPane movingObjectScroll;
	
	private JTabbedPane tabbedVITAPane;
	private JComboBox<String> stationTypeComboBox;
	private JComboBox<String> stationDistriTypeComboBox;
	private JComboBox<String> movingObjectTypeComboBox;
	private JComboBox<String> movObjDistributerTypeComboBox;
	private JComboBox<UploadObject> fileComboBox;
	private JComboBox<UploadObject> objectComboBox;
	private JComboBox<Floor> floorCombobox;
	private Calendar startCalendar;
	private Calendar endCalendar;

	private MapPainter mapPainter;

	private Connection connection = null;
	private UploadObject fileChosen = null;

	private Floor chosenFloor = null;
	private Partition selectedPart = null;
	private AccessPoint selectedAP = null;
	private Connector selectedCon = null;
	private DefaultListModel<Partition> connectedPartsModel;

	private ArrayList<UploadObject> files = null;
	private ArrayList<MovingObj> movingObjs = new ArrayList<MovingObj>();
	private ArrayList<MovingObj> destMovingObjs = new ArrayList<MovingObj>();
	private DefaultTableModel idTrajectoryModel = new DefaultTableModel();

	private MovingObjResponse movingObj;
	private MovingObj[] persons;

	private boolean empty = false;
	private double zoom = 1;
	private double previousX;
	private double previousY;
	private double currentX;
	private double currentY;

	private Map<Path2D, Partition> partitionsMap = new HashMap<Path2D, Partition>();
	private Map<Path2D, AccessPoint> accesspointsMap = new HashMap<Path2D, AccessPoint>();
	private Map<Path2D, Connector> connsMap = new HashMap<Path2D, Connector>();

	private HashMap<String, String> stationTypeMap = new HashMap<String, String>();
	private HashMap<String, String> movingObjTypeMap = new HashMap<String, String>();
	private HashMap<String, String> movObjInitMap = new HashMap<String, String>();
	private HashMap<String, String> stationInitMap = new HashMap<String, String>();
	private HashMap<String, String> positionAlgorithmMap = new HashMap<String, String>();

	private List<Partition> possibleConnectedPartsList;
	private JLabel lblConsole;
	private JLabel lblGenerationPeriod2;
	private JLabel lblMovObjDistributerType2;
	private JLabel lblMovObjDistributerType1;
	private JComboBox textField_1;
	private JComboBox textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JPanel panel_3;

	private Vector columnId;
	private Vector dataId;
	private Vector columnPath;
	private Vector dataPath;

	private JTable idTable;
	private JTable pathTable;
	private JTextField textField;
	private JTextField textField_7;
	private JTextField textField_12;
	private JTextField textField_13;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Home window = new Home();
					window.frmTrajectoryGenerator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Home() {
		initialize();
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		WebLookAndFeel.install();

		frmTrajectoryGenerator = new JFrame();
		frmTrajectoryGenerator.setTitle("BT-Gen");
		frmTrajectoryGenerator.setBounds(100, 100, 1094, 685);
		frmTrajectoryGenerator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTrajectoryGenerator.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1081, 646);
		frmTrajectoryGenerator.getContentPane().add(tabbedPane);

		JPanel inPanel = new JPanel();
		tabbedPane.addTab("Indoor Environment", null, inPanel, null);
		inPanel.setLayout(null);

		JPanel filePanel = new JPanel();
		filePanel.setBounds(0, 0, 1067, 72);
		inPanel.add(filePanel);
		filePanel.setLayout(null);

		fileComboBox = new JComboBox<UploadObject>();
		fileComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		fileComboBox.setBackground(Color.WHITE);
		fileComboBox.setBounds(10, 38, 225, 23);
		filePanel.add(fileComboBox);

		btnImport = new JButton("Import");
		btnImport.setBackground(Color.WHITE);
		btnImport.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnImport.setBounds(247, 38, 97, 23);
		filePanel.add(btnImport);

		btnDeleteFile = new JButton("Clear");
		btnDeleteFile.setBackground(Color.WHITE);
		btnDeleteFile.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDeleteFile.setBounds(356, 38, 97, 23);
		filePanel.add(btnDeleteFile);

		btnView = new JButton("Load");
		btnView.setBackground(Color.WHITE);
		btnView.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnView.setBounds(463, 38, 97, 23);
		filePanel.add(btnView);

		btnDecompAll = new JButton("Decompose");
		btnDecompAll.setBackground(Color.WHITE);
		btnDecompAll.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDecompAll.setBounds(574, 38, 110, 23);
		filePanel.add(btnDecompAll);

		JLabel lblFileLoader = new JLabel("File Loader");
		lblFileLoader.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblFileLoader.setBounds(10, 11, 186, 23);
		filePanel.add(lblFileLoader);

		JPanel inGenPanel = new JPanel();
		inGenPanel.setBounds(0, 73, 1067, 545);
		inPanel.add(inGenPanel);
		inGenPanel.setLayout(null);

		mapPanel = new JPanel();
		mapPanel.setBorder(null);
		mapPanel.setBackground(Color.WHITE);
		mapPanel.setBounds(12, 10, 670, 528);
		inGenPanel.add(mapPanel);
		mapPanel.setLayout(null);

		JLabel lblNavigation = new JLabel("Navigation");
		lblNavigation.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNavigation.setBounds(704, 10, 186, 23);
		inGenPanel.add(lblNavigation);

		JLabel label_7 = new JLabel("Floor:");
		label_7.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_7.setBounds(704, 43, 113, 23);
		inGenPanel.add(label_7);

		floorCombobox = new JComboBox<Floor>();
		floorCombobox.setFont(new Font("Dialog", Font.PLAIN, 11));
		floorCombobox.setBackground(Color.WHITE);
		floorCombobox.setBounds(807, 45, 247, 23);
		inGenPanel.add(floorCombobox);

		btnDeleteNav = new JButton("Clear");
		btnDeleteNav.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDeleteNav.setBackground(Color.WHITE);
		btnDeleteNav.setBounds(981, 80, 73, 23);
		inGenPanel.add(btnDeleteNav);
		
		JLabel lblConfiguration = new JLabel("Configuration");
		lblConfiguration.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblConfiguration.setBounds(704, 118, 186, 23);
		inGenPanel.add(lblConfiguration);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(704, 152, 350, 386);
		inGenPanel.add(tabbedPane_1);
		
		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Device", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel label = new JLabel("Export:");
		label.setFont(new Font("Dialog", Font.PLAIN, 14));
		label.setBounds(10, 11, 95, 23);
		panel_1.add(label);
		
		chckbxEnvironment = new JCheckBox("Environment");
		chckbxEnvironment.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxEnvironment.setBounds(113, 14, 105, 23);
		panel_1.add(chckbxEnvironment);
		
		chckbxPositioningDevice = new JCheckBox("Device Position");
		chckbxPositioningDevice.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxPositioningDevice.setBounds(233, 14, 121, 23);
		panel_1.add(chckbxPositioningDevice);
		
		JLabel label_1 = new JLabel("Device");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_1.setBounds(10, 44, 95, 23);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("Type:");
		label_2.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_2.setBounds(10, 65, 95, 23);
		panel_1.add(label_2);
		
		stationTypeComboBox = new JComboBox();
		stationTypeComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		stationTypeComboBox.setBackground(Color.WHITE);
		stationTypeComboBox.setBounds(113, 58, 222, 21);
		panel_1.add(stationTypeComboBox);
		
		JLabel label_3 = new JLabel("Deployment");
		label_3.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_3.setBounds(10, 98, 95, 23);
		panel_1.add(label_3);
		
		JLabel label_4 = new JLabel("Model:");
		label_4.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_4.setBounds(10, 119, 95, 23);
		panel_1.add(label_4);
		
		stationDistriTypeComboBox = new JComboBox();
		stationDistriTypeComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		stationDistriTypeComboBox.setBackground(Color.WHITE);
		stationDistriTypeComboBox.setBounds(113, 112, 222, 21);
		panel_1.add(stationDistriTypeComboBox);
		
		txtStationMaxNumInPart = new JTextField();
		txtStationMaxNumInPart.setToolTipText("Maximum for each room");
		txtStationMaxNumInPart.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtStationMaxNumInPart.setColumns(10);
		txtStationMaxNumInPart.setBounds(113, 154, 222, 21);
		panel_1.add(txtStationMaxNumInPart);
		
		txtStationMaxNumInArea = new JTextField();
		txtStationMaxNumInArea.setToolTipText("Maximum for each 100 meter square");
		txtStationMaxNumInArea.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtStationMaxNumInArea.setColumns(10);
		txtStationMaxNumInArea.setBounds(113, 185, 222, 21);
		panel_1.add(txtStationMaxNumInArea);
		
		JLabel label_5 = new JLabel("Device");
		label_5.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_5.setBounds(10, 162, 95, 23);
		panel_1.add(label_5);
		
		JLabel label_6 = new JLabel("Number:");
		label_6.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_6.setBounds(10, 183, 95, 23);
		panel_1.add(label_6);
		
		JLabel label_8 = new JLabel("Detection");
		label_8.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_8.setBounds(10, 216, 95, 23);
		panel_1.add(label_8);
		
		JLabel label_9 = new JLabel("Range:");
		label_9.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_9.setBounds(10, 237, 95, 23);
		panel_1.add(label_9);
		
		txtScanRange = new JTextField();
		txtScanRange.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtScanRange.setColumns(10);
		txtScanRange.setBounds(113, 230, 222, 21);
		panel_1.add(txtScanRange);
		
		JLabel label_10 = new JLabel("Detection");
		label_10.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_10.setBounds(10, 270, 95, 23);
		panel_1.add(label_10);
		
		JLabel label_11 = new JLabel("Frequency:");
		label_11.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_11.setBounds(10, 291, 95, 23);
		panel_1.add(label_11);
		
		txtScanRate = new JTextField();
		txtScanRate.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtScanRate.setColumns(10);
		txtScanRate.setBounds(113, 284, 222, 21);
		panel_1.add(txtScanRate);
		
		btnStationGenerate = new JButton("Generate");
		btnStationGenerate.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnStationGenerate.setBackground(Color.WHITE);
		btnStationGenerate.setBounds(238, 324, 97, 23);
		panel_1.add(btnStationGenerate);
		
		JPanel panel_2 = new JPanel();
		tabbedPane_1.addTab("Moving Object", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel label_12 = new JLabel("Scenario");
		label_12.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_12.setBounds(10, 11, 113, 23);
		panel_2.add(label_12);
		
		JLabel label_13 = new JLabel("Files:");
		label_13.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_13.setBounds(10, 32, 113, 23);
		panel_2.add(label_13);
		
		objectComboBox = new JComboBox<UploadObject>();
		objectComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		objectComboBox.setBackground(Color.WHITE);
		objectComboBox.setBounds(125, 13, 210, 23);
		panel_2.add(objectComboBox);
		
		btnMovingObjUpload = new JButton("Upload");
		btnMovingObjUpload.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnMovingObjUpload.setBackground(Color.WHITE);
		btnMovingObjUpload.setBounds(125, 46, 97, 23);
		panel_2.add(btnMovingObjUpload);
		
		JButton button_2 = new JButton("Clear");
		button_2.setFont(new Font("Dialog", Font.PLAIN, 11));
		button_2.setBackground(Color.WHITE);
		button_2.setBounds(238, 46, 97, 23);
		panel_2.add(button_2);
		
		movingObjectPanel = new JPanel();
		movingObjectPanel.setBackground(Color.LIGHT_GRAY);
		movingObjectPanel.setBounds(10, 80, 325, 119);
		panel_2.add(movingObjectPanel);
		
		movingObjectScroll = new JScrollPane();
		movingObjectScroll.setBackground(Color.LIGHT_GRAY);
		movingObjectScroll.setBounds(567, 486, 340, 137);
		panel_2.add(movingObjectScroll);
		
		JLabel label_14 = new JLabel("Export:");
		label_14.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_14.setBounds(10, 208, 91, 23);
		panel_2.add(label_14);
		
		chckbxTrajectory = new JCheckBox("Trajectory");
		chckbxTrajectory.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxTrajectory.setBounds(109, 211, 105, 23);
		panel_2.add(chckbxTrajectory);
		
		chckbxTracking = new JCheckBox("Raw RSSI");
		chckbxTracking.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxTracking.setBounds(229, 211, 121, 23);
		panel_2.add(chckbxTracking);
		
		btnObjectInit = new JButton("Init");
		btnObjectInit.setBackground(Color.WHITE);
		btnObjectInit.setBounds(60, 242, 56, 25);
		panel_2.add(btnObjectInit);
		
		btnObjectStart = new JButton("Start");
		btnObjectStart.setBackground(Color.WHITE);
		btnObjectStart.setBounds(126, 243, 62, 25);
		panel_2.add(btnObjectStart);
		
		btnObjectStop = new JButton("Stop");
		btnObjectStop.setBackground(Color.WHITE);
		btnObjectStop.setBounds(198, 242, 56, 25);
		panel_2.add(btnObjectStop);
		
		btnSnapShot = new JButton("Capture");
		btnSnapShot.setBackground(Color.WHITE);
		btnSnapShot.setBounds(264, 242, 71, 25);
		panel_2.add(btnSnapShot);
		
		JPanel panel_5 = new JPanel();
		tabbedPane_1.addTab("Machine", null, panel_5, null);
		panel_5.setLayout(null);
		
		JLabel lblMachine = new JLabel("Machine");
		lblMachine.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMachine.setBounds(10, 11, 113, 23);
		panel_5.add(lblMachine);
		
		JLabel label_16 = new JLabel("Files:");
		label_16.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_16.setBounds(10, 32, 113, 23);
		panel_5.add(label_16);
		
		JComboBox<UploadObject> comboBox_3 = new JComboBox<UploadObject>();
		comboBox_3.setFont(new Font("Dialog", Font.PLAIN, 11));
		comboBox_3.setBackground(Color.WHITE);
		comboBox_3.setBounds(125, 13, 210, 23);
		panel_5.add(comboBox_3);
		
		JButton button_7 = new JButton("Upload");
		button_7.setFont(new Font("Dialog", Font.PLAIN, 11));
		button_7.setBackground(Color.WHITE);
		button_7.setBounds(125, 46, 97, 23);
		panel_5.add(button_7);
		
		JButton button_8 = new JButton("Clear");
		button_8.setFont(new Font("Dialog", Font.PLAIN, 11));
		button_8.setBackground(Color.WHITE);
		button_8.setBounds(238, 46, 97, 23);
		panel_5.add(button_8);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.LIGHT_GRAY);
		panel_4.setBounds(10, 80, 325, 119);
		panel_5.add(panel_4);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBackground(Color.LIGHT_GRAY);
		panel_4.add(scrollPane_1);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnConnect.setBackground(Color.WHITE);
		btnConnect.setBounds(238, 210, 97, 23);
		panel_5.add(btnConnect);

		generateButtonGroup = new ButtonGroup();

		connectedPartsModel = new DefaultListModel<Partition>();

		possibleConnectedPartsList = new ArrayList<>();

		generateButtonGroup = new ButtonGroup();

		connectedPartsModel = new DefaultListModel<Partition>();

		possibleConnectedPartsList = new ArrayList<>();

		// Model For Path Table
		idTrajectoryModel = new DefaultTableModel() {

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// TODO Auto-generated method stub
				switch (columnIndex) {
				case 0:
					return Integer.class;
				case 1:
					return Color.class;
				case 2:
					return Boolean.class;
				default:
					return String.class;
				}
			}
		};

		idTrajectoryModel.addColumn("User Id");
		idTrajectoryModel.addColumn("Color");
		idTrajectoryModel.addColumn("Action");

		idTable = new JTable();
		idTable.setAutoResizeMode(WebTable.AUTO_RESIZE_OFF);
		idTable.setRowSelectionAllowed(false);
		idTable.setColumnSelectionAllowed(true);
		idTable.setPreferredScrollableViewportSize(new Dimension(345, 290));
		idTable.setModel(idTrajectoryModel);

		addActionListeners();
		addFocusListeners();

		switchStateForButtons(InteractionState.BEFORE_IMPORT);

		updateFileChooser();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				repaint();
			}

		}, 0, 100);
	}

	private void toggleBtnObjectStart() {
		// TODO Auto-generated method stub
//		if (btnObjectInit.isEnabled() && (!persons[0].getStartTime().equals("")) && !persons[0].getEndTime().equals(""))
//			btnObjectStart.setEnabled(true);
	}

	private void addFocusListeners() {

	}

	private void toggleGeneratationBtns(boolean state) {
		btnStationGenerate.setEnabled(state);
		btnObjectInit.setEnabled(state);
		toggleMovingObjectGenerationBtns(false);
	}

	private void toggleMovingObjectGenerationBtns(boolean state) {
		btnObjectStart.setEnabled(state);
		btnObjectStop.setEnabled(state);
		btnSnapShot.setEnabled(state);
	}

	private void switchStateForButtons(int state) {

		switch (state) {
		case InteractionState.BEFORE_IMPORT:
			btnImport.setEnabled(true);
			btnDeleteFile.setEnabled(false);
			btnView.setEnabled(false);
			btnDecompAll.setEnabled(false);
			toggleGeneratationBtns(false);
			break;
		case InteractionState.AFTER_IMPORT:
			btnImport.setEnabled(true);
			btnDeleteFile.setEnabled(true);
			btnView.setEnabled(true);
			btnDecompAll.setEnabled(false);
			toggleGeneratationBtns(false);
			break;
		case InteractionState.AFTER_UPLOAD_FILE:
			btnImport.setEnabled(true);
			btnDeleteFile.setEnabled(true);
			btnView.setEnabled(true);
			btnDecompAll.setEnabled(false);
			toggleGeneratationBtns(false);
			break;
		case InteractionState.AFTER_VIEW_FILE_NO_CHANGE:
			btnImport.setEnabled(true);
			btnDeleteFile.setEnabled(true);
			btnView.setEnabled(false);
			btnDecompAll.setEnabled(true);
			toggleGeneratationBtns(true);
			break;
		case InteractionState.AFTER_VIEW_FILE_CHANGED:
			btnImport.setEnabled(true);
			btnDeleteFile.setEnabled(true);
			btnView.setEnabled(true);
			btnDecompAll.setEnabled(true);
			toggleGeneratationBtns(true);
			break;
		case InteractionState.AFTER_DECOMPOSE:
			btnImport.setEnabled(true);
			btnDeleteFile.setEnabled(true);
			btnView.setEnabled(false);
			btnDecompAll.setEnabled(false);
			toggleGeneratationBtns(true);
			break;
		default:
			btnImport.setEnabled(false);
			btnDeleteFile.setEnabled(false);
			btnView.setEnabled(false);
			btnDecompAll.setEnabled(false);
			toggleGeneratationBtns(false);
		}

	}

	private void addActionListeners() {

		btnMovingObjUpload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uploadObjectFile();
			}
		});

		btnImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				uploadFile();
				updateFileChooser();
				if (files.size() > 0) {
					fileComboBox.setSelectedItem(files.get(files.size() - 1));
//					printUploadInfo();
					switchStateForButtons(InteractionState.AFTER_IMPORT);
				}
			}
		});

		fileComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				fileChosen = (UploadObject) fileComboBox.getSelectedItem();

				if (fileChosen != null) {
					if ((lastSelectedFileName != null) && (!fileChosen.getFilename().equals(lastSelectedFileName))) {
						switchStateForButtons(InteractionState.AFTER_VIEW_FILE_CHANGED);
					}
					lastSelectedFileName = fileChosen.getFilename();
				}
			}

		});

		btnView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewFile();

				List<Partition> isolatedPartitions = ClearIllegal
						.calIsolatedPartitions(DB_WrapperLoad.partitionDecomposedT);
				for (Partition isolatedPartition : isolatedPartitions) {
					// ClearIllegal.connectPossiblePartitons(isolatedPartition);
				}
			}
		});

		btnDeleteFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteFile();
			}

		});

	}

//	private void printUploadInfo() {
//		txtConsoleArea.append("You have extracted: \n");
//		txtConsoleArea.append(DB_WrapperLoad.floorT.size() + " floors\n");
//		txtConsoleArea.append(DB_WrapperLoad.partitionT.size() + " partitions\n");
//		txtConsoleArea.append(DB_WrapperLoad.accesspointT.size() + " doors\n");
//		txtConsoleArea.append(DB_WrapperLoad.connectorT.size() + " stairs\n");
//		txtConsoleArea.append("----------------------------------------------------------------\n");
//	}

//	private void printPartAPInfo() {
//		txtConsoleArea.append("Viewing File " + fileChosen.getUploadId() + ". " + fileChosen.getFilename() + "\n");
//		txtConsoleArea.append("There are in total: \n");
//		txtConsoleArea.append(DB_WrapperLoad.floorT.size() + " floors\n");
//		txtConsoleArea.append(DB_WrapperLoad.partitionT.size() + " partitions\n");
//		txtConsoleArea.append(DB_WrapperLoad.accesspointT.size() + " doors\n");
//		txtConsoleArea.append(DB_WrapperLoad.connectorT.size() + " stairs\n");
//		txtConsoleArea.append("----------------------------------------------------------------\n");
//	}

	private void uploadFile() {
		String default_path = System.getProperty("user.dir"); // + "//export
		// files";
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(default_path));
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".ifc") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Ifc Files";
			}
		};
		chooser.setFileFilter(filter);

		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			UploadObject object = new UploadObject();
			object.setFilename(file.getName());
			object.setFile_type("IFC");
			object.setFile_size((int) file.length());
			object.setDescription("");
			if (isFileExisted(object) == true) {
				System.out.println("File already existed!");
				JOptionPane.showMessageDialog(this, "File already existed!", "Error", JOptionPane.ERROR_MESSAGE);
//				txtConsoleArea.append("File Already Existed! PASS\n");
				return;
			}

//			txtConsoleArea.append("Uploading File......");
			DB_FileUploader uploader = new DB_FileUploader();
			boolean status = uploader.saveObjectToDB(object, file);
			if (!status) {
				System.out.println("ERROR");
				JOptionPane.showMessageDialog(this, "Unkown Error!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				// Update the table
				updateFileChooser();
				importFile();
				JOptionPane.showMessageDialog(this, "Uploading File is done!", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	private void uploadObjectFile() {
		String default_path = System.getProperty("user.dir"); // + "//export
		// files";
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(default_path));
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".json") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Json Files";
			}
		};
		chooser.setFileFilter(filter);

		int result = chooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			UploadObject object = new UploadObject();
			object.setFilename(file.getName());
			object.setFile_type("JSON");
			object.setFile_size((int) file.length());
			object.setDescription("");
			if (isFileExisted(object) == true) {
				System.out.println("File already existed!");
				JOptionPane.showMessageDialog(this, "File already existed!", "Error", JOptionPane.ERROR_MESSAGE);
//				txtConsoleArea.append("File Already Existed! PASS\n");
				return;
			}

			// load moving objects
			try {
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Date.class, new TimeSerializer());
				gsonBuilder.registerTypeAdapter(Date.class, new TimeDeserializer());
				Gson gson = gsonBuilder.setPrettyPrinting().serializeNulls().create();
				JsonReader reader = new JsonReader(new FileReader(file));
				reader.setLenient(true);
				movingObj = gson.fromJson(reader, MovingObjResponse.class);

				// Show the file in panel
				objectComboBox.removeAllItems();
				objectComboBox.addItem(object);
				persons = movingObj.getMovingObject();
				for (MovingObj person : persons) {
					PersonView view = new PersonView(person, person.getObjectId());
					movingObjectPanel.add(view);
					repaint();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean isFileExisted(UploadObject uploadFile) {
		for (UploadObject uo : files) {
			if (uploadFile.getFilename().equals(uo.getFilename())
					&& uploadFile.getFile_size() == uploadFile.getFile_size()) {
				return true;
			}
		}
		return false;
	}

	private void importFile() {
		connection = DB_Connection.connectToDatabase("conf/moovework.properties");
		try {
			fileChosen = (UploadObject) fileComboBox.getSelectedItem();
			DB_Import.importAll(connection, fileChosen.getUploadId(), DB_WrapperLoad.loadFile(connection, fileChosen));
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(PreparedStatement.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
		// Close the prepared statement and connection if necessary.
		finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(PreparedStatement.class.getName());
				lgr.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
	}

	private void viewFile() {
		int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to view the selected file?", "Confirmation",
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			connection = DB_Connection.connectToDatabase("conf/moovework.properties");

			try {
				fileChosen = (UploadObject) fileComboBox.getSelectedItem();
				DB_WrapperLoad.loadALL(connection, fileChosen.getUploadId());
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			mapPainter = new MapPainter(fileChosen.getUploadId());
			mapPanel.add(mapPainter);
			switchStateForButtons(InteractionState.AFTER_VIEW_FILE_NO_CHANGE);
		} else if (n == JOptionPane.NO_OPTION) {
			// Nothing
		} else {
			// Nothing
		}
		return;
	}

	private void deleteFile() {
		int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected file?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			connection = DB_Connection.connectToDatabase("conf/moovework.properties");

			try {
				fileChosen = (UploadObject) fileComboBox.getSelectedItem();
//				txtConsoleArea.append(fileChosen.getFilename() + " is deleted!!!\n");
				DB_WrapperDelete.deleteFile(connection, fileChosen);
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			fileChosen = null;
			updateFileChooser();
			JOptionPane.showMessageDialog(this, "Deleting File is done!", "Information",
					JOptionPane.INFORMATION_MESSAGE);
			// updateButtons();
		} else if (n == JOptionPane.NO_OPTION) {
			// Nothing
		} else {
			// Nothing
		}
		return;
	}

	private void updateFileChooser() {
		initFileList();
		loadFileChooser();
	}

	private void initFileList() {
		connection = DB_Connection.connectToDatabase("conf/moovework.properties");
		try {
			files = DB_WrapperLoad.loadFileTable(connection);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadFileChooser() {
		fileComboBox.removeAllItems();
		for (UploadObject uo : files) {
			fileComboBox.addItem(uo);
		}
		if (files.size() > 0) {
			fileComboBox.setSelectedItem(files.get(files.size() - 1));
			switchStateForButtons(InteractionState.AFTER_UPLOAD_FILE);
		} else {
			switchStateForButtons(InteractionState.BEFORE_IMPORT);
		}
	}

	private String decideOutputPath() {
		String default_path = System.getProperty("user.dir"); // + "//export
		// files";
		String outputPath = default_path;
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(default_path));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int intRetVal = jfc.showOpenDialog(this);
		if (intRetVal == JFileChooser.APPROVE_OPTION) {
			outputPath = jfc.getSelectedFile().getPath();
			return outputPath;
		} else {
			return null;
		}
	}

	// Generator Map Class
	private class MapPainter extends JPanel {

		/**
		 * 
		 */
		private MovingAdapter ma = new MovingAdapter();
		private int fileIDX;
		private boolean stationsGen = false;
		private boolean movingObjsGen = false;

		MapPainter(int fileID) {
			fileIDX = fileID;
			setSize(800, 800);
			setPreferredSize(new Dimension(800, 800));

			btnObjectStart.setEnabled(false);
			btnObjectStop.setEnabled(false);
			btnSnapShot.setEnabled(false);

			loadFloorChooser();

			clearMapPainterActionListener();
			addMapPainterActionListener();

			initUCLComboBox();
			loadPropFromFile("conf/pattern.properties");

			addMouseMotionListener(ma);
			addMouseWheelListener(ma);
			addMouseListener(ma);

			setDoubleBuffered(true);
			setOpaque(true);
			setBackground(Color.white);

			D2DGraph buildingD2D = new D2DGraph(DB_WrapperLoad.partitionDecomposedT,
					DB_WrapperLoad.accessPointConnectorT);
			buildingD2D.generateD2DDistance();
			for (Floor floor : DB_WrapperLoad.floorT) {
				floor.setPartitionsRTree(IdrObjsUtility.generatePartRTree(floor));
				floor.setD2dGraph(buildingD2D);
			}
		}

		private void addMapPainterActionListener() {
			floorCombobox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					chosenFloor = (Floor) floorCombobox.getSelectedItem();
					selectedPart = null;
					selectedAP = null;
					selectedCon = null;
				}

			});

			btnStationGenerate.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					storePropFromGUI("conf/pattern.properties");
					generateStations();
					stationsGen = true;
					if (movingObjsGen == true) {
						toggleBtnObjectStart();
					}
					if (chckbxPositioningDevice.isSelected() || chckbxEnvironment.isSelected()) {
						String outputPath = decideOutputPath();
						if (outputPath != null) {
							if (chckbxEnvironment.isSelected()) {
								String envDir = createEnvironmentOutputDir(outputPath);
								exportEnvironment(envDir);
							}
							if (chckbxPositioningDevice.isSelected()) {
								String stationDir = createStationOutputDir(outputPath);
								exportStations(stationDir);
							}
							JOptionPane.showMessageDialog(frmTrajectoryGenerator,
									"Generating Infrastrcture Data is done!", "Information",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}

			});

			chckbxTrajectory.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (chckbxTrajectory.isSelected() == true) {
						MovingObj.setTrajectoryFlag(true);
					} else {
						MovingObj.setTrajectoryFlag(false);
					}
				}

			});

			chckbxTracking.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (chckbxTracking.isSelected() == true) {
						MovingObj.setTrackingFlag(true);
					} else {
						MovingObj.setTrackingFlag(false);
					}
				}

			});

			btnObjectInit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					storePropFromGUI("conf/pattern.properties");
					generateMovingObjs();
					movingObjsGen = true;
					if (stationsGen == true) {
						btnObjectStart.setEnabled(true);
					}
				}
			});

			btnObjectStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (btnObjectStart.getText().equals("Start")) {
						if (chckbxTrajectory.isSelected() || chckbxTracking.isSelected()) {

							String outputPath = decideOutputPath();
							if (outputPath != null) {
								createMovingObjectOutputDir(outputPath);
								storePropFromGUI("conf/pattern.properties");
								btnStationGenerate.setEnabled(false);
								btnObjectInit.setEnabled(false);
								btnObjectStart.setText("Pause");
								btnObjectStop.setEnabled(true);
								btnSnapShot.setEnabled(false);
								setStartEndTimer();
							}
						}
					} else if (btnObjectStart.getText().equals("Pause")) {
						btnSnapShot.setEnabled(true);
						pauseIndoorObj();
						btnObjectStart.setText("Resume");
					} else if (btnObjectStart.getText().equals("Resume")) {
						btnSnapShot.setEnabled(false);
						pauseIndoorObj();
						btnObjectStart.setText("Pause");
					}
				}

			});

			btnObjectStop.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					stopIndoorObj();
					btnStationGenerate.setEnabled(true);
					btnObjectInit.setEnabled(true);
					btnObjectStart.setEnabled(false);
					btnObjectStop.setEnabled(false);
					btnObjectStart.setText("Start");
					btnSnapShot.setEnabled(false);
					// System.out.println(IdrObjsUtility.trajDir);
				}

			});

			btnSnapShot.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String outputPath = decideOutputPath();
					if (outputPath != null) {
						createSnapshotOutputDir(outputPath);
						snapShot(movingObjs);
					}

				}
			});
		}

		private void test() {
			String default_path = System.getProperty("user.dir"); // + "//export
			// files";
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(default_path));

			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".zip") || f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "zip Files";
				}
			};
			chooser.setFileFilter(filter);

			int result = chooser.showOpenDialog(this);
		}

		/**
		 * illegal element --> self-intersection partitions, isolated access points
		 * warning element --> isolated partitions print all the isolation partitions
		 * and access points get all the self-intersection partition, and delete them
		 * all, because they can caught an exception when decompose delete all the
		 * isolation access points, try to fix isolate partitions remember to reload all
		 * the space object, because some partitions may have been deleted
		 */
		private void clearIllegal() {
			PrintIsolatedObject.printAllIsolation(DB_WrapperLoad.partitionDecomposedT,
					DB_WrapperLoad.accessPointConnectorT);

			List<Partition> intersectionPartitions = ClearIllegal
					.calIntersectionPartitions(DB_WrapperLoad.partitionDecomposedT);
			System.out.println(intersectionPartitions);
			List<AccessPoint> isolatedAccessPoints = ClearIllegal
					.calIsolatedAccessPoints(DB_WrapperLoad.accessPointConnectorT);
			System.out.println(isolatedAccessPoints);

			Connection connection = DB_Connection.connectToDatabase("conf/moovework.properties");

			try {
				for (Partition intersectionPartition : intersectionPartitions) {
					System.out.println("delete " + intersectionPartition);
					DB_WrapperDelete.deletePartition(connection, intersectionPartition);
				}

				for (AccessPoint isolatedAccessPoint : isolatedAccessPoints) {
					System.out.println("delete " + isolatedAccessPoint);
					DB_WrapperDelete.deleteAccessPoint(connection, isolatedAccessPoint);
				}

				DB_WrapperLoad.loadALL(connection, fileChosen.getUploadId());
				for (Floor floor : DB_WrapperLoad.floorT) {
					floor.setPartitionsRTree(IdrObjsUtility.generatePartRTree(floor));
				}

				List<Partition> isolatedPartitions = ClearIllegal
						.calIsolatedPartitions(DB_WrapperLoad.partitionDecomposedT);
				for (Partition isolatedPartition : isolatedPartitions) {
					ClearIllegal.connectPossiblePartitons(isolatedPartition);
				}

				DB_WrapperLoad.loadALL(connection, fileChosen.getUploadId());
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		private void setStartEndTimer() {

			Timer startTimer = new Timer();

			try {
				IdrObjsUtility.objectGenerateStartTime = startCalendar == null
						? IdrObjsUtility.sdf.parse(movingObj.getStartTime())
						: startCalendar.getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				IdrObjsUtility.objectGenerateEndTime = endCalendar == null
						? IdrObjsUtility.sdf.parse(movingObj.getEndTime())
						: endCalendar.getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			long generatonPeriod = IdrObjsUtility.objectGenerateEndTime.getTime()
					- IdrObjsUtility.objectGenerateStartTime.getTime();

			Date startPoint = new Date(System.currentTimeMillis());
			IdrObjsUtility.startClickedTime = startPoint;
			Date endPoint = new Date(startPoint.getTime() + generatonPeriod);

			startTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					startIndoorObj();
				}
			}, startPoint);

			Timer endTimer = new Timer();

			endTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					btnObjectStop.doClick();
				}
			}, endPoint);
		}

		private void clearMapPainterActionListener() {
			for (ActionListener al : floorCombobox.getActionListeners()) {
				floorCombobox.removeActionListener(al);
			}
			for (ActionListener al : btnStationGenerate.getActionListeners()) {
				btnStationGenerate.removeActionListener(al);
			}

			for (ActionListener al : btnObjectInit.getActionListeners()) {
				btnObjectInit.removeActionListener(al);
			}

			for (ActionListener al : btnObjectStart.getActionListeners()) {
				btnObjectStart.removeActionListener(al);
			}

			for (ActionListener al : btnObjectStop.getActionListeners()) {
				btnObjectStop.removeActionListener(al);
			}

			for (ActionListener al : btnSnapShot.getActionListeners()) {
				btnSnapShot.removeActionListener(al);
			}

			for (ActionListener al : stationTypeComboBox.getActionListeners()) {
				stationTypeComboBox.removeActionListener(al);
			}
		}

		private void initUCLComboBox() {
			initStationTypeMap();
			initStationInitMap();
			initMovingObjTypeMap();
			initMovObjInitMap();
		}

		private void initStationTypeMap() {
			try {
				FileReader fileReader = new FileReader("conf/StationsTypeMap.properties");
				BufferedReader buff = new BufferedReader(fileReader);
				String line;
				stationTypeMap.clear();
				stationTypeComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					stationTypeMap.put(splitedString[0], splitedString[1]);
					stationTypeComboBox.addItem(splitedString[0]);
				}
				fileReader.close();
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void initMovingObjTypeMap() {
			try {
				FileReader fileReader = new FileReader("conf/MovingObjsTypeMap.properties");
				BufferedReader buff = new BufferedReader(fileReader);
				String line;
				movingObjTypeMap.clear();
//					movingObjectTypeComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					movingObjTypeMap.put(splitedString[0], splitedString[1]);
//						movingObjectTypeComboBox.addItem(splitedString[0]);
				}
				fileReader.close();
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void initMovObjInitMap() {
			try {
				FileReader fileReader = new FileReader("conf/MovObjsInitMap.properties");
				BufferedReader buff = new BufferedReader(fileReader);
				String line;
				movObjInitMap.clear();
//					movObjDistributerTypeComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					movObjInitMap.put(splitedString[0], splitedString[1]);
//						movObjDistributerTypeComboBox.addItem(splitedString[0]);
				}
				fileReader.close();
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void initStationInitMap() {
			try {
				FileReader fileReader = new FileReader("conf/StationsInitMap.properties");
				BufferedReader buff = new BufferedReader(fileReader);
				String line;
				stationInitMap.clear();
				stationDistriTypeComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					stationInitMap.put(splitedString[0], splitedString[1]);
					stationDistriTypeComboBox.addItem(splitedString[0]);
				}
				fileReader.close();
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			chosenFloor = (Floor) floorCombobox.getSelectedItem();

			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			AffineTransform tx = getCurrentTransform();

			Stroke Pen1, Pen2, PenDash;
			Pen1 = new BasicStroke(1.5F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
			Pen2 = new BasicStroke(3.0F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
			float dash1[] = { 5.0f };
			PenDash = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

			paintIFCObjects(chosenFloor, g2, tx, Pen1, Pen2, PenDash);

			IdrObjsUtility.paintMovingObjs(chosenFloor, g2, tx, Pen1, movingObjs, new Color(116, 124, 155));
			IdrObjsUtility.paintMovingObjs(chosenFloor, g2, tx, Pen1, destMovingObjs, new Color(116, 124, 155));
			IdrObjsUtility.paintStations(chosenFloor, g2, tx, Pen1, new Color(245, 166, 35, 120));

			repaint(); // fix the animation issues
			// Point2D.Double point1 = new Point2D.Double(343, 250);
			// DstMovingObj dstMovingObj = new
			// DstMovingObj(DB_WrapperLoad.floorT.get(3), point1);
			// dstMovingObj.setCurrentPartition(IdrObjsUtility.findPartitionForPoint(DB_WrapperLoad.floorT.get(1),
			// point1));
			// Point2D.Double point2 = new Point2D.Double(405, 180);
			// dstMovingObj.setCurDestPoint(point2);
			// dstMovingObj.setCurDestFloor(DB_WrapperLoad.floorT.get(2));
			// // paintTrajectory(g2, tx, Pen2, dstMovingObj, chosenFloor);
		}

		private void paintIFCObjects(Floor chosenFloor, Graphics2D g2, AffineTransform tx, Stroke Pen1, Stroke Pen2,
				Stroke PenDash) {
			empty = false;

			if (chosenFloor == null) {
				empty = true;

			} else if (chosenFloor.getPartitions().isEmpty()) {
				empty = true;
			}

			if (!empty) {
				paintPartitions(g2, tx, Pen1, Pen2);
				paintAccessPoints(g2, tx, PenDash, Pen1);
				paintConnectors(g2, tx, Pen1, PenDash);
			} else {
				g2.drawString("No partitions on this floor!", 100, 300);
			}
		}

		private void paintPartitions(Graphics2D g2, AffineTransform tx, Stroke Pen1, Stroke Pen2) {
			partitionsMap.clear();
			for (Partition r : chosenFloor.getPartsAfterDecomposed()) {
				Polygon2D.Double po = r.getPolygon2D();

				Path2D poNew = (Path2D) tx.createTransformedShape(po);
				partitionsMap.put(poNew, r);

				// Highlight color
				if (r == selectedPart) {
					g2.setColor(new Color(74, 144, 226));
				} else {
					g2.setColor(new Color(249, 248, 246));
				}
				g2.fill(poNew);

				// Background image
				if (r == selectedPart) {
					g2.setColor(new Color(173, 173, 173));
					g2.setStroke(Pen1);
				} else {
					g2.setColor(new Color(173, 173, 173));
					g2.setStroke(Pen1);
				}
				g2.draw(poNew);

				paintNameOnPart(r, g2, poNew);
			}
			if (selectedPart != null) {
				paintConnectedPartitions(g2, tx, Pen1, Pen2);
			}
		}

		private void paintNameOnPart(Partition r, Graphics2D g2, Path2D poNew) {
			if (r.getPolygonGIS().numPoints() < 6) {

				String s = r.getName();

				FontRenderContext frc = g2.getFontRenderContext();
				Font font = g2.getFont().deriveFont(10f);
				g2.setColor(Color.black);
				g2.setFont(font);
				float sw = (float) font.getStringBounds(s, frc).getWidth();
				LineMetrics lm = font.getLineMetrics(s, frc);
				float sh = lm.getAscent() + lm.getDescent();

				float sx = (float) (poNew.getBounds2D().getX() + (poNew.getBounds2D().getWidth() - sw) / 2);
				float sy = (float) (poNew.getBounds2D().getY() + (poNew.getBounds2D().getHeight() + sh) / 2
						- lm.getDescent());
				g2.drawString(s, sx, sy);
			}
		}

		private void paintConnectedPartitions(Graphics2D g2, AffineTransform tx, Stroke Pen1, Stroke Pen2) {
			for (Partition part : selectedPart.getConParts()) {
				if (part.getFloor() != selectedPart.getFloor()) {
					continue;
				}
				Polygon2D.Double po = part.getPolygon2D();
				Path2D poNew = (Path2D) tx.createTransformedShape(po);

				g2.setColor(new Color(203, 209, 216));
				g2.fill(poNew);

				g2.setColor(new Color(173, 173, 173));
				g2.setStroke(Pen1);
				g2.draw(poNew);

				paintNameOnPart(part, g2, poNew);

			}
		}

		private void paintAccessPoints(Graphics2D g2, AffineTransform tx, Stroke PenDash, Stroke Pen1) {
			accesspointsMap.clear();
			for (AccessPoint ap : chosenFloor.getAccessPoints()) {

				Path2D clickBox = (Path2D) tx.createTransformedShape(ap.getLine2DClickBox());

				Path2D newLine = (Path2D) tx.createTransformedShape(ap.getLine2D());

				accesspointsMap.put(clickBox, ap);

				if (ap == selectedAP) {
					g2.setColor(new Color(74, 144, 226));
					g2.setStroke(Pen1);
				} else if (ap.getApType().equals(2)) {
					g2.setBackground(new Color(116, 124, 155));
					g2.setColor(new Color(248, 231, 28));
					g2.setStroke(PenDash);
				} else {
					g2.setColor(new Color(144, 19, 254));
					g2.setStroke(Pen1);
				}
				g2.draw(newLine);
			}
		}

		private void paintConnectors(Graphics2D g2, AffineTransform tx, Stroke Pen1, Stroke penDash) {
			connsMap.clear();
			g2.setStroke(penDash);
			for (Connector c : chosenFloor.getConnectors()) {
				Point2D.Double point1 = c.getLocation2D();
				Point2D.Double point2 = c.getUpperLocation2D();
				double x, y, w, h;
				if (point2 == null) {
					x = point1.getX();
					y = point1.getY();
					w = 3;
					h = 3;
				} else {
					x = Math.min(point1.getX(), point2.getX());
					y = Math.min(point1.getY(), point2.getY());
					w = Math.abs(point1.getX() - point2.getX());
					h = Math.abs(point1.getY() - point2.getY());
				}
				Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, w + 3, h + 3);
				Path2D newRect = (Path2D) tx.createTransformedShape(rectangle);
				connsMap.put(newRect, c);
				if (selectedCon == c) {
					g2.setColor(new Color(74, 144, 226));

					for (Partition part : selectedCon.getPartitions()) {
						if (part.getFloor() != selectedCon.getFloor()) {
							continue;
						}
						Polygon2D.Double po = part.getPolygon2D();
						Path2D poNew = (Path2D) tx.createTransformedShape(po);

						g2.setColor(new Color(203, 209, 216));
						g2.fill(poNew);

						g2.setColor(new Color(173, 173, 173));
						g2.setStroke(Pen1);
						g2.draw(poNew);

						paintNameOnPart(part, g2, poNew);

					}
					g2.setColor(new Color(144, 19, 254));

				} else {
					g2.setColor(new Color(49, 76, 206));
				}
				g2.setStroke(penDash);
				g2.draw(newRect);
			}

			for (Connector c : DB_WrapperLoad.connectorT) {
				if (c.getUpperFloor() == chosenFloor) {
					Point2D.Double point1 = c.getLocation2D();
					Point2D.Double point2 = c.getUpperLocation2D();
					double x, y, w, h;
					if (point2 == null) {
						x = point1.getX();
						y = point1.getY();
						w = 3;
						h = 3;
					} else {
						x = Math.min(point1.getX(), point2.getX());
						y = Math.min(point1.getY(), point2.getY());
						w = Math.abs(point1.getX() - point2.getX());
						h = Math.abs(point1.getY() - point2.getY());
					}
					Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, w + 3, h + 3);
					Path2D newRect = (Path2D) tx.createTransformedShape(rectangle);
					connsMap.put(newRect, c);
					if (selectedCon == c) {
						g2.setColor(new Color(74, 144, 226));
					} else {
						g2.setColor(new Color(49, 188, 77));

					}
					g2.draw(newRect);
				}
			}
		}

//		private void updateSelectPartsList() {
//			connectedPartsModel.clear();
//			if (selectedPart != null) {
//				txtselectedNameField.setText(selectedPart.getName() + " AND ID: " + selectedPart.getItemID());
//				for (Partition partition : selectedPart.getConParts()) {
//					connectedPartsModel.addElement(partition);
//				}
//			} else if (selectedAP != null) {
//				txtselectedNameField.setText(selectedAP.getName() + " AND ID: " + selectedAP.getItemID());
//				for (Partition p : selectedAP.getPartitions()) {
//					connectedPartsModel.addElement(p);
//				}
//			} else if (selectedCon != null) {
//				txtselectedNameField.setText(selectedCon.getName() + " AND ID: " + selectedCon.getItemID());
//				for (Partition p : selectedCon.getPartitions()) {
//
//					connectedPartsModel.addElement(p);
//				}
//
//			} else {
//
//			}
//		}

		private AffineTransform getCurrentTransform() {

			AffineTransform tx = new AffineTransform();

			double centerX = (double) getWidth() / 2;
			double centerY = (double) getHeight() / 2;

			tx.translate(centerX, centerY);
			tx.scale(zoom, zoom);
			tx.translate(currentX - centerX, currentY - centerY);

			return tx;
		}

		private void incrementZoom(double amount) {
			zoom += amount;
			zoom = Math.max(0.00001, zoom);
			repaint();
		}

		private Point2D getTranslatedPoint(double panelX, double panelY) {

			AffineTransform tx = getCurrentTransform();
			Point2D point2d = new Point2D.Double(panelX, panelY);
			try {
				return tx.inverseTransform(point2d, null);
			} catch (NoninvertibleTransformException ex) {
				ex.printStackTrace();
				return null;
			}
		}

		private void loadFloorChooser() {
			String floor_globalid = null;
			if (floorCombobox.getSelectedItem() != null) {
				Floor f = (Floor) floorCombobox.getSelectedItem();
				floor_globalid = f.getGlobalID();
			}
			floorCombobox.removeAllItems();
			for (int i = 0; i < DB_WrapperLoad.floorT.size(); i++) {
				Floor f = DB_WrapperLoad.floorT.get(i);
				floorCombobox.addItem(f);
			}
			if (floor_globalid != null) {
				for (int i = 0; i < floorCombobox.getModel().getSize(); i++) {
					if (floorCombobox.getModel().getElementAt(i).getGlobalID().equals(floor_globalid)) {
						floorCombobox.setSelectedItem(floorCombobox.getModel().getElementAt(i));
						break;
					}
				}
			}
		}

		private void generateStations() {
			for (Floor floor : DB_WrapperLoad.floorT) {
				floor.getStations().clear();
			}
			IndoorObjsFactory initlizer = new IndoorObjsFactory();
			for (Floor floor : DB_WrapperLoad.floorT) {
				ArrayList<Station> stations = new ArrayList<Station>();
				initlizer.generateStationsOnFloor(floor, stations);
				floor.setStations(stations);
				floor.setStationsRTree(IdrObjsUtility.generateStationRTree(floor.getStations()));
			}
		}

		private void exportEnvironment(String envDir) {

			Date date = new Date(System.currentTimeMillis());
			String time = IdrObjsUtility.dir_sdf.format(date);

			String currentPath = envDir + "//" + time;
			new File(currentPath).mkdirs();

			File file = null;
			FileOutputStream outStr = null;
			BufferedOutputStream buff = null;

			String floor_outputPath = currentPath + "//Floors" + ".csv";
			try {
				file = new File(floor_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				String header = "ITEMID" + "," + "GLOBALID" + "," + "FLOORNAME" + "\n";
				buff.write(header.getBytes());
				for (Floor floor : DB_WrapperLoad.floorT) {
					buff.write((floor.writeFloor()).getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String accessPoint_outputPath = currentPath + "//AccessPoints" + ".csv";
			try {
				file = new File(accessPoint_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				String header = "ITEMID" + "," + "GLOBALID" + "," + "X" + "," + "Y" + "\n";
				buff.write(header.getBytes());
				for (Floor floor : DB_WrapperLoad.floorT) {
					for (AccessPoint ap : floor.getAccessPoints())
						buff.write((ap.writeAccessPoint()).getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String parition_outputPath = currentPath + "//Partitions" + ".csv";
			try {
				file = new File(parition_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
//		            String header = "\"itemId\"" + "," + "\"globalId\"" + "," + "\"position\"" + "\n";
				String header = "FLOORID" + "," + "ITEMID" + "," + "NAME" + "," + "POLYGON" + "\n";
				buff.write(header.getBytes());
				for (Floor floor : DB_WrapperLoad.floorT) {
					for (Partition par : floor.getPartitions())
						buff.write((par.writePartition(floor)).getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String connector_outputPath = currentPath + "//Connectors" + ".csv";
			try {
				file = new File(connector_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				String header = "ITEMID" + "," + "GLOBALID" + "," + "X" + "," + "Y" + "\n";
				buff.write(header.getBytes());
				for (Floor floor : DB_WrapperLoad.floorT) {
					for (Connector connector : floor.getConnectors())
						buff.write((connector.writeAccessPoint()).getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String connectivity_outputPath = currentPath + "//Connectivity" + ".csv";
			try {
				file = new File(connectivity_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				String header = "ITEMID" + "," + "GLOBALID" + "," + "NAME" + "," + "FIRSTPARTITIONGLOBALID" + ","
						+ "SECONDPARTITIONGLOBALID" + "," + "ACCESSRULE" + "," + "X" + "," + "Y" + "\n";
				buff.write(header.getBytes());
				for (Connectivity connectivity : DB_WrapperLoad.connectivityT) {
					buff.write((connectivity.writeConnectivity()).getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void exportMovingPatternsConfiguration(String outputPath, String time) {

			// System.out.println(outputPath);
			String mo_configuration_outputPath = outputPath + "//Moving_Object_Configuration_" + time + ".txt";
			// System.out.println(mo_configuration_outputPath);
			try {
				File file = new File(mo_configuration_outputPath);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStr = new FileOutputStream(file);
				BufferedOutputStream buff = new BufferedOutputStream(outStr);
				String configure = "Moving Object Type=" + persons[0].getMovingObjectType() + "\n";
				configure = configure + "Initial Distribution=" + persons[0].getInitialDistribution() + "\n";
				configure = configure + "Maximum Object Number in a Partition=" + persons[0].getObjectNumber() + "\n";
				configure = configure + "Maximum Life Span(s)=" + persons[0].getLifeSpan() + "\n";
				configure = configure + "Maximum Step Length(m)=" + persons[0].getMaxStepLength() + "\n";
				configure = configure + "Move Rate(ms)=" + persons[0].getMoveRate() + "\n";
				configure = configure + "Generation Period=" + movingObj.getStartTime() + "-" + movingObj.getEndTime()
						+ "\n";
				buff.write(configure.getBytes());
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void exportStations(String stationDir) {

			Date date = new Date(System.currentTimeMillis());
			String time = IdrObjsUtility.dir_sdf.format(date);

			// IdrObjsUtility.createOutputDir();

			String station_outputPath = stationDir + "//Devices_" + time + ".txt";
			String station_configuration_outputPath = stationDir + "//Device_Configuration_" + time + ".txt";
			try {
				File file = new File(station_outputPath);
				file.createNewFile();
				FileOutputStream outStr = new FileOutputStream(file);
				BufferedOutputStream buff = new BufferedOutputStream(outStr);
				String comments = "deviceId" + "\t" + "floorId" + "\t" + "partitionId" + "\t" + "location_x" + "\t"
						+ "location_y" + "\n";
				buff.write(comments.getBytes());
				boolean flag = false;
				Station sampled_station = null;
				IdrObjsUtility.allStations = new Hashtable<Integer, Station>();
				for (Floor floor : DB_WrapperLoad.floorT) {
					for (Station station : floor.getStations()) {
						IdrObjsUtility.allStations.put(station.getId(), station);
						if (!flag) {
							sampled_station = station;
							flag = true;
						}
						buff.write(station.toString().getBytes());
					}
				}
				buff.close();

				if (sampled_station != null) {
					file = new File(station_configuration_outputPath);
					file.createNewFile();
					outStr = new FileOutputStream(file);
					buff = new BufferedOutputStream(outStr);
					String configure = "Device Type=" + stationTypeComboBox.getSelectedItem().toString() + "\n";
					configure = configure + "Deployment Model=" + stationDistriTypeComboBox.getSelectedItem().toString()
							+ "\n";
					configure = configure + "Maximum Device Number in a Partition=" + txtStationMaxNumInPart.getText()
							+ "\n";
					configure = configure + "Maximum Device Number in 100 m^2=" + txtStationMaxNumInArea.getText()
							+ "\n";
					configure = configure + "Detection Range(m)=" + txtScanRange.getText() + "\n";
					configure = configure + "Detection Frequency(ms)=" + txtScanRate.getText() + "\n";
					buff.write(configure.getBytes());
					buff.close();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private String createEnvironmentOutputDir(String outputPath) {
			// TODO Auto-generated method stub

			File dir = new File(outputPath);

			File spaceDir = new File(outputPath + "//indoor enviroment");

			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			if (!spaceDir.exists() && !spaceDir.isDirectory()) {
				spaceDir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			return spaceDir.getPath();

		}

		private String createStationOutputDir(String outputPath) {
			// TODO Auto-generated method stub

			File dir = new File(outputPath);

			File stationDir = new File(outputPath + "//indoor devices");

			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			if (!stationDir.exists() && !stationDir.isDirectory()) {
				stationDir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			return stationDir.getPath();

		}

		private void createMovingObjectOutputDir(String outputPath) {
			// TODO Auto-generated method stub

			Date date = new Date(System.currentTimeMillis());
			String time = IdrObjsUtility.dir_sdf.format(date);

			File dir = new File(outputPath);

			File rssiDir = new File(outputPath + "//raw rssi");
			File trajDir = new File(outputPath + "//raw trajectory");

			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			IdrObjsUtility.outputDir = outputPath;
			IdrObjsUtility.rssiDir = rssiDir.getPath() + "//" + time;
			IdrObjsUtility.trajDir = trajDir.getPath() + "//" + time;
			exportMovingPatternsConfiguration(rssiDir.getPath(), time);

			if (!rssiDir.exists() && !rssiDir.isDirectory() && chckbxTracking.isSelected()) {
				rssiDir.mkdirs();
			}
			File cur_rssiDir = new File(IdrObjsUtility.rssiDir);
			if (!cur_rssiDir.exists() && !cur_rssiDir.isDirectory() && chckbxTracking.isSelected()) {
				cur_rssiDir.mkdirs();
			}

			if (!trajDir.exists() && !trajDir.isDirectory() && chckbxTrajectory.isSelected()) {
				trajDir.mkdirs();
				new File(IdrObjsUtility.trajDir).mkdirs();
			}
			File cur_trajDir = new File(IdrObjsUtility.trajDir);
			if (!cur_trajDir.exists() && !cur_trajDir.isDirectory() && chckbxTracking.isSelected()) {
				cur_trajDir.mkdirs();
			}

			return;

		}

		private void createSnapshotOutputDir(String outputPath) {
			// TODO Auto-generated method stub

			File dir = new File(outputPath);

			File snapshotDir = new File(outputPath + "//snapshot");

			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			snapshotDir.mkdirs();

			IdrObjsUtility.snapshotDir = snapshotDir.getPath();
			//
			// new File(IdrObjsUtility.snapshotDir).mkdirs();

			// positionDir.mkdirs();

			return;

		}

		private void generateMovingObjs() {
			movingObjs.clear();
			IndoorObjsFactory initlizer = new IndoorObjsFactory();
			for (Floor floor : DB_WrapperLoad.floorT) {
				initlizer.generateMovingObjsOnFloor(floor, movingObjs);
			}
			// PropLoader propLoader = new PropLoader();
			// propLoader.loadProp("conf/factory.properties");
			// movingObjs = (ArrayList)DB_WrapperLoad.floorT
			// .stream()
			// .map(floor ->
			// IndoorObjectFactory.createMovingObjectsOnFloor(floor,
			// propLoader.getMovingObjDistributerType()))
			// .flatMap(Collection::stream)
			// .collect(Collectors.toList());
			setMovingObjsInitTime();
		}

		private void setMovingObjsInitTime() {
			movingObjs.forEach(movingObj -> {
				movingObj.setInitMovingTime(calGaussianTime());
			});
		}

		private long calGaussianTime() {
			Random random = new Random();
			try {
				IdrObjsUtility.objectGenerateStartTime = startCalendar == null
						? IdrObjsUtility.sdf.parse(movingObj.getStartTime())
						: startCalendar.getTime();
				IdrObjsUtility.objectGenerateEndTime = endCalendar == null
						? IdrObjsUtility.sdf.parse(movingObj.getEndTime())
						: endCalendar.getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			long startTime = IdrObjsUtility.objectGenerateStartTime.getTime();
			long endTime = IdrObjsUtility.objectGenerateEndTime.getTime();
			long middle = (long) ((endTime - startTime) / 2.0 + startTime);
			long error = (long) ((endTime - startTime) * 0.5);
			long gaussianTime = (long) (middle + random.nextGaussian() * error);
			if (gaussianTime < startTime) {
				return startTime;
			} else if (gaussianTime > endTime) {
				return endTime;
			} else {
				return gaussianTime;
			}
		}

		private void snapShot(ArrayList<MovingObj> movingObjs) {

			try {

				String time = IdrObjsUtility.dir_sdf.format(new Date(IdrObjsUtility.objectGenerateStartTime.getTime()
						+ (System.currentTimeMillis() - IdrObjsUtility.startClickedTime.getTime())));

				// create snap shot file
				File file = new File(IdrObjsUtility.snapshotDir + "//snapshot_" + time + ".txt");
				file.createNewFile();
				FileOutputStream outStr = new FileOutputStream(file);
				BufferedOutputStream buff = new BufferedOutputStream(outStr);

				// record snap shot data
				for (MovingObj movingObj : movingObjs) {
					buff.write((movingObj.getId() + "\t").getBytes());
					buff.write((movingObj.getCurrentLocation().getX() + "\t" + movingObj.getCurrentLocation().getY()
							+ "\n").getBytes());
					int packIndex = 1;
					movingObj.calRSSI();
					ArrayList<Pack> packs = movingObj.getPackages();
					for (Pack pack : packs) {
						String packInfo = packIndex + "\t" + pack.toString() + "\n";
						buff.write(packInfo.getBytes());
						packIndex++;
					}
					buff.write("\n".getBytes());
				}

				buff.flush();
				buff.close();
				JOptionPane.showMessageDialog(this, "Extracting snapshot is done!", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void storePropFromGUI(String propName) {
			Properties props = new Properties();

			String stationTypeSimple = stationTypeComboBox.getSelectedItem().toString();
			String stationType = stationTypeMap.get(stationTypeSimple);
			props.setProperty("stationType", stationType);
			String stationDistributerTypeSimple = stationDistriTypeComboBox.getSelectedItem().toString();
			String stationDistributerType = stationInitMap.get(stationDistributerTypeSimple);
			props.setProperty("stationDistributerType", stationDistributerType);
			String stationMaxNumInPart = txtStationMaxNumInPart.getText();
			props.setProperty("stationMaxNumInPart", stationMaxNumInPart);

			String stationNumArea = txtStationMaxNumInArea.getText();
			props.setProperty("stationNumArea", stationNumArea);
			String scanRange = txtScanRange.getText();
			props.setProperty("stationScanRange", scanRange);
			String stationScanRate = txtScanRate.getText();
			props.setProperty("stationScanRate", stationScanRate);

			Station.setScanRange(Double.parseDouble(scanRange));
			Station.setScanRate(Integer.parseInt(stationScanRate));

			if (persons != null) {

				String movingObjTypeSimple = persons[0].getMovingObjectType();
				String movingObjType = movingObjTypeMap.get(movingObjTypeSimple);
				props.setProperty("movingObjType", movingObjType);
				String movObjDistriTypeSimple = persons[0].getInitialDistribution();
				String movObjDistriType = movObjInitMap.get(movObjDistriTypeSimple);
				props.setProperty("movingObjDistributerType", movObjDistriType);
				String maxMovingNumInPart = Integer.toString(persons[0].getObjectNumber());
				props.setProperty("movingObjMaxNumInPart", maxMovingNumInPart);
				String maxStepLength = Double.toString(persons[0].getMaxStepLength());
				props.setProperty("movingObjMaxStepLength", maxStepLength);
				String moveRate = Integer.toString(persons[0].getMoveRate());
				props.setProperty("movingObjMoveRate", moveRate);
				String movingObjMaxLifeSpan = Integer.toString(persons[0].getLifeSpan());
				props.setProperty("movingObjMaxLifeSpan", movingObjMaxLifeSpan);

				MovingObj.setScanRange(Double.parseDouble(scanRange));
				MovingObj.setMaxStepLength(persons[0].getMaxStepLength());
				MovingObj.setMoveRate(persons[0].getMoveRate());
				MovingObj.setMaxSpeed(persons[0].getMaxStepLength() / ((persons[0].getMoveRate() + 0.0) / 1000));
			}

			try {
				FileOutputStream out = new FileOutputStream(propName);
				props.store(out, null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void startIndoorObj() {
			// IdrObjsUtility.createOutputDir();
			IdrObjsUtility.movingObjsTest(movingObjs);

			destMovingObjs.clear();
			Floor floor1 = DB_WrapperLoad.floorT.get(2);
			Floor floor2 = DB_WrapperLoad.floorT.get(2);
			IdrObjsUtility.DestMovingObjTest2(floor1, floor2, destMovingObjs);
		}

		private void stopIndoorObj() {
			for (MovingObj movingObj : movingObjs) {
				movingObj.setArrived(true);
				if (movingObj instanceof RegularMultiDestCustomer) {
					((RegularMultiDestCustomer) movingObj).setFinished(true);
				}
			}
			for (MovingObj destMoving : destMovingObjs) {
				DstMovingObj destMovingObj = (DstMovingObj) destMoving;
				destMovingObj.setArrived(true);
			}
			movingObjs.clear();
			destMovingObjs.clear();

			for (Floor floor : DB_WrapperLoad.floorT) {
				floor.getStations().clear();
			}

			JOptionPane.showMessageDialog(this, "Generating Moving Object Data is done!", "Information",
					JOptionPane.INFORMATION_MESSAGE);

		}

		private void pauseIndoorObj() {
			for (MovingObj movingObj : movingObjs) {

				movingObj.changeFlag();
				// pauseBut.setText("resume");
				if (movingObj.getPauseFlag() == false) {
					movingObj.resumeThread();
					// pauseBut.setText("pause");
				}
			}

			for (MovingObj movingObj : destMovingObjs) {
				DstMovingObj destMovingObj = (DstMovingObj) movingObj;
				destMovingObj.changeFlag();
				if (destMovingObj.getPauseFlag() == false) {
					destMovingObj.resumeThread();
				}
			}
		}

		private void loadPropFromFile(String propName) {

			Properties props = new Properties();
			FileInputStream in;
			try {
				in = new FileInputStream(propName);
				props.load(in);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String stationMaxNumInPart = props.getProperty("stationMaxNumInPart");
			txtStationMaxNumInPart.setText(stationMaxNumInPart);
			String stationNumArea = props.getProperty("stationNumArea");
			txtStationMaxNumInArea.setText(stationNumArea);
			String scanRange = props.getProperty("stationScanRange");
			txtScanRange.setText(scanRange);
			String stationScanRate = props.getProperty("stationScanRate");
			txtScanRate.setText(stationScanRate);

//				String maxMovingNumInPartition = props.getProperty("movingObjMaxNumInPart");
//				txtMaxMovObjNumInPart.setText(maxMovingNumInPartition);
//				String maxStepLength = props.getProperty("movingObjMaxStepLength");
//				txtMaxStepLength.setText(maxStepLength);
//				String moveRate = props.getProperty("movingObjMoveRate");
//				txtMoveRate.setText(moveRate);
//				String movObjMaxLifeSpan = props.getProperty("movingObjMaxLifeSpan");
//				txtMaximumLifeSpan.setText(movObjMaxLifeSpan);

//			txtStartTime.setText(IdrObjsUtility.sdf.format(System.currentTimeMillis()));
//			txtEndTime.setText(IdrObjsUtility.sdf.format(System.currentTimeMillis() + 10 * 60 * 1000));
		}

		private class MovingAdapter extends MouseAdapter {
			private Point startDrag;

			public void mouseWheelMoved(MouseWheelEvent e) {
				previousX = e.getX();
				previousY = e.getY();
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					incrementZoom(1.0 * (double) e.getWheelRotation());
				}
			}

			public void mousePressed(MouseEvent e) {
				previousX = e.getX();
				previousY = e.getY();

				startDrag = new Point(e.getX(), e.getY()); // First point
				if (!empty) {
					selectedPart = null;
					selectedAP = null;

					for (Entry<Path2D, Partition> mapping : partitionsMap.entrySet()) {
						if (mapping.getKey().contains(startDrag)) {
							selectedPart = mapping.getValue();
							selectedAP = null;
							selectedCon = null;
							break;
						}
					}

					for (Entry<Path2D, AccessPoint> mapping : accesspointsMap.entrySet()) {
						if (mapping.getKey().contains(startDrag)) {
							selectedAP = mapping.getValue();
							selectedPart = null;
							selectedCon = null;
							break;
						}
					}

					for (Entry<Path2D, Connector> mapping : connsMap.entrySet()) {
						if (mapping.getKey().contains(startDrag)) {
							selectedCon = mapping.getValue();
							selectedPart = null;
							selectedAP = null;
							break;
						}
					}

					connectedPartsModel.clear();
				}
				repaint();
			}

			public void mouseDragged(MouseEvent e) {

				Point2D adjPreviousPoint = getTranslatedPoint(previousX, previousY);
				Point2D adjNewPoint = getTranslatedPoint(e.getX(), e.getY());

				double newX = adjNewPoint.getX() - adjPreviousPoint.getX();
				double newY = adjNewPoint.getY() - adjPreviousPoint.getY();

				previousX = e.getX();
				previousY = e.getY();

				currentX += newX;
				currentY += newY;

				repaint();
			}

			public void mouseReleased(MouseEvent e) {
			}
		}
	}

	// Id Table Renderer Class
	private class IdTableRenderer extends DefaultTableCellRenderer {
		private final Map<String, Color> colorMap = new HashMap<>();

		public IdTableRenderer() {
			// TODO Auto-generated constructor stub
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setBackground(null);
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			getColorForCell(row, column).ifPresent(this::setBackground);
			return this;
		}

		public void setColorForCell(int row, int col, Color color) {
			colorMap.put(row + ":" + col, color);
		}

		public Optional<Color> getColorForCell(int row, int col) {
			return Optional.ofNullable(colorMap.get(row + ":" + col));
		}
	}
}
