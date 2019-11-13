package com.indoorobject.utility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.khelekore.prtree.PRTree;

import com.database.DB_WrapperLoad;
import com.database.spatialobject.AccessPoint;
import com.database.spatialobject.Floor;
import com.database.spatialobject.Partition;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.scheduledexecutor.IScheduledExecutorService;
import com.indoorobject.IndoorObjsFactory;
import com.indoorobject.movingobject.MovingObj;
import com.indoorobject.movingobject.MultiDestinationMovement;
import com.indoorobject.station.Station;
import com.trajectory.Trajectory;
import com.trajectory.VisualTrajectory;

public class IdrObjsUtility {

	public static PRTree<Partition> partTree;
	public static PRTree<Station> stationTree;
	public static String outputDir;
	public static String rssiDir;
	public static String trajDir;
	public static String snapshotDir;

	public static Hashtable<Integer, Station> allStations = new Hashtable<Integer, Station>();

	public static int rp_id_count = 0;
	public static int writedTrajectory = 0;	

	public static SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
	public static SimpleDateFormat dir_sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	public static Date objectGenerateStartTime = null;
	public static Date objectGenerateEndTime = null;
	public static Date startClickedTime = null;

	public static IndoorObjsFactory initlizer;

	public static ArrayList<MovingObj> tempObjs;
	public static ArrayList<Floor> floors;

	public static String startCalendar;
	public static String endCalendar;
	
	public static long startTime;
	public static long endTime;
	
	public static boolean isStart = false;
	
	public static ScheduledExecutorService scheduler =
   	     Executors.newScheduledThreadPool(1);

	public synchronized static void paintMovingObjs(Floor chosenFloor, Graphics2D g2, 
			AffineTransform tx, Stroke pen1, ArrayList<MovingObj> movingObjs, 
			Color color) {

		g2.setColor(color);
		g2.setStroke(pen1);
		List<MovingObj> toDeleteMovingObjs = new ArrayList<>();

		for (MovingObj movingObj : movingObjs) {
			if (movingObj instanceof MultiDestinationMovement) {
				if (((MultiDestinationMovement) movingObj).isFinished()) {
					toDeleteMovingObjs.add(movingObj);
					System.out.println(movingObj.getId() + " is passed");
					continue;
				}
			} else if (movingObj.isArrived()) {
				toDeleteMovingObjs.add(movingObj);
				System.out.println(movingObj.getId() + " is passed");
				continue;
			}

			if (!movingObj.isActive()) {
				continue;
			}

			if (movingObj.getCurrentFloor() == chosenFloor) {
				Point2D.Double currentLocation = movingObj.getCurrentLocation();
				Rectangle2D.Double rectangleTest = new Rectangle2D.Double(currentLocation.getX(),
						currentLocation.getY(), 0.5, 0.5);
				Path2D rectangleNew = (Path2D) tx.createTransformedShape(rectangleTest);
				g2.fill(rectangleNew);
			}
		}

		toDeleteMovingObjs.forEach((o) -> {
			if(o.isWrited() == true) {
				writedTrajectory++;
				endTime = System.currentTimeMillis();
				System.out.println("Number of writed trajectory:" + writedTrajectory);
				System.out.println("Current time execution:" + endTime);
			}
			movingObjs.remove(o);
			System.out.println("remove "+ o.getId());
			System.out.println("number of visitor: "+movingObjs.size());
		});

		// if(!toDeleteMovingObjs.isEmpty()) {
		if(isStart) {
			if(movingObjs.size() < 10) {
				// Generate new movingObj
				// genNewMovingObj(floors, toDeleteMovingObjs.size());
				genNewMovingObj(floors, 1);

				// Combine array list first
				movingObjs.addAll(tempObjs);			

				// rerun new movingObj
				for (MovingObj movingObj : movingObjs) {
					// Check if already active
					if(!movingObj.isActive()) {
						if (movingObj instanceof MultiDestinationMovement) {
							MultiDestinationMovement multiDestCustomer = (MultiDestinationMovement) movingObj;
							Timer timer = new Timer();
							scheduler.schedule(new TimerTask() {
								@Override
								public void run() {
									multiDestCustomer.genMultiDestinations();
									System.out.println("New " + multiDestCustomer.getId() + " is generated");
									multiDestCustomer.setActive(true);
									Thread thread = new Thread(multiDestCustomer);
									thread.start();
								}
							}, Math.max(0, multiDestCustomer.getInitMovingTime() - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
						} else {
							Timer timer = new Timer();
							scheduler.schedule(new TimerTask() {
								@Override
								public void run() {
									System.out.println("New " +movingObj.getId() + " is generated");
									movingObj.setActive(true);
									Thread thread = new Thread(movingObj);
									thread.start();
								}
							}, Math.max(0, movingObj.getInitMovingTime() - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
						}
					}
				}
			}
		}
	}

	// Generate new moving object if previous already killed
	public synchronized static void genNewMovingObj(ArrayList<Floor> floors, int cap) {
		tempObjs = new ArrayList<MovingObj>(cap);
		tempObjs.clear(); // Empty first
		for (Floor floor : DB_WrapperLoad.floorT) {
			initlizer.generateMovingObjsOnFloor(floor, tempObjs);
		}
		tempObjs.forEach(tempObj -> {
			tempObj.setInitMovingTime(calGaussianTime());
		});
		System.out.println("Created new "+ tempObjs.size()+" moving objects!");
	}

	public static long calGaussianTime() {
		Random random = new Random();
		try {
			IdrObjsUtility.objectGenerateStartTime = IdrObjsUtility.sdf.parse(startCalendar);
			IdrObjsUtility.objectGenerateEndTime = IdrObjsUtility.sdf.parse(endCalendar);
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

	// Add executor service here
	public synchronized static void genMovingObj(IndoorObjsFactory init, ArrayList<Floor> flrs,
			ArrayList<MovingObj> movingObjs, String startCal, String endCal) throws Exception {
		// Instantiate Hazelcast instance
         HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
         IScheduledExecutorService executor = hazelcastInstance.getScheduledExecutorService("exec");
        
		// Start the calculation time
		startTime = System.currentTimeMillis();
		
		// Create instance of factory
		initlizer = init;
		floors = flrs;
		startCalendar = startCal;
		endCalendar = endCal;

		// check if moving object is zero
		if(movingObjs.size() <= 0) {
			System.out.println("no visitor in building!");
		}

		for (MovingObj movingObj : movingObjs) {
			if (movingObj instanceof MultiDestinationMovement) {
				MultiDestinationMovement multiDestCustomer = (MultiDestinationMovement) movingObj;
				executor.schedule(new TimerTask() {
					@Override
					public void run() {
						multiDestCustomer.genMultiDestinations();
						System.out.println("Multi Destination moving object "+ multiDestCustomer.getId() + " is activated");
						multiDestCustomer.setActive(true);
						Thread thread = new Thread(multiDestCustomer);
						thread.start();
					}
				}, Math.max(0, multiDestCustomer.getInitMovingTime() - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
			} else {
				executor.schedule(new TimerTask() {
					@Override
					public void run() {
						System.out.println( "Single Destination moving object " + movingObj.getId() + " is activated");
						movingObj.setActive(true);
						Thread thread = new Thread(movingObj);
						thread.start();
					}
				}, Math.max(0, movingObj.getInitMovingTime() - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
			}
		}
	}

	public static void paintStations(Floor floor, Graphics2D g2, AffineTransform tx, Stroke pen1, Color color) {

		g2.setStroke(pen1);
		for (Station station : floor.getStations()) {
			Point2D.Double currentLocation = station.getCurrentLocation();
			Ellipse2D.Double ellipse = new Ellipse2D.Double(currentLocation.getX(), currentLocation.getY(), 0.8, 0.8);
			Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);
			// g2.draw(ellipseNew);
			g2.setColor(color);
			g2.fill(ellipseNew);

			Color borderColor = new Color(245, 166, 35);
			g2.setColor(borderColor);
			g2.draw(ellipseNew);
		}
	}

	public static void paintSingleTrajectories(Floor floor, Graphics2D g2, AffineTransform tx, Stroke pen1,
			ArrayList<VisualTrajectory> trajectories) {

		g2.setStroke(pen1);
		Color color = new Color(255, 80, 80);

		int i = 0;
		for (VisualTrajectory vt : trajectories) {
			for (Trajectory t : vt.getTrajectories()) {
				if (t.getFloorId() == floor.getItemID()) {
					if (i == 0 || i == vt.getTrajectories().size() - 1) {
						BufferedImage image = null;

						try {
							image = ImageIO.read(IdrObjsUtility.class.getResource("/com/gui/marker.png"));
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}

						g2.drawImage(image,(int) t.getAxis(),(int) t.getOordinat(), null);
						//						Rectangle2D.Double ellipse = new Rectangle2D.Double(t.getAxis(), t.getOordinat(), 0.45, 0.45);
						//						Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);
						//
						//						Color background = new Color(255, 255, 255);
						//						g2.setColor(background);
						//						g2.fill(ellipseNew);
						//
						//						g2.setColor(vt.getColor());
						//						g2.draw(ellipseNew);
					} else {
						Ellipse2D.Double ellipse = new Ellipse2D.Double(t.getAxis(), t.getOordinat(), 0.15, 0.15);
						Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);

						// g2.draw(ellipseNew);
						g2.setColor(vt.getColor());
						g2.fill(ellipseNew);

						g2.setColor(vt.getColor());
						g2.draw(ellipseNew);
					}
					i++;
				}				
			}
		}
	}

	public static void paintRegionTrajectories(Floor floor, Graphics2D g2, AffineTransform tx, Stroke pen1,
			Map<Integer, ArrayList<Trajectory>> trajectories) {

		g2.setStroke(pen1);
		trajectories.forEach((k, v) -> {
			Random rand = new Random();
			// Java 'Color' class takes 3 floats, from 0 to 1.
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();

			Color color = new Color(r, g, b);

			int i = 0;
			for (Trajectory t : v) {
				if (t.getFloorId() == floor.getItemID()) {
					if (i == 0 || i == trajectories.size() - 1) {
						//	        			BufferedImage image = null;
						//	        			
						//	                    try {
						//	                        image = ImageIO.read(IdrObjsUtility.class.getResource("/cn/edu/zju/db/datagen/gui/marker.png"));
						//	                    } catch (IOException ioe) {
						//	                        ioe.printStackTrace();
						//	                    }
						//	                    
						//	                    g2.drawImage(image,(int) t.getAxis(),(int) t.getOordinat(), null);
						Rectangle2D.Double ellipse = new Rectangle2D.Double(t.getAxis(), t.getOordinat(), 0.45, 0.45);
						Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);

						// g2.draw(ellipseNew);
						Color background = new Color(255, 255, 255);
						g2.setColor(background);
						g2.fill(ellipseNew);

						g2.setColor(color);
						g2.draw(ellipseNew);
					} else {
						Ellipse2D.Double ellipse = new Ellipse2D.Double(t.getAxis(), t.getOordinat(), 0.15, 0.15);
						Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);

						// g2.draw(ellipseNew);
						g2.setColor(color);
						g2.fill(ellipseNew);

						g2.setColor(color);
						g2.draw(ellipseNew);
					}

					i++;
				}
			}
		});
	}

	public static void paintRegionTrajectories(Floor floor, Graphics2D g2, AffineTransform tx, Stroke pen1,
			ArrayList<VisualTrajectory> trajectories) {

		g2.setStroke(pen1);
		for (VisualTrajectory vt : trajectories) {
			int i = 0;
			for (Trajectory t : vt.getTrajectories()) {
				if (t.getFloorId() == floor.getItemID()) {
					Ellipse2D.Double ellipse = new Ellipse2D.Double(t.getAxis(), t.getOordinat(), 0.15, 0.15);
					Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);

					// g2.draw(ellipseNew);
					g2.setColor(vt.getColor());
					g2.fill(ellipseNew);

					g2.setColor(vt.getColor());
					g2.draw(ellipseNew);
				}
			}
		}
	}

	public static void paintTrajectories(Floor floor, Graphics2D g2, AffineTransform tx, Stroke pen1,
			ArrayList<ArrayList<Trajectory>> trajectories) {

		g2.setStroke(pen1);
		for (ArrayList<Trajectory> temp : trajectories) {
			Random rand = new Random();
			// Java 'Color' class takes 3 floats, from 0 to 1.
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();

			Color color = new Color(r, g, b);

			int i = 0;
			for (Trajectory t : temp) {
				if (t.getFloorId() == floor.getItemID()) {
					Ellipse2D.Double ellipse = new Ellipse2D.Double(t.getAxis(), t.getOordinat(), 0.15, 0.15);
					Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);

					// g2.draw(ellipseNew);
					g2.setColor(color);
					g2.fill(ellipseNew);

					g2.setColor(color);
					g2.draw(ellipseNew);
				}
			}
		}
	}

	public static void paintHeatTrajectories(Floor floor, Graphics2D g2, AffineTransform tx, Stroke pen1,
			ArrayList<Trajectory> trajectories) {

		g2.setStroke(pen1);
		for (Trajectory t : trajectories) {
			if (t.getFloorId() == floor.getItemID()) {
				Ellipse2D.Double ellipse = new Ellipse2D.Double(t.getAxis(), t.getOordinat(), 3, 3);
				Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);
				// g2.draw(ellipseNew);
				Color color = new Color(51, 255, 51);
				g2.setColor(color);
				g2.fill(ellipseNew);

				//                Color borderColor = new Color(200, 29, 37);
				//                g2.setColor(borderColor);
				g2.draw(ellipseNew);
			}
		}

		for (Trajectory t : trajectories) {
			if (t.getFloorId() == floor.getItemID()) {
				Ellipse2D.Double ellipse = new Ellipse2D.Double(t.getAxis(), t.getOordinat(), 2, 2);
				Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);
				// g2.draw(ellipseNew);
				Color color = new Color(255, 255, 0);
				g2.setColor(color);
				g2.fill(ellipseNew);

				//                Color borderColor = new Color(200, 29, 37);
				//                g2.setColor(borderColor);
				g2.draw(ellipseNew);
			}
		}

		for (Trajectory t : trajectories) {
			if (t.getFloorId() == floor.getItemID()) {
				Ellipse2D.Double ellipse = new Ellipse2D.Double(t.getAxis(), t.getOordinat(), 0.5, 0.5);
				Path2D ellipseNew = (Path2D) tx.createTransformedShape(ellipse);
				// g2.draw(ellipseNew);
				Color color = new Color(200, 29, 37);
				g2.setColor(color);
				g2.fill(ellipseNew);

				//                Color borderColor = new Color(200, 29, 37);
				//                g2.setColor(borderColor);
				g2.draw(ellipseNew);
			}
		}
	}

	public static void MovingToDestination(Floor floor1, Floor floor2, ArrayList<MovingObj> movingObjs) {
		System.out.println(floor1);
		System.out.println(floor2);
		Point2D.Double point1 = new Point2D.Double(353, 200);
		//        Point2D.Double point1 = new Point2D.Double(343, 250);
		Partition part1 = findPartitionForPoint(floor1, point1);
		MultiDestinationMovement obj1 = new MultiDestinationMovement(floor1, point1);
		obj1.setCurrentPartition(part1);
		Point2D.Double point2 = new Point2D.Double(405, 180);
		Partition part2 = findPartitionForPoint(floor2, point2);
		MultiDestinationMovement obj2 = new MultiDestinationMovement(floor2, point2);
		obj2.setCurrentPartition(part2);

		obj1.setCurDestPoint(point2);
		obj1.setCurDestPartition(part2);
		obj1.setCurDestFloor(floor2);
		movingObjs.add(obj1);
		movingObjs.add(obj2);

		System.out.println("obj1: " + floor1 + " " + part1);
		System.out.println("obj2: " + floor2 + " " + part2);
		if (part1 == null || part2 == null) {
			System.out.println("Outdoor");
		} else {
			obj1.genMultiDestinations();
			//            obj1.runSingleThread();
			Thread thread = new Thread(obj1);
			thread.run();
		}

	}

	// generate a r-tree for all the partitions on a floor
	// so we can find a point's partition quickly
	public static PRTree<Partition> generatePartRTree(Floor floor) {
		ArrayList<Partition> partitions = floor.getPartsAfterDecomposed();
		partTree = new PRTree<Partition>(new PartMBRConverter(), 10);
		partTree.load(partitions);
		return partTree;
	}

	// generate a r-tree for all the stations on a floor, so a moving object can
	// find stations in its range quickly
	public static PRTree<Station> generateStationRTree(ArrayList<Station> stations) {
		stationTree = new PRTree<Station>(new StationMBRConverter(), 10);
		stationTree.load(stations);
		return stationTree;
	}

	public static Partition findPartitionForPoint(Floor floor, Point2D.Double point) {
		PRTree<Partition> rtree = floor.getPartitionsRTree();
		if (!rtree.isEmpty()) {
			for (Partition part : rtree.find(point.getX(), point.getY(), point.getX(), point.getY())) {
				if (part.getPolygon2D().contains(point)) {
					return part;
				}
			}
		}
		return null;
	}

	public static List<Partition> findClosePartitions(Floor floor, Partition partition) {
		List<Partition> possibleConPartitions = new ArrayList<>();
		Rectangle2D.Double rectangle = (Rectangle2D.Double) partition.getPolygon2D().getBounds2D();
		double margin = 2;
		Rectangle2D.Double queryRectangle = new Rectangle2D.Double(rectangle.getX() - margin, rectangle.getY() - margin,
				rectangle.getWidth() + 2 * margin, rectangle.getHeight() + 2 * margin);
		for (Partition closePartition : floor.getPartitionsRTree().find(queryRectangle.getMinX(),
				queryRectangle.getMinY(), queryRectangle.getMaxX(), queryRectangle.getMaxY())) {
			if (!closePartition.getItemID().equals(partition.getItemID())) {
				possibleConPartitions.add(closePartition);
			}
		}
		return possibleConPartitions;
	}

	public static void printPartition(Floor floor, ArrayList<Station> stations) {
		PRTree<Partition> rtree = floor.getPartitionsRTree();
		for (Station station : stations) {
			for (Partition part : rtree.find(station.getCurrentLocation().getX(), station.getCurrentLocation().getY(),
					station.getCurrentLocation().getX(), station.getCurrentLocation().getY())) {
				System.out.print(station.getId() + " ");
				System.out.println(part.getName() + " " + part.getItemID());
			}
		}
	}

	public static Partition getAdjacentPartition(AccessPoint AP, Partition partition) {
		for (Partition part : AP.getPartitions()) {
			if (part != partition) {
				return part;
			}
		}
		System.out.println("Single Partition Door");
		return null;
	}

	public static void createOutputDir() {
		// outputDir = System.getProperty("user.dir");
		// Date date = new Date();
		// DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH_mm");
		// String time = format.format(date);
		// outputDir = outputDir + "//export files//" + time;
		// rssiDir = outputDir + "//raw rssi";
		// trajDir = outputDir + "//raw trajectory";
		// File dir = new File(outputDir);
		// File rssiDirFile = new File(rssiDir);
		// File trajDirFile = new File(trajDir);
		// if(!dir.exists() && !dir.isDirectory()){
		// dir.mkdirs();
		// rssiDirFile.mkdirs();
		// trajDirFile.mkdirs();
		// }else{
		// System.out.println("Dir already exists");
		// }
	}

	public static void changeAllAlgInputPath() {
		changeAlgInputPath("conf/trilateration.properties");
		changeAlgInputPath("conf/fingerprint.properties");
		changeAlgInputPath("probablistic.properties");
	}

	private static void changeAlgInputPath(String fileName) {
		try {
			FileInputStream in = new FileInputStream(fileName);
			Properties props = new Properties();
			props.load(in);
			in.close();
			FileOutputStream out = new FileOutputStream(fileName);
			props.setProperty("inputpath", rssiDir);
			props.store(out, "update message");
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
