package cn.edu.zju.db.datagen.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
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
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import cn.edu.zju.db.datagen.algorithm.Algorithm;
import cn.edu.zju.db.datagen.algorithm.FPT;
import cn.edu.zju.db.datagen.algorithm.PXM;
import cn.edu.zju.db.datagen.algorithm.TRI;
import cn.edu.zju.db.datagen.database.DB_Connection;
import cn.edu.zju.db.datagen.database.DB_FileUploader;
import cn.edu.zju.db.datagen.database.DB_Import;
import cn.edu.zju.db.datagen.database.DB_WrapperDelete;
import cn.edu.zju.db.datagen.database.DB_WrapperLoad;
import cn.edu.zju.db.datagen.database.spatialobject.AccessPoint;
import cn.edu.zju.db.datagen.database.spatialobject.Connectivity;
import cn.edu.zju.db.datagen.database.spatialobject.Connector;
import cn.edu.zju.db.datagen.database.spatialobject.Floor;
import cn.edu.zju.db.datagen.database.spatialobject.Partition;
import cn.edu.zju.db.datagen.database.spatialobject.UploadObject;
import cn.edu.zju.db.datagen.gui.util.InteractionState;
import cn.edu.zju.db.datagen.gui.util.calendar.JTimeChooser;
import cn.edu.zju.db.datagen.indoorobject.IndoorObjsFactory;
import cn.edu.zju.db.datagen.indoorobject.movingobject.DstMovingObj;
import cn.edu.zju.db.datagen.indoorobject.movingobject.MovingObj;
import cn.edu.zju.db.datagen.indoorobject.movingobject.MovingObjResponse;
import cn.edu.zju.db.datagen.indoorobject.movingobject.RegularMultiDestCustomer;
import cn.edu.zju.db.datagen.indoorobject.station.Pack;
import cn.edu.zju.db.datagen.indoorobject.station.Station;
import cn.edu.zju.db.datagen.indoorobject.utility.IdrObjsUtility;
import cn.edu.zju.db.datagen.json.DateSerializer;
import cn.edu.zju.db.datagen.json.TimeDeserializer;
import cn.edu.zju.db.datagen.json.TimeSerializer;
import cn.edu.zju.db.datagen.machine.Machine;
import cn.edu.zju.db.datagen.spatialgraph.D2DGraph;
import cn.edu.zju.db.datagen.trajectory.Trajectory;
import diva.util.java2d.Polygon2D;
import javax.swing.ListModel;
import javax.swing.JRadioButton;

public class Home extends JApplet {

	private static String lastSelectedFileName = null;
	
	private JFrame frmTrajectoryGenerator;
	private JTextField txtselectedNameField;
	private JTextField txtStationMaxNumInPart;
	private JTextField txtStationMaxNumInArea;
	private JTextField txtScanRange;
	private JTextField txtScanRate;
	private JTextField txtMaxMovObjNumInPart;
	private JTextField txtMaximumLifeSpan;
	private JTextField txtMaxStepLength;
	private JTextField txtMoveRate;
	private JTextField txtStartTime;
	private JTextField txtEndTime;
	private JTextField txtRssiInputPath;

	private JButton btnImport;
	private JButton btnDeleteFile;
	private JButton btnView;
	private JButton btnDecompAll;
	private JButton btnDeleteNav;
	private JButton btnDeleteFloor;
	private JButton btnDeleteEntity;
	private JButton btnStationGenerate;
	private JButton btnObjectInit;
	private JButton btnObjectStart;
	private JButton btnObjectStop;
	private JButton btnSnapShot;
	private JButton btnPositionGenerate;
	private JButton btnMachineUpload;
	private JButton btnMovingObjUpload;
	private JButton btnShow;
	
	private JPanel filePanel;
	private JPanel mapPanel;
	private JPanel mapVisualPanel;
	private JPanel controlPanel;
	private JPanel dbiPanel;
	private JPanel uclPanel;
	private JPanel movingObjectPanel;
	private JPanel playPanel;
	private JPanel stationPanel;
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

	public static JTextArea txtConsoleArea;
	private JTextArea positionPropertiesArea;

	private JCheckBox chckbxPositiongData;
	private JCheckBox chckbxTrajectory;
	private JCheckBox chckbxTracking;
	private JCheckBox chckbxPositioningDevice;
	private JCheckBox chckbxEnvironment;
	
	private ButtonGroup visualButtonGroup;
	
	private JRadioButton chckbxPath;
	private JRadioButton chckbxHeatMap;

	private JScrollPane scrollPaneConsole;
	private JScrollPane positionPropertiesScrollPane;
	private JScrollPane scrollPanePart;
	private JScrollPane movingObjectScroll;
	private JScrollPane machineScroll;

	private JTabbedPane tabbedVITAPane;

	private JComboBox<String> stationTypeComboBox;
	private JComboBox<String> stationDistriTypeComboBox;
	private JComboBox<String> movingObjectTypeComboBox;
	private JComboBox<String> movObjDistributerTypeComboBox;
	private JComboBox<String> positionAlgorithmComboBox;
	private JComboBox<UploadObject> fileComboBox;
	private JComboBox<UploadObject> objectComboBox;
	private JComboBox<UploadObject> machineComboBox;
	private JComboBox<Floor> floorCombobox;
	private JComboBox<Floor> navCombobox;
	private Calendar startCalendar;
	private Calendar endCalendar;

	private MapPainter mapPainter;
	private MapVisualPainter mapVisualPainter;
	private Connection connection = null;
	private Floor chosenFloor = null;
	private Partition selectedPart = null;
	private Partition mapPart = null;
	private AccessPoint selectedAP = null;
	private Connector selectedCon = null;
	private UploadObject fileChosen = null;
	private JList<Partition> connectedPartsList;
	private DefaultListModel<Partition> connectedPartsModel;
	private ArrayList<UploadObject> files = null;
	private ArrayList<MovingObj> movingObjs = new ArrayList<MovingObj>();
	private ArrayList<MovingObj> destMovingObjs = new ArrayList<MovingObj>();
	private ArrayList<Trajectory> trajectories;
	private MovingObjResponse[] persons;
	private Machine[] machines;

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
	private JComboBox textField_7;
	private JTextField textField;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
	private void initialize() {
		frmTrajectoryGenerator = new JFrame();
		frmTrajectoryGenerator.setTitle("Trajectory Generator");
		frmTrajectoryGenerator.setBounds(100, 100, 940, 935);
		frmTrajectoryGenerator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTrajectoryGenerator.getContentPane().setLayout(null);
		
		JPanel filePanel = new JPanel();
		filePanel.setBounds(0, 0, 924, 45);
		frmTrajectoryGenerator.getContentPane().add(filePanel);
		filePanel.setLayout(null);
		
		fileComboBox = new JComboBox<UploadObject>();
		fileComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		fileComboBox.setBackground(Color.WHITE);
		fileComboBox.setBounds(12, 10, 225, 23);
		filePanel.add(fileComboBox);
		
		btnImport = new JButton("Import");
		btnImport.setBackground(Color.WHITE);
		btnImport.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnImport.setBounds(249, 10, 97, 23);
		filePanel.add(btnImport);
		
		btnDeleteFile = new JButton("Clear");
		btnDeleteFile.setBackground(Color.WHITE);
		btnDeleteFile.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDeleteFile.setBounds(358, 10, 97, 23);
		filePanel.add(btnDeleteFile);
		
		btnView = new JButton("Load");
		btnView.setBackground(Color.WHITE);
		btnView.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnView.setBounds(467, 10, 97, 23);
		filePanel.add(btnView);
		
		btnDecompAll = new JButton("Decompose");
		btnDecompAll.setBackground(Color.WHITE);
		btnDecompAll.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDecompAll.setBounds(576, 10, 110, 23);
		filePanel.add(btnDecompAll);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Dialog", Font.PLAIN, 11));
		tabbedPane.setBounds(0, 43, 924, 853);
		frmTrajectoryGenerator.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Training", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblMachineConfiguration = new JLabel("Machine Configuration");
		lblMachineConfiguration.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblMachineConfiguration.setBounds(504, 10, 186, 23);
		panel.add(lblMachineConfiguration);
		
		JButton btnMachineClear = new JButton("Clear");
		btnMachineClear.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnMachineClear.setBackground(Color.WHITE);
		btnMachineClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MachinesWindow panel = new MachinesWindow();
				panel.main(null);
			}
		});
		btnMachineClear.setBounds(810, 76, 97, 23);
		panel.add(btnMachineClear);
		
		stationPanel = new JPanel();
		stationPanel.setBackground(Color.LIGHT_GRAY);
		stationPanel.setBounds(504, 109, 403, 120);
		panel.add(stationPanel);
		
		machineScroll = new JScrollPane();
		machineScroll.setBackground(Color.LIGHT_GRAY);
		machineScroll.setBounds(504, 109, 403, 105);
		stationPanel.add(machineScroll);	
		
		JLabel lblTrainingConfiguration = new JLabel("Training Configuration");
		lblTrainingConfiguration.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblTrainingConfiguration.setBounds(504, 248, 186, 23);
		panel.add(lblTrainingConfiguration);
		
		JButton button = new JButton("Load");
		button.setFont(new Font("Dialog", Font.PLAIN, 11));
		button.setBackground(Color.WHITE);
		button.setBounds(504, 399, 97, 23);
		panel.add(button);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnClear.setBackground(Color.WHITE);
		btnClear.setBounds(613, 399, 97, 23);
		panel.add(btnClear);
		
		JButton btnTrain = new JButton("Train");
		btnTrain.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnTrain.setBackground(Color.WHITE);
		btnTrain.setBounds(810, 399, 97, 23);
		panel.add(btnTrain);
		
		txtConsoleArea = new JTextArea();
		txtConsoleArea.setEditable(false);
		txtConsoleArea.setBackground(Color.WHITE);
		txtConsoleArea.setBounds(12, 10, 480, 804);
		panel.add(txtConsoleArea);
		
		JLabel lblNeuralNetwork = new JLabel("Neural Network");
		lblNeuralNetwork.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNeuralNetwork.setBounds(504, 334, 113, 23);
		panel.add(lblNeuralNetwork);
		
		JLabel label_2 = new JLabel("Model:");
		label_2.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_2.setBounds(504, 355, 113, 23);
		panel.add(label_2);
		
		textField_7 = new JComboBox();
		textField_7.setFont(new Font("Dialog", Font.PLAIN, 11));
		textField_7.setBackground(Color.WHITE);
		textField_7.setBounds(629, 348, 278, 21);
		panel.add(textField_7);
		
		JLabel lblTraining = new JLabel("Training");
		lblTraining.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblTraining.setBounds(504, 281, 113, 23);
		panel.add(lblTraining);
		
		JLabel lblIteration = new JLabel("Iteration:");
		lblIteration.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblIteration.setBounds(504, 302, 113, 23);
		panel.add(lblIteration);
		
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 11));
		textField.setColumns(10);
		textField.setBounds(629, 295, 278, 21);
		panel.add(textField);
		
		btnMachineUpload = new JButton("Upload");
		btnMachineUpload.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnMachineUpload.setBackground(Color.WHITE);
		btnMachineUpload.setBounds(701, 76, 97, 23);
		panel.add(btnMachineUpload);
		
		JLabel label_4 = new JLabel("Configuration");
		label_4.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_4.setBounds(504, 43, 113, 23);
		panel.add(label_4);
		
		JLabel label_5 = new JLabel("Files:");
		label_5.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_5.setBounds(504, 64, 113, 23);
		panel.add(label_5);
		
		machineComboBox = new JComboBox<UploadObject>();
		machineComboBox.setBackground(Color.WHITE);
		machineComboBox.setBounds(619, 45, 288, 23);
		panel.add(machineComboBox);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Generator", null, panel_1, null);
		panel_1.setLayout(null);
		
		mapPanel = new JPanel();
		mapPanel.setBackground(Color.WHITE);
		mapPanel.setBounds(12, 10, 515, 804);
		panel_1.add(mapPanel);
		
		JLabel lblDeviceConfiguration = new JLabel("Device Configuration");
		lblDeviceConfiguration.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblDeviceConfiguration.setBounds(569, 117, 186, 23);
		panel_1.add(lblDeviceConfiguration);
		
		JLabel lblExport = new JLabel("Export:");
		lblExport.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblExport.setBounds(569, 147, 95, 23);
		panel_1.add(lblExport);
		
		chckbxEnvironment = new JCheckBox("Environment");
		chckbxEnvironment.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxEnvironment.setBounds(672, 150, 105, 23);
		panel_1.add(chckbxEnvironment);
		
		chckbxPositioningDevice = new JCheckBox("Device Position");
		chckbxPositioningDevice.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxPositioningDevice.setBounds(792, 150, 121, 23);
		panel_1.add(chckbxPositioningDevice);
		
		JLabel lblDevice = new JLabel("Device");
		lblDevice.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDevice.setBounds(569, 180, 95, 23);
		panel_1.add(lblDevice);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblType.setBounds(569, 201, 95, 23);
		panel_1.add(lblType);
		
		stationTypeComboBox = new JComboBox();
		stationTypeComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		stationTypeComboBox.setBackground(Color.WHITE);
		stationTypeComboBox.setBounds(672, 194, 237, 21);
		panel_1.add(stationTypeComboBox);
		
		stationDistriTypeComboBox = new JComboBox();
		stationDistriTypeComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		stationDistriTypeComboBox.setBackground(Color.WHITE);
		stationDistriTypeComboBox.setBounds(672, 248, 237, 21);
		panel_1.add(stationDistriTypeComboBox);
		
		JLabel lblDeployment = new JLabel("Deployment");
		lblDeployment.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDeployment.setBounds(569, 234, 95, 23);
		panel_1.add(lblDeployment);
		
		JLabel lblModel_1 = new JLabel("Model:");
		lblModel_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblModel_1.setBounds(569, 255, 95, 23);
		panel_1.add(lblModel_1);
		
		JLabel lblDevice_1 = new JLabel("Device");
		lblDevice_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDevice_1.setBounds(569, 298, 95, 23);
		panel_1.add(lblDevice_1);
		
		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNumber.setBounds(569, 319, 95, 23);
		panel_1.add(lblNumber);
		
		txtStationMaxNumInPart = new JTextField();
		txtStationMaxNumInPart.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtStationMaxNumInPart.setToolTipText("Maximum for each room");
		txtStationMaxNumInPart.setColumns(10);
		txtStationMaxNumInPart.setBounds(672, 290, 237, 21);
		panel_1.add(txtStationMaxNumInPart);
		
		txtStationMaxNumInArea = new JTextField();
		txtStationMaxNumInArea.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtStationMaxNumInArea.setToolTipText("Maximum for each 100 meter square");
		txtStationMaxNumInArea.setColumns(10);
		txtStationMaxNumInArea.setBounds(672, 321, 237, 21);
		panel_1.add(txtStationMaxNumInArea);
		
		JLabel lblDetection = new JLabel("Detection");
		lblDetection.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDetection.setBounds(569, 352, 95, 23);
		panel_1.add(lblDetection);
		
		JLabel lblRange = new JLabel("Range:");
		lblRange.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRange.setBounds(569, 373, 95, 23);
		panel_1.add(lblRange);
		
		txtScanRange = new JTextField();
		txtScanRange.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtScanRange.setColumns(10);
		txtScanRange.setBounds(672, 366, 237, 21);
		panel_1.add(txtScanRange);
		
		JLabel lblDetection_1 = new JLabel("Detection");
		lblDetection_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDetection_1.setBounds(569, 406, 95, 23);
		panel_1.add(lblDetection_1);
		
		JLabel lblFrequency = new JLabel("Frequency:");
		lblFrequency.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblFrequency.setBounds(569, 427, 95, 23);
		panel_1.add(lblFrequency);
		
		txtScanRate = new JTextField();
		txtScanRate.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtScanRate.setColumns(10);
		txtScanRate.setBounds(672, 420, 237, 21);
		panel_1.add(txtScanRate);
		
		btnStationGenerate = new JButton("Generate");
		btnStationGenerate.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnStationGenerate.setBackground(Color.WHITE);
		btnStationGenerate.setBounds(812, 451, 97, 23);
		panel_1.add(btnStationGenerate);
		
		JLabel lblMovingObjectConfiguration = new JLabel("Moving Object Configuration");
		lblMovingObjectConfiguration.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblMovingObjectConfiguration.setBounds(569, 484, 237, 23);
		panel_1.add(lblMovingObjectConfiguration);
		
		JButton btnClear_1 = new JButton("Clear");
		btnClear_1.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnClear_1.setBackground(Color.WHITE);
		btnClear_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MovingObjectsWindow().main(null);
			}
		});
		btnClear_1.setBounds(812, 550, 97, 23);
		panel_1.add(btnClear_1);
		
		movingObjectPanel = new JPanel();
		movingObjectPanel.setBackground(Color.LIGHT_GRAY);
		movingObjectPanel.setBounds(569, 583, 340, 119);
		panel_1.add(movingObjectPanel);
		
		movingObjectScroll = new JScrollPane();
		movingObjectScroll.setBackground(Color.LIGHT_GRAY);
		movingObjectScroll.setBounds(567, 486, 340, 137);
		movingObjectPanel.add(movingObjectScroll);		
		
		btnObjectInit = new JButton("Init");
		btnObjectInit.setBackground(Color.WHITE);
		btnObjectInit.setBounds(707, 758, 97, 23);
		panel_1.add(btnObjectInit);
		
		btnObjectStart = new JButton("Start");
		btnObjectStart.setBackground(Color.WHITE);
		btnObjectStart.setBounds(812, 758, 97, 23);
		panel_1.add(btnObjectStart);
		
		btnObjectStop = new JButton("Stop");
		btnObjectStop.setBackground(Color.WHITE);
		btnObjectStop.setBounds(709, 791, 97, 23);
		panel_1.add(btnObjectStop);
		
		btnMovingObjUpload = new JButton("Upload");
		btnMovingObjUpload.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnMovingObjUpload.setBackground(Color.WHITE);
		btnMovingObjUpload.setBounds(709, 550, 97, 23);
		panel_1.add(btnMovingObjUpload);
		
		chckbxTrajectory = new JCheckBox("Trajectory");
		chckbxTrajectory.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxTrajectory.setBounds(668, 714, 105, 23);
		panel_1.add(chckbxTrajectory);
		
		chckbxTracking = new JCheckBox("Raw RSSI");
		chckbxTracking.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxTracking.setBounds(788, 714, 121, 23);
		panel_1.add(chckbxTracking);
		
		JLabel label = new JLabel("Export:");
		label.setFont(new Font("Dialog", Font.PLAIN, 14));
		label.setBounds(569, 711, 91, 23);
		panel_1.add(label);
		
		objectComboBox = new JComboBox<UploadObject>();
		objectComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		objectComboBox.setBackground(Color.WHITE);
		objectComboBox.setBounds(684, 517, 225, 23);
		panel_1.add(objectComboBox);
		
		JLabel lblConfiguration = new JLabel("Configuration");
		lblConfiguration.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblConfiguration.setBounds(569, 515, 113, 23);
		panel_1.add(lblConfiguration);
		
		JLabel lblFiles = new JLabel("Files:");
		lblFiles.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblFiles.setBounds(569, 536, 113, 23);
		panel_1.add(lblFiles);
		
		btnSnapShot = new JButton("Capture");
		btnSnapShot.setBackground(Color.WHITE);
		btnSnapShot.setBounds(812, 791, 97, 23);
		panel_1.add(btnSnapShot);
		
		JLabel lblNavigation = new JLabel("Navigation");
		lblNavigation.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNavigation.setBounds(569, 10, 186, 23);
		panel_1.add(lblNavigation);
		
		JLabel label_7 = new JLabel("Floor:");
		label_7.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_7.setBounds(569, 43, 113, 23);
		panel_1.add(label_7);
		
		navCombobox = new JComboBox();
		navCombobox.setFont(new Font("Dialog", Font.PLAIN, 11));
		navCombobox.setBackground(Color.WHITE);
		navCombobox.setBounds(694, 45, 215, 23);
		panel_1.add(navCombobox);
		
		btnDeleteNav = new JButton("Clear");
		btnDeleteNav.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDeleteNav.setBackground(Color.WHITE);
		btnDeleteNav.setBounds(836, 80, 73, 23);
		panel_1.add(btnDeleteNav);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Visualization", null, panel_2, null);
		panel_2.setLayout(null);
		
		mapVisualPanel = new JPanel();
		mapVisualPanel.setBackground(Color.WHITE);
		mapVisualPanel.setBounds(12, 10, 519, 804);
		panel_2.add(mapVisualPanel);
		
		JLabel lblBuildingDetails = new JLabel("Building Details");
		lblBuildingDetails.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblBuildingDetails.setBounds(543, 10, 186, 23);
		panel_2.add(lblBuildingDetails);
		
		floorCombobox = new JComboBox();
		floorCombobox.setFont(new Font("Dialog", Font.PLAIN, 11));
		floorCombobox.setBackground(Color.WHITE);
		floorCombobox.setBounds(668, 45, 239, 23);
		panel_2.add(floorCombobox);
		
		JLabel lblFloor = new JLabel("Floor:");
		lblFloor.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblFloor.setBounds(543, 43, 113, 23);
		panel_2.add(lblFloor);
		
		JLabel lblDetails = new JLabel("Details:");
		lblDetails.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDetails.setBounds(543, 175, 113, 23);
		panel_2.add(lblDetails);
		
		scrollPanePart = new JScrollPane();
		scrollPanePart.setBounds(668, 177, 239, 88);
		panel_2.add(scrollPanePart);
		
		connectedPartsModel = new DefaultListModel<Partition>();
		connectedPartsList = new JList<Partition>(connectedPartsModel);
		connectedPartsList.setBackground(Color.WHITE);
		connectedPartsList.setFont(new Font("Dialog", Font.PLAIN, 14));
		connectedPartsList.setVisibleRowCount(6);
		connectedPartsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		connectedPartsList.setSelectedIndex(0);
		connectedPartsList.setLayoutOrientation(JList.VERTICAL);
		connectedPartsList.setBounds(20, 20, 300, 70);
		scrollPanePart.setViewportView(connectedPartsList);

		possibleConnectedPartsList = new ArrayList<>();
		
		JLabel lblRoom = new JLabel("Room:");
		lblRoom.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRoom.setBounds(543, 111, 95, 23);
		panel_2.add(lblRoom);
		
		txtselectedNameField = new JTextField();
		txtselectedNameField.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtselectedNameField.setEditable(false);
		txtselectedNameField.setColumns(10);
		txtselectedNameField.setBounds(668, 111, 239, 21);
		panel_2.add(txtselectedNameField);
		
		JLabel lblMovementPatterns = new JLabel("Movement Patterns");
		lblMovementPatterns.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblMovementPatterns.setBounds(541, 287, 186, 23);
		panel_2.add(lblMovementPatterns);
		
		JLabel lblStartDate = new JLabel("Start Date:");
		lblStartDate.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblStartDate.setBounds(541, 343, 95, 23);
		panel_2.add(lblStartDate);
		
		txtStartTime = new JTextField();
		txtStartTime.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtStartTime.setColumns(10);
		txtStartTime.setBounds(666, 343, 239, 21);
		panel_2.add(txtStartTime);
		
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblEndDate.setBounds(541, 376, 95, 23);
		panel_2.add(lblEndDate);
		
		txtEndTime = new JTextField();
		txtEndTime.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtEndTime.setColumns(10);
		txtEndTime.setBounds(666, 376, 239, 21);
		panel_2.add(txtEndTime);
		
		btnShow = new JButton("Show");
		btnShow.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnShow.setBackground(Color.WHITE);
		btnShow.setBounds(808, 408, 97, 23);
		panel_2.add(btnShow);
		
		btnDeleteFloor = new JButton("Clear");
		btnDeleteFloor.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDeleteFloor.setBackground(Color.WHITE);
		btnDeleteFloor.setBounds(834, 78, 73, 23);
		panel_2.add(btnDeleteFloor);
		
		btnDeleteEntity = new JButton("Clear");
		btnDeleteEntity.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnDeleteEntity.setBackground(Color.WHITE);
		btnDeleteEntity.setBounds(834, 142, 73, 23);
		panel_2.add(btnDeleteEntity);
		
		JLabel lblPositioning = new JLabel("Positioning");
		lblPositioning.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblPositioning.setBounds(541, 470, 186, 23);
		panel_2.add(lblPositioning);
		
		JLabel label_1 = new JLabel("Export:");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_1.setBounds(541, 499, 87, 23);
		panel_2.add(label_1);
		
		JCheckBox chckbxPositioningData = new JCheckBox("Positioning Data");
		chckbxPositioningData.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxPositioningData.setBounds(666, 500, 123, 23);
		panel_2.add(chckbxPositioningData);
		
		JLabel lblPositioning_1 = new JLabel("Positioning");
		lblPositioning_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPositioning_1.setBounds(541, 532, 113, 23);
		panel_2.add(lblPositioning_1);
		
		JLabel lblAlgorithm = new JLabel("Algorithm:");
		lblAlgorithm.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAlgorithm.setBounds(541, 553, 113, 23);
		panel_2.add(lblAlgorithm);
		
		positionAlgorithmComboBox = new JComboBox();
		positionAlgorithmComboBox.setFont(new Font("Dialog", Font.PLAIN, 11));
		positionAlgorithmComboBox.setBackground(Color.WHITE);
		positionAlgorithmComboBox.setBounds(666, 546, 239, 21);
		panel_2.add(positionAlgorithmComboBox);
		
		JLabel lblRssi = new JLabel("RSSI");
		lblRssi.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRssi.setBounds(541, 586, 113, 23);
		panel_2.add(lblRssi);
		
		JLabel lblPath = new JLabel("Path:");
		lblPath.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPath.setBounds(541, 607, 113, 23);
		panel_2.add(lblPath);
		
		txtRssiInputPath = new JTextField();
		txtRssiInputPath.setFont(new Font("Dialog", Font.PLAIN, 11));
		txtRssiInputPath.setColumns(10);
		txtRssiInputPath.setBounds(666, 600, 239, 21);
		panel_2.add(txtRssiInputPath);
		
		JLabel label_3 = new JLabel("Details:");
		label_3.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_3.setBounds(541, 640, 113, 23);
		panel_2.add(label_3);
		
		positionPropertiesScrollPane = new JScrollPane();
		positionPropertiesScrollPane.setBounds(666, 642, 239, 99);
		panel_2.add(positionPropertiesScrollPane);
		
		positionPropertiesArea = new JTextArea();
		positionPropertiesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		positionPropertiesArea.setBackground(Color.WHITE);
		positionPropertiesScrollPane.setViewportView(positionPropertiesArea);
		positionPropertiesArea.setBorder(new LineBorder(Color.LIGHT_GRAY));
		
		btnPositionGenerate = new JButton("Generate");
		btnPositionGenerate.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnPositionGenerate.setBackground(Color.WHITE);
		btnPositionGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPositionGenerate.setBounds(808, 751, 97, 23);
		panel_2.add(btnPositionGenerate);
		
		chckbxPath = new JRadioButton("Path");
		chckbxPath.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxPath.setBounds(668, 313, 105, 23);
		chckbxPath.setSelected(true);
		panel_2.add(chckbxPath);
		
		chckbxHeatMap = new JRadioButton("Heatmap");
		chckbxHeatMap.setFont(new Font("Dialog", Font.PLAIN, 11));
		chckbxHeatMap.setBounds(788, 313, 121, 23);
		panel_2.add(chckbxHeatMap);
		
		JLabel lblShow = new JLabel("Display:");
		lblShow.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblShow.setBounds(541, 313, 95, 23);
		panel_2.add(lblShow);
				
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

		txtStartTime.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Frame dialogFrame = new Frame();
				Dialog dialog = new Dialog(dialogFrame);
				dialog.setLayout(null);
				JTimeChooser jtc = new JTimeChooser(dialog);
				startCalendar = jtc.showTimeDialog();
				System.out.println(startCalendar.getTime().toString());
				txtStartTime.setText(IdrObjsUtility.sdf.format(startCalendar.getTime()));
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});

		txtEndTime.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				btnObjectStart.setEnabled(false);

				Frame dialogFrame = new Frame();
				Dialog dialog = new Dialog(dialogFrame);
				dialog.setLayout(null);
				JTimeChooser jtc = new JTimeChooser(dialog);
				endCalendar = jtc.showTimeDialog();
				System.out.println(endCalendar.getTime().toString());

				try {
					Date selectedStartTime = IdrObjsUtility.sdf.parse(txtStartTime.getText());
					if (endCalendar.getTime().before(selectedStartTime)) {
						JOptionPane.showMessageDialog(frmTrajectoryGenerator, "The end time should be later than start time!",
								"Error", JOptionPane.ERROR_MESSAGE);
						txtEndTime.setText("");
					} else {
						txtEndTime.setText(IdrObjsUtility.sdf.format(endCalendar.getTime()));
						toggleBtnObjectStart();
					}
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});

		txtRssiInputPath.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String previousPath = txtRssiInputPath.getText();
				String inputPath = decideOutputPath();
				if (inputPath != null) {
					txtRssiInputPath.setText(inputPath);
				} else {
					txtRssiInputPath.setText(previousPath);
				}

				if ((!txtRssiInputPath.getText().equals("")) && (txtRssiInputPath.getText() != null)) {
					btnPositionGenerate.setEnabled(true);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});

	}

	private void toggleGeneratationBtns(boolean state) {
		btnDeleteFloor.setEnabled(state);
		btnDeleteEntity.setEnabled(state);
		btnStationGenerate.setEnabled(state);
		btnObjectInit.setEnabled(state);
		toggleMovingObjectGenerationBtns(false);
		btnPositionGenerate.setEnabled(state);
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
		
		btnMachineUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadConfigFile();
			}
		});
		
		btnMovingObjUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uploadObjectFile();
			}
		});

		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				uploadFile();
				updateFileChooser();
				if (files.size() > 0) {
					fileComboBox.setSelectedItem(files.get(files.size() - 1));
					printUploadInfo();
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

	private void printUploadInfo() {
		txtConsoleArea.append("You have extracted: \n");
		txtConsoleArea.append(DB_WrapperLoad.floorT.size() + " floors\n");
		txtConsoleArea.append(DB_WrapperLoad.partitionT.size() + " partitions\n");
		txtConsoleArea.append(DB_WrapperLoad.accesspointT.size() + " doors\n");
		txtConsoleArea.append(DB_WrapperLoad.connectorT.size() + " stairs\n");
		txtConsoleArea.append("----------------------------------------------------------------\n");
	}

	private void printPartAPInfo() {
		txtConsoleArea.append("Viewing File " + fileChosen.getUploadId() + ". " + fileChosen.getFilename() + "\n");
		txtConsoleArea.append("There are in total: \n");
		txtConsoleArea.append(DB_WrapperLoad.floorT.size() + " floors\n");
		txtConsoleArea.append(DB_WrapperLoad.partitionT.size() + " partitions\n");
		txtConsoleArea.append(DB_WrapperLoad.accesspointT.size() + " doors\n");
		txtConsoleArea.append(DB_WrapperLoad.connectorT.size() + " stairs\n");
		txtConsoleArea.append("----------------------------------------------------------------\n");
	}

	private void uploadFile() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".ifc") || f.isDirectory();
			}

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
	
	private void uploadConfigFile() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".json") || f.isDirectory();
			}

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
				txtConsoleArea.append("File Already Existed! PASS\n");
				return;
			}

			// load machine objects
			try {
				Gson gson = new Gson();
				JsonReader reader = new JsonReader(new FileReader(file));
				reader.setLenient(true);
				machines = gson.fromJson(reader, Machine[].class);
				// Show the file in panel
				machineComboBox.removeAllItems();
				machineComboBox.addItem(object);
				for (Machine machine: machines) {
					MachineView view = new MachineView(machine, machine.getName());
					stationPanel.add(view);
					repaint();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void uploadObjectFile() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".json") || f.isDirectory();
			}

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
				txtConsoleArea.append("File Already Existed! PASS\n");
				return;
			}

			// load moving objects
			try {
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Date.class, new TimeSerializer());
				gsonBuilder.registerTypeAdapter(Date.class, new TimeDeserializer());
				Gson gson = gsonBuilder
						.setPrettyPrinting()
						.serializeNulls()
						.create();
				JsonReader reader = new JsonReader(new FileReader(file));
				reader.setLenient(true);
				persons = gson.fromJson(reader, MovingObjResponse[].class);
				
				// Show the file in panel
				objectComboBox.removeAllItems();
				objectComboBox.addItem(object);
				for (MovingObjResponse person: persons) {
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
		int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to view the selected file?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
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
			mapVisualPainter = new MapVisualPainter(fileChosen.getUploadId());
			mapVisualPanel.add(mapVisualPainter);
			
			switchStateForButtons(InteractionState.AFTER_VIEW_FILE_NO_CHANGE);
			printPartAPInfo();
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
				txtConsoleArea.append(fileChosen.getFilename() + " is deleted!!!\n");
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
			setBorder(BorderFactory.createLineBorder(Color.black));
			setOpaque(true);
			setBackground(Color.white);

			D2DGraph buildingD2D = new D2DGraph(DB_WrapperLoad.partitionDecomposedT,
					DB_WrapperLoad.accessPointConnectorT);
			// BuildingD2DGrpah buildingD2D = new BuildingD2DGrpah();
			// BuildingD2DGrpah.partitions =
			// DB_WrapperLoad.partitionDecomposedT;
			// BuildingD2DGrpah.accessPoints =
			// DB_WrapperLoad.accessPointConnectorT;
			// BuildingD2DGrpah.connectors = DB_WrapperLoad.connectorT;
			buildingD2D.generateD2DDistance();
			for (Floor floor : DB_WrapperLoad.floorT) {
				floor.setPartitionsRTree(IdrObjsUtility.generatePartRTree(floor));
				floor.setD2dGraph(buildingD2D);
			}

		}

		private void addMapPainterActionListener() {
			navCombobox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					chosenFloor = (Floor) navCombobox.getSelectedItem();
					selectedPart = null;
					selectedAP = null;
					selectedCon = null;
				}

			});
			
			btnStationGenerate.addActionListener(new ActionListener() {
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
							JOptionPane.showMessageDialog(frmTrajectoryGenerator, "Generating Infrastrcture Data is done!",
									"Information", JOptionPane.INFORMATION_MESSAGE);
						}
						String selected_station_type = stationTypeComboBox.getSelectedItem().toString();
						if (selected_station_type.equals("WIFI")) {
							initAlgorithmMap();
						} else {
							initAlgorithmMapForRFIDAndBT();
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
								updateAlgProps();
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
				public void actionPerformed(ActionEvent e) {
					String outputPath = decideOutputPath();
					if (outputPath != null) {
						createSnapshotOutputDir(outputPath);
						snapShot(movingObjs);
					}

				}
			});
		}

		/**
		 * illegal element --> self-intersection partitions, isolated access
		 * points warning element --> isolated partitions print all the
		 * isolation partitions and access points get all the self-intersection
		 * partition, and delete them all, because they can caught an exception
		 * when decompose delete all the isolation access points, try to fix
		 * isolate partitions remember to reload all the space object, because
		 * some partitions may have been deleted
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
						? IdrObjsUtility.sdf.parse(persons[0].getStartTime()) : startCalendar.getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			try {
				IdrObjsUtility.objectGenerateEndTime = endCalendar == null
						? IdrObjsUtility.sdf.parse(persons[0].getEndTime()) : endCalendar.getTime();
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

		private void updateAlgProps() {
			// IdrObjsUtility.changeAllAlgInputPath();
			String algorithmType = positionAlgorithmComboBox.getSelectedItem().toString();
			loadAlgorithmProp(algorithmType);
		}

		private void loadAlgorithmProp(String algorithmType) {
			if (algorithmType.equals("Trilateration")) {
				loadAlgPropToGUI("conf/trilateration.properties");
			}
			if (algorithmType.equals("Fingerprinting")) {
				loadAlgPropToGUI("conf/fingerprint.properties");
			}
			if (algorithmType.equals("Proximity_Analysis")) {
				loadAlgPropToGUI("conf/proximity.properties");
			}
		}

		private void loadAlgPropToGUI(String algPropName) {
			System.out.println(algPropName);
			positionPropertiesArea.setText("");
			try {
				FileReader fileReader = new FileReader(algPropName);
				BufferedReader buff = new BufferedReader(fileReader);
				String line = null;
				while ((line = buff.readLine()) != null) {
					positionPropertiesArea.append(line + "\n");
				}
				fileReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void storeAlgProp() {
			String algorithmType = positionAlgorithmComboBox.getSelectedItem().toString();
			String outputFileName = null;
			System.out.println(algorithmType);
			if (algorithmType.equals("Trilateration")) {
				outputFileName = "conf/trilateration.properties";
			}
			if (algorithmType.equals("Fingerprinting")) {
				outputFileName = "conf/fingerprint.properties";
			}
			if (algorithmType.equals("Proximity_Analysis")) {
				outputFileName = "conf/proximity.properties";
			}

			try {
				FileWriter writer = new FileWriter(outputFileName);
				String positionPropsTxt = positionPropertiesArea.getText();

				boolean dateFlag = true;
				String[] lines = positionPropsTxt.split("\n");
				for (String line : lines) {
					System.out.println(line);
					// update the time-stamp for properties file
					if (dateFlag && "#".equals(line.substring(0, 1))) {
						dateFlag = false;
						writer.write("#" + new Date(System.currentTimeMillis()).toString() + "\n");
					} else
						writer.write(line + "\n");
				}

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void clearMapPainterActionListener() {
			for (ActionListener al : navCombobox.getActionListeners()) {
				navCombobox.removeActionListener(al);
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
//				movingObjectTypeComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					movingObjTypeMap.put(splitedString[0], splitedString[1]);
//					movingObjectTypeComboBox.addItem(splitedString[0]);
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
//				movObjDistributerTypeComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					movObjInitMap.put(splitedString[0], splitedString[1]);
//					movObjDistributerTypeComboBox.addItem(splitedString[0]);
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

		private void initAlgorithmMap() {
			try {
				FileReader fileReader = new FileReader("conf/algorithmTypeMap.properties");
				BufferedReader buff = new BufferedReader(fileReader);
				String line;
				positionAlgorithmMap.clear();
				for (int i = 0; i < positionAlgorithmComboBox.getItemCount(); i++) {
					System.out.println("--" + positionAlgorithmComboBox.getItemAt(i));
				}
				positionAlgorithmComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					positionAlgorithmMap.put(splitedString[0], splitedString[1]);
					positionAlgorithmComboBox.addItem(splitedString[0]);
				}
				fileReader.close();
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void initAlgorithmMapForRFIDAndBT() {
			try {
				FileReader fileReader = new FileReader("conf/algorithmTypeMap.properties");
				BufferedReader buff = new BufferedReader(fileReader);
				String line;
				positionAlgorithmMap.clear();
				for (int i = 0; i < positionAlgorithmComboBox.getItemCount(); i++) {
					System.out.println("--" + positionAlgorithmComboBox.getItemAt(i));
				}
				positionAlgorithmComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					if (!splitedString[0].equals("Fingerprinting")) {
						positionAlgorithmMap.put(splitedString[0], splitedString[1]);
						positionAlgorithmComboBox.addItem(splitedString[0]);
					}
				}
				fileReader.close();
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			chosenFloor = (Floor) navCombobox.getSelectedItem();

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
				} else if (connectedPartsList.getSelectedValuesList().contains(r)) {
					g2.setColor(new Color(103, 109, 116));
				} else {
					g2.setColor(new Color(249, 248, 246));
				}
				g2.fill(poNew);
				
				// Background image
				if (r == selectedPart) {
					g2.setColor(new Color(173, 173, 173));
					g2.setStroke(Pen1);
				} else if (connectedPartsList.getSelectedValuesList().contains(r)) {
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

		private void updateSelectPartsList() {
			connectedPartsModel.clear();
			if (selectedPart != null) {
				txtselectedNameField.setText(selectedPart.getName() + " AND ID: " + selectedPart.getItemID());
				for (Partition partition : selectedPart.getConParts()) {
					connectedPartsModel.addElement(partition);
				}
			} else if (selectedAP != null) {
				txtselectedNameField.setText(selectedAP.getName() + " AND ID: " + selectedAP.getItemID());
				for (Partition p : selectedAP.getPartitions()) {
					connectedPartsModel.addElement(p);
				}
			} else if (selectedCon != null) {
				txtselectedNameField.setText(selectedCon.getName() + " AND ID: " + selectedCon.getItemID());
				for (Partition p : selectedCon.getPartitions()) {

					connectedPartsModel.addElement(p);
				}

			} else {

			}
		}

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
			if (navCombobox.getSelectedItem() != null) {
				Floor f = (Floor) navCombobox.getSelectedItem();
				floor_globalid = f.getGlobalID();
			}
			navCombobox.removeAllItems();
			for (int i = 0; i < DB_WrapperLoad.floorT.size(); i++) {
				Floor f = DB_WrapperLoad.floorT.get(i);
				navCombobox.addItem(f);
			}
			if (floor_globalid != null) {
				for (int i = 0; i < navCombobox.getModel().getSize(); i++) {
					if (navCombobox.getModel().getElementAt(i).getGlobalID().equals(floor_globalid)) {
						navCombobox.setSelectedItem(navCombobox.getModel().getElementAt(i));
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

			String floor_outputPath = currentPath + "//Floors" + ".txt";
			try {
				file = new File(floor_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				for (Floor floor : DB_WrapperLoad.floorT) {
					buff.write((floor.toString() + "\n").getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String accessPoint_outputPath = currentPath + "//Access Points" + ".txt";
			try {
				file = new File(accessPoint_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				for (Floor floor : DB_WrapperLoad.floorT) {
					for (AccessPoint ap : floor.getAccessPoints())
						buff.write((ap.toString() + "\n").getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String parition_outputPath = currentPath + "//Partitions" + ".txt";
			try {
				file = new File(parition_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				for (Floor floor : DB_WrapperLoad.floorT) {
					for (Partition par : floor.getPartitions())
						buff.write((par.toString2() + "\n").getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String connector_outputPath = currentPath + "//Connectors" + ".txt";
			try {
				file = new File(connector_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				for (Floor floor : DB_WrapperLoad.floorT) {
					for (Connector connector : floor.getConnectors())
						buff.write((connector.toString() + "\n").getBytes());
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String connectivity_outputPath = currentPath + "//Connectivity" + ".txt";
			try {
				file = new File(connectivity_outputPath);
				file.createNewFile();
				outStr = new FileOutputStream(file);
				buff = new BufferedOutputStream(outStr);
				for (Connectivity connectivity : DB_WrapperLoad.connectivityT) {
					buff.write((connectivity.toString() + "\n").getBytes());
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
				configure = configure + "Initial Distribution="
						+ persons[0].getInitialDistribution() + "\n";
				configure = configure + "Maximum Object Number in a Partition=" + persons[0].getObjectNumber()
						+ "\n";
				configure = configure + "Maximum Life Span(s)=" + persons[0].getLifeSpan() + "\n";
				configure = configure + "Maximum Step Length(m)=" + persons[0].getMaxStepLength() + "\n";
				configure = configure + "Move Rate(ms)=" + persons[0].getMoveRate() + "\n";
				configure = configure + "Generation Period=" + persons[0].getStartTime() + "-" + persons[0].getEndTime()
						+ "\n";
				buff.write(configure.getBytes());
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private void exportPositioningConfiguration(String outputPath) {

			System.out.println(outputPath);
			String position_configuration_outputPath = outputPath.substring(0, outputPath.lastIndexOf(File.separator))
					+ "//Positioning_Configuration_"
					+ outputPath.substring(outputPath.lastIndexOf(File.separator) + 1, outputPath.length()) + ".txt";
			System.out.println(position_configuration_outputPath);
			try {
				File file = new File(position_configuration_outputPath);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStr = new FileOutputStream(file);
				BufferedOutputStream buff = new BufferedOutputStream(outStr);
				String configure = positionPropertiesArea.getText();
				configure = configure + "\n" + "positioning_algorithm="
						+ positionAlgorithmComboBox.getSelectedItem().toString() + "\n";
				configure = configure + "\n" + "input_rssi_path=" + txtRssiInputPath.getText() + "\n";
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

		private String createPositioningOutputDir(String outputPath) {
			// TODO Auto-generated method stub

			Date date = new Date(System.currentTimeMillis());
			String time = IdrObjsUtility.dir_sdf.format(date);

			File dir = new File(outputPath);

			File positionDir = new File(outputPath + "//indoor positioning data");

			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			positionDir.mkdirs();

			if (!positionDir.exists() && !positionDir.isDirectory() && chckbxPositiongData.isSelected()) {
				positionDir.mkdirs();
			}
			File cur_positionDir = new File(positionDir.getPath() + "//" + time);
			if (!cur_positionDir.exists() && !cur_positionDir.isDirectory() && chckbxTracking.isSelected()) {
				cur_positionDir.mkdirs();
			}

			return cur_positionDir.getPath();

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
						? IdrObjsUtility.sdf.parse(persons[0].getStartTime()) : startCalendar.getTime();
				IdrObjsUtility.objectGenerateEndTime = endCalendar == null
						? IdrObjsUtility.sdf.parse(persons[0].getEndTime()) : endCalendar.getTime();
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

			String positionAlgorithm = positionAlgorithmComboBox.getSelectedItem().toString();
			String posAlgType = positionAlgorithmMap.get(positionAlgorithm);
			props.setProperty("positionAlgorithm", posAlgType);

			MovingObj.setScanRange(Double.parseDouble(scanRange));
			MovingObj.setMaxStepLength(persons[0].getMaxStepLength());
			MovingObj.setMoveRate(persons[0].getMoveRate());
			MovingObj.setMaxSpeed(persons[0].getMaxStepLength() / ((persons[0].getMoveRate() + 0.0) / 1000));
			
			
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

//			String maxMovingNumInPartition = props.getProperty("movingObjMaxNumInPart");
//			txtMaxMovObjNumInPart.setText(maxMovingNumInPartition);
//			String maxStepLength = props.getProperty("movingObjMaxStepLength");
//			txtMaxStepLength.setText(maxStepLength);
//			String moveRate = props.getProperty("movingObjMoveRate");
//			txtMoveRate.setText(moveRate);
//			String movObjMaxLifeSpan = props.getProperty("movingObjMaxLifeSpan");
//			txtMaximumLifeSpan.setText(movObjMaxLifeSpan);

			txtStartTime.setText(IdrObjsUtility.sdf.format(System.currentTimeMillis()));
			txtEndTime.setText(IdrObjsUtility.sdf.format(System.currentTimeMillis() + 10 * 60 * 1000));
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
					txtselectedNameField.setText("");
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
					updateSelectPartsList();

					// possibleConnectedPartsList.clear();
					// updatePossibleConnectedPartsList();
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
	
	// Visual Map Class
	private class MapVisualPainter extends JPanel {

		private MovingAdapter ma = new MovingAdapter();
		private int fileIDX;

		MapVisualPainter(int fileID) {
			fileIDX = fileID;
			setSize(800, 800);
			setPreferredSize(new Dimension(800, 800));

			loadFloorChooser();

			clearMapPainterActionListener();
			addMapPainterActionListener();

			initUCLComboBox();
//			loadPropFromFile("conf/pattern.properties");

			addMouseMotionListener(ma);
			addMouseWheelListener(ma);
			addMouseListener(ma);

			setDoubleBuffered(true);
			setBorder(BorderFactory.createLineBorder(Color.black));
			setOpaque(true);
			setBackground(Color.white);

			D2DGraph buildingD2D = new D2DGraph(DB_WrapperLoad.partitionDecomposedT,
					DB_WrapperLoad.accessPointConnectorT);
			// BuildingD2DGrpah buildingD2D = new BuildingD2DGrpah();
			// BuildingD2DGrpah.partitions =
			// DB_WrapperLoad.partitionDecomposedT;
			// BuildingD2DGrpah.accessPoints =
			// DB_WrapperLoad.accessPointConnectorT;
			// BuildingD2DGrpah.connectors = DB_WrapperLoad.connectorT;
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

			btnDecompAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					int n = JOptionPane.showConfirmDialog(frmTrajectoryGenerator,
							"Are you sure you want to decompose the selected file?", "Confirmation",
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						Connection con = DB_Connection.connectToDatabase("conf/moovework.properties");
						clearIllegal();

						System.out.println(
								"\nBefore decomposed partitions " + DB_WrapperLoad.partitionDecomposedT.size() + "\n");
						try {
							DB_Import.decompose(con);
							DB_WrapperLoad.loadALL(con, fileIDX);
							con.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

						D2DGraph buildingD2D = new D2DGraph(DB_WrapperLoad.partitionDecomposedT,
								DB_WrapperLoad.accessPointConnectorT);
						// BuildingD2DGrpah.partitions =
						// DB_WrapperLoad.partitionDecomposedT;
						// BuildingD2DGrpah.accessPoints =
						// DB_WrapperLoad.accessPointConnectorT;
						// BuildingD2DGrpah buildingD2D = new
						// BuildingD2DGrpah();
						buildingD2D.generateD2DDistance();

						for (Floor floor : DB_WrapperLoad.floorT) {
							floor.setPartitionsRTree(IdrObjsUtility.generatePartRTree(floor));
							floor.setD2dGraph(buildingD2D);
							System.out.println(
									floor.getName() + " " + floor.getPartsAfterDecomposed().size() + " partitions\n");

						}

						System.out.println("In total: " + DB_WrapperLoad.partitionDecomposedT.size() + " partitions\n");
						JOptionPane.showMessageDialog(frmTrajectoryGenerator, "Decomposing File is done!", "Information",
								JOptionPane.INFORMATION_MESSAGE);
						switchStateForButtons(InteractionState.AFTER_DECOMPOSE);
						selectedAP = null;
						selectedPart = null;
						loadFloorChooser();
						updateSelectPartsList();
						repaint();
					} else if (n == JOptionPane.NO_OPTION) {
						// Nothing
					} else {
						// Nothing
					}

				}
			});

			btnDeleteEntity.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int n = JOptionPane.showConfirmDialog(frmTrajectoryGenerator,
							"Are you sure you want to delete the selected entity?", "Confirmation",
							JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION) {
						Connection con = DB_Connection.connectToDatabase("conf/moovework.properties");
						try {
							if (selectedPart != null) {
								DB_WrapperDelete.deletePartition(con, selectedPart);
							} else if (selectedAP != null) {
								DB_WrapperDelete.deleteAccessPoint(con, selectedAP);
							}
							DB_WrapperLoad.loadALL(con, fileIDX);
							con.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						JOptionPane.showMessageDialog(frmTrajectoryGenerator, "Deleting Entity is done!", "Information",
								JOptionPane.INFORMATION_MESSAGE);
						loadFloorChooser();
						repaint();
					} else if (n == JOptionPane.NO_OPTION) {
						// Nothing
					} else {
						// Nothing
					}

				}

			});

			connectedPartsList.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					repaint();
				}
			});

			btnShow.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						paintHeatMap();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			visualButtonGroup = new ButtonGroup();
			visualButtonGroup.add(chckbxPath);
			visualButtonGroup.add(chckbxHeatMap);
		}

		/**
		 * illegal element --> self-intersection partitions, isolated access
		 * points warning element --> isolated partitions print all the
		 * isolation partitions and access points get all the self-intersection
		 * partition, and delete them all, because they can caught an exception
		 * when decompose delete all the isolation access points, try to fix
		 * isolate partitions remember to reload all the space object, because
		 * some partitions may have benn deleted
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

		private void updateAlgProps() {
			// IdrObjsUtility.changeAllAlgInputPath();
			String algorithmType = positionAlgorithmComboBox.getSelectedItem().toString();
			loadAlgorithmProp(algorithmType);
		}

		private void loadAlgorithmProp(String algorithmType) {
			if (algorithmType.equals("Trilateration")) {
				loadAlgPropToGUI("conf/trilateration.properties");
			}
			if (algorithmType.equals("Fingerprinting")) {
				loadAlgPropToGUI("conf/fingerprint.properties");
			}
			if (algorithmType.equals("Proximity_Analysis")) {
				loadAlgPropToGUI("conf/proximity.properties");
			}
		}

		private void loadAlgPropToGUI(String algPropName) {
			System.out.println(algPropName);
			positionPropertiesArea.setText("");
			try {
				FileReader fileReader = new FileReader(algPropName);
				BufferedReader buff = new BufferedReader(fileReader);
				String line = null;
				while ((line = buff.readLine()) != null) {
					positionPropertiesArea.append(line + "\n");
				}
				fileReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void storeAlgProp() {
			String algorithmType = positionAlgorithmComboBox.getSelectedItem().toString();
			String outputFileName = null;
			System.out.println(algorithmType);
			if (algorithmType.equals("Trilateration")) {
				outputFileName = "conf/trilateration.properties";
			}
			if (algorithmType.equals("Fingerprinting")) {
				outputFileName = "conf/fingerprint.properties";
			}
			if (algorithmType.equals("Proximity_Analysis")) {
				outputFileName = "conf/proximity.properties";
			}

			try {
				FileWriter writer = new FileWriter(outputFileName);
				String positionPropsTxt = positionPropertiesArea.getText();

				boolean dateFlag = true;
				String[] lines = positionPropsTxt.split("\n");
				for (String line : lines) {
					System.out.println(line);
					// update the time-stamp for properties file
					if (dateFlag && "#".equals(line.substring(0, 1))) {
						dateFlag = false;
						writer.write("#" + new Date(System.currentTimeMillis()).toString() + "\n");
					} else
						writer.write(line + "\n");
				}

				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void clearMapPainterActionListener() {
			for (ActionListener al : floorCombobox.getActionListeners()) {
				floorCombobox.removeActionListener(al);
			}

			for (ActionListener al : btnDecompAll.getActionListeners()) {
				btnDecompAll.removeActionListener(al);
			}

			for (ActionListener al : btnDeleteEntity.getActionListeners()) {
				btnDeleteEntity.removeActionListener(al);
			}

			for (ListSelectionListener al : connectedPartsList.getListSelectionListeners()) {
				connectedPartsList.removeListSelectionListener(al);
			}
			
			for (ActionListener al : btnShow.getActionListeners()) {
				btnShow.removeActionListener(al);
			}			
		}

		private void initUCLComboBox() {
			initAlgorithmMap();
		}

		private void initAlgorithmMap() {
			try {
				FileReader fileReader = new FileReader("conf/algorithmTypeMap.properties");
				BufferedReader buff = new BufferedReader(fileReader);
				String line;
				positionAlgorithmMap.clear();
				for (int i = 0; i < positionAlgorithmComboBox.getItemCount(); i++) {
					System.out.println("--" + positionAlgorithmComboBox.getItemAt(i));
				}
				positionAlgorithmComboBox.removeAllItems();
				while ((line = buff.readLine()) != null) {
					String[] splitedString = new String[2];
					splitedString = line.split("=");
					positionAlgorithmMap.put(splitedString[0], splitedString[1]);
					positionAlgorithmComboBox.addItem(splitedString[0]);
				}
				fileReader.close();
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
			if (trajectories != null) {
				if(chckbxPath.isSelected()) {
//					System.out.println("Path");
//					IdrObjsUtility.paintTrajectories(chosenFloor, g2, tx, Pen1, trajectories, new Color(200, 29, 37));									
				} else {
					System.out.println("Heat Map");
					IdrObjsUtility.paintHeatTrajectories(chosenFloor, g2, tx, Pen1, trajectories);													
				}
			}
			
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
				} else if (connectedPartsList.getSelectedValuesList().contains(r)) {
					g2.setColor(new Color(103, 109, 116));
				} else {
					g2.setColor(new Color(249, 248, 246));
				}
				g2.fill(poNew);
				
				// Heatmap color
//				if (r == mapPart) {
//			        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//			        Color color1 = Color.RED;
//			        Color color2 = Color.GREEN;
//			        GradientPaint gp = new GradientPaint(
//			        		(int) poNew.getBounds2D().getMinX(), (int) poNew.getBounds2D().getMinY(), color1, 
//			        		(int) poNew.getBounds2D().getMaxX(), (int) poNew.getBounds2D().getMaxY(), color2);
//			        
//			        g2.setPaint(gp);
//				}
//				g2.fill(poNew);
//				for (Trajectory t : trajectories) {
//					if (t.getRoomId() == r.getItemID()) {
//						// add the points
//					}
//				}

				// Background color
				if (r == selectedPart) {
					g2.setColor(new Color(173, 173, 173));
					g2.setStroke(Pen1);
				} else if (connectedPartsList.getSelectedValuesList().contains(r)) {
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
		
		private void paintHeatMap() throws ParseException {
			String outputPath = decideOutputPath();
			File folder = new File(outputPath);
			File[] listOfFiles = folder.listFiles();
			trajectories = new ArrayList<Trajectory>();
			
			// Read all files
			for (File file : listOfFiles) {
//				System.out.println("---" + file.getName() + "---");
				try {
					// Create an object of file reader class
					// with CSV file as a parameter.
					FileReader filereader = new FileReader(file);

					// create csvReader object
					// and skip first Line
					CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
					List<String[]> allData = csvReader.readAll();

					// print Data
					for (String[] row : allData) {
						Trajectory traject = new Trajectory(
								Integer.parseInt(row[0]), 
								Integer.parseInt(row[1]), 
								Double.parseDouble(row[2]), 
								Double.parseDouble(row[3]), 
								new SimpleDateFormat("yy/MM/dd HH:mm:ss").parse(row[4]));
						if(isWithinRange(traject.getTimestamp())) {
							trajectories.add(traject);							
						}
//						 for (String cell : row) {
//						 System.out.print(cell + "\t");
//						 }
//						 System.out.println();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
//			System.out.println(trajectories.size());
//			mapPart = null;
//			mapPart = chosenFloor.getPartsAfterDecomposed().get(new Random().nextInt(chosenFloor.getPartsAfterDecomposed().size()));
			
			repaint();
		}
		
		private boolean isWithinRange(Date testDate) throws ParseException {
			Date startDate = new SimpleDateFormat("yy/MM/dd HH:mm:ss").parse(txtStartTime.getText());
			Date endDate = new SimpleDateFormat("yy/MM/dd HH:mm:ss").parse(txtEndTime.getText());
			
		    return testDate.getTime() >= startDate.getTime() &&
		             testDate.getTime() <= endDate.getTime();
		}

		private void updateSelectPartsList() {
			connectedPartsModel.clear();
			if (selectedPart != null) {
				txtselectedNameField.setText(selectedPart.getName() + " AND ID: " + selectedPart.getItemID());
				for (Partition partition : selectedPart.getConParts()) {
					connectedPartsModel.addElement(partition);
				}
			} else if (selectedAP != null) {
				txtselectedNameField.setText(selectedAP.getName() + " AND ID: " + selectedAP.getItemID());
				for (Partition p : selectedAP.getPartitions()) {
					connectedPartsModel.addElement(p);
				}
			} else if (selectedCon != null) {
				txtselectedNameField.setText(selectedCon.getName() + " AND ID: " + selectedCon.getItemID());
				for (Partition p : selectedCon.getPartitions()) {

					connectedPartsModel.addElement(p);
				}

			} else {

			}
		}

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

		private void exportPositioningConfiguration(String outputPath) {

			System.out.println(outputPath);
			String position_configuration_outputPath = outputPath.substring(0, outputPath.lastIndexOf(File.separator))
					+ "//Positioning_Configuration_"
					+ outputPath.substring(outputPath.lastIndexOf(File.separator) + 1, outputPath.length()) + ".txt";
			System.out.println(position_configuration_outputPath);
			try {
				File file = new File(position_configuration_outputPath);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStr = new FileOutputStream(file);
				BufferedOutputStream buff = new BufferedOutputStream(outStr);
				String configure = positionPropertiesArea.getText();
				configure = configure + "\n" + "positioning_algorithm="
						+ positionAlgorithmComboBox.getSelectedItem().toString() + "\n";
				configure = configure + "\n" + "input_rssi_path=" + txtRssiInputPath.getText() + "\n";
				buff.write(configure.getBytes());
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		private String createPositioningOutputDir(String outputPath) {
			// TODO Auto-generated method stub

			Date date = new Date(System.currentTimeMillis());
			String time = IdrObjsUtility.dir_sdf.format(date);

			File dir = new File(outputPath);

			File positionDir = new File(outputPath + "//indoor positioning data");

			if (!dir.exists() && !dir.isDirectory()) {
				dir.mkdirs();
			} else {
				System.out.println("Dir already exists");
			}

			positionDir.mkdirs();

			if (!positionDir.exists() && !positionDir.isDirectory() && chckbxPositiongData.isSelected()) {
				positionDir.mkdirs();
			}
			File cur_positionDir = new File(positionDir.getPath() + "//" + time);
			if (!cur_positionDir.exists() && !cur_positionDir.isDirectory() && chckbxTracking.isSelected()) {
				cur_positionDir.mkdirs();
			}

			return cur_positionDir.getPath();

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
					txtselectedNameField.setText("");
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
					updateSelectPartsList();

					// possibleConnectedPartsList.clear();
					// updatePossibleConnectedPartsList();
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
}
