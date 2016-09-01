import java.awt.Color;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Fractal
{
	public static final float YELLOW = 1f / 6f;
	public static final float CYAN = .5f;
	public static final float MAGENTA = 5f / 6f;
	public static final float RED = 0f;
	public static final float GREEN = 1f/3f;
	public static final float BLUE = 2f / 3f;

	private static final List<String> validTags;
	private static final Map<String,String> tagDefault;
	private static final Map<String,String> tagDesc;
	private static final Map<String,String> tagHelp;
	private static final String ls = System.lineSeparator();

	static {
		int numTags = 11;
		HashMap<String,String> desc = new HashMap<String,String>(numTags);
		HashMap<String,String> help = new HashMap<String,String>(numTags);
		HashMap<String,String> def = new HashMap<String,String>(numTags);
		ArrayList<String> tags = new ArrayList<String>(numTags);
		String tag;

		tag = "-x";
		tags.add(tag);
		def.put(tag, "-0.75");
		desc.put(tag, "\t-x      \tSpecify the center x-coordinate of the output" + ls);
		help.put(tag, "\t-x [val] use: \tSpecify the horizontal shift of the center of the" + ls
				+ "\t        \timage on the Cartesian Coordinate Plane" + ls);

		tag = "-y";
		tags.add(tag);
		def.put(tag, "0.0");
		desc.put(tag, "\t-y      \tSpecify the center y-coordinate of the output" + ls);
		help.put(tag, "\t-y [val] use: \tSpecify the vertical shift of the center of the" + ls
				+ "\t        \timage on the Cartesian Coordinate Plane" + ls);

		tag = "-z";
		tags.add(tag);
		def.put(tag, "0.0");
		desc.put(tag, "\t-z      \tSpecify the magnitude of the zoom" + ls);
		help.put(tag, "\t-z [val] use: \tSpecify magnitude by which to zoom into the image" + ls
				+ "\t        \tMust be a decimal number" + ls
				+ "\t        \tActual zoom level is 2^zoom_mag" + ls);

		tag = "-e";
		tags.add(tag);
		def.put(tag, "2.0");
		desc.put(tag, "\t-e      \tSpecify the exponent to use in the fractal generation equation" + ls);
		help.put(tag, "\t-e [val] use: \tSpecify the exponent to use in the generation algorithm" + ls
				+ "\t        \tMust be a decimal number that is not zero or one" + ls
				+ "\t        \tExponents outside of two and three are untested" + ls
				+ "\t        \tLarge positive or negative exponents will likely cause" + ls
				+ "\t        \tdrastic increase in processing time" + ls);

		tag = "-b";
		tags.add(tag);
		def.put(tag, "2.0");
		desc.put(tag, "\t-b      \tSpecify the bailout limit to use in the fractal generation process" + ls);
		help.put(tag, "\t-b [val] use: \tSpecify the maximum bailout to use in the generation algorithm" + ls
				+ "\t        \tMust be a positive decimal number greater than one" + ls
				+ "\t        \tBailout values other than two have unpredictable results" + ls
				+ "\t        \tLower bailout values may cause premature aborting of the calculation, and" + ls
				+ "\t        \tHigher bailout values may cause unnecessary increase in processing time" + ls);

		tag = "-cm";
		tags.add(tag);
		def.put(tag, "1.0");
		desc.put(tag, "\t-cm     \tSpecify the amount by which to scale the color" + ls);
		help.put(tag, "\t-cm [val] use:\tSpecify the color scale factor" + ls
				+ "\t        \tIncrease color variation with a number > 1" + ls
				+ "\t        \tDecrease color variation with a number < 1" + ls
				+ "\t        \tMust be a positive non-zero decimal number" + ls);

		tag = "-ca";
		tags.add(tag);
		def.put(tag, "0.0");
		desc.put(tag, "\t-ca     \tSpecify the amount to increase the hue of the pixels by" + ls);
		help.put(tag, "\t-ca [val] use:\tSpecify the color hue shift factor" + ls
				+ "\t        \tMust be a positive number in the range 0 < x < 1" + ls);

		tag = "-i";
		tags.add(tag);
		def.put(tag, "0");
		desc.put(tag, "\t-i      \tSpecify the maximum nuber of iterations per pixel before bailout" + ls);
		help.put(tag, "\t-i [val] use: \tSpecify the iteration bailout upper limit" + ls
				+ "\t        \tMust be a positive non-zero integer" + ls
				+ "\t        \tA higher number will increase the possible color range" + ls
				+ "\t        \tA lower number will decrease the possible color range" + ls
				+ "\t        \tA higher iteration bailout limit will generally slow the program down" + ls
				+ "\t        \tA lower iteration bailout limit will generally speed the program up" + ls);

		tag = "-r";
		tags.add(tag);
		def.put(tag, "1");
		desc.put(tag, "\t-r      \tSpecify the magnitude to scale the resolution up by" + ls);
		help.put(tag, "\t-r [val] use: \tSpecify the magnitude by which to scale the image resolution up by" + ls
				+ "\t        \tMust be a positive non-zero integer" + ls
				+ "\t        \tLarge values (i.e. 16) are unable to be processed" + ls
				+ "\t        \tA value of 1 will output a 1920x1080 image, the default size" + ls
				+ "\t        \tA value of 2 will output a 3840x2160 image, a 2K image" + ls
				+ "\t        \tThe run time is related to the square of the resolution factor" + ls);

		tag = "-t";
		tags.add(tag);
		def.put(tag, "false");
		desc.put(tag, "\t-t      \tSpecify that the image should be generated with multithreaded processing" + ls);
		help.put(tag, "\t-t       use: \tFlag that indicates to the program that multithreaded" + ls
				+ "\t        \tprocessing should be used" + ls
				+ "\t        \tThis flag accepts no arguments" + ls
				+ "\t        \tThis feature is not extensively tested and may bring about instability" + ls);

		tag = "-o";
		tags.add(tag);
		def.put(tag, System.getProperty("user.home"));
		desc.put(tag, "\t-o      \tSpecify the ouput directory" + ls);
		help.put(tag, "\t-o [val] use: \tSpecify which folder to output the image to" + ls
				+ "\t        \tMust be an existing folder" + ls
				+ "\t        \tCannot be a file path name, existing or otherwise" + ls);


		tags.trimToSize();

		validTags = Collections.<String>unmodifiableList(tags);
		tagDefault = Collections.<String,String>unmodifiableMap(def);
		tagDesc = Collections.<String,String>unmodifiableMap(desc);
		tagHelp = Collections.<String,String>unmodifiableMap(help);
	}

	private static boolean xLocSet = false, yLocSet = false, zoomSet = false, expSet = false;
	private static boolean colorMultSet = false, colorAddSet = false, maxItSet = false;
	private static boolean resMultSet = false, threadedSet = false, outputSet = false;
	private static boolean bailSet = false;



	public static void main(String[] args) throws Exception
	{
		long startT = System.nanoTime();

		if (args.length == 0
				|| args[0].equalsIgnoreCase("help")
				|| args[0].equalsIgnoreCase("?")
				|| args[0].equalsIgnoreCase("-help")
				|| args[0].equalsIgnoreCase("/?")
				|| args[0].equalsIgnoreCase("\\?")
				|| args[0].equalsIgnoreCase("/help")
				|| args[0].equalsIgnoreCase("\\help")
				|| args[0].equalsIgnoreCase("-h")
				|| args[0].equalsIgnoreCase("-?"))
		{
			if (args.length > 1)
				printHelp(args[1]);
			else
				printHelp(null);

			return;
		}

		args = parseArgs(args);

		double[] dVals = new double[5];
		float[] fVals = new float[2];
		int[] iVals = new int[2];
		boolean[] boolVals = new boolean[1];
		File output;

		int dStart = 0;
		int fStart = dStart + dVals.length;
		int iStart = fStart + fVals.length;
		int bStart = iStart + iVals.length;
		int oLoc = bStart + boolVals.length;

		int i;
		for(i = 0; i < dVals.length; i++)
			dVals[i] = Double.valueOf(args[i+dStart]);
		for(i = 0; i < fVals.length; i++)
			fVals[i] = Float.valueOf(args[i+fStart]);
		for(i = 0; i < iVals.length; i++)
			iVals[i] = Integer.valueOf(args[i+iStart]);
		for(i = 0; i < boolVals.length; i++)
			boolVals[i] = Boolean.valueOf(args[i+bStart]);
		output = new File(args[oLoc]);

		Fractal f = new Fractal(dVals[0], dVals[1], dVals[2], dVals[3], dVals[4],
				fVals[1], fVals[0], iVals[1], iVals[0], output);

		if(boolVals[0])
			f.generateMultithreaded();
		else
			f.generate();
		f.display();
		f.saveImage();

		long endT = System.nanoTime();
		long time = endT - startT;
		long days = time / (1000000000L * 60 * 60 * 24);
		long hours = time / (1000000000L * 60L * 60L) % 24;
		long minutes = time / (1000000000L * 60L) % 60;
		long seconds = time / 1000000000L % 60;
		long milliseconds = time / 1000000L % 1000;
		long microseconds = time / 1000L % 1000;
		long nanoseconds = time % 1000;

		String timeString = "";

		if (days > 0)
			timeString += days + " days ";
		if (hours > 0)
			timeString += hours + "hr ";
		if (minutes > 0)
			timeString += minutes + "min ";
		if (seconds > 0)
			timeString += seconds + "sec ";
		if (milliseconds > 0)
			timeString += milliseconds + "ms ";
		if (microseconds > 0)
			timeString += String.valueOf(microseconds) + (char) 0xb5 + "s ";
		if (nanoseconds > 0)
			timeString += nanoseconds + "ns";

		System.out.println("Total Time:   " + timeString);
	}

	private static IllegalArgumentException repeatArg(String argName)
	{
		return new IllegalArgumentException("Can only set " + argName + " once");
	}

	private static String[] parseArgs(String[] arguments) throws Exception
	{
		int i = 0;
		Object[] hold;
		Map<String,String> out = new HashMap<String,String>();
		while (i < arguments.length)
		{
			if(!isValidTag(arguments[i]))
				throw new IllegalArgumentException("Invalid Tag: " + arguments[i]);
			hold = parseTag(arguments[i], arguments[i+1]);
			i += (int)hold[2];
			out.put((String)hold[0], (String)hold[1]);
		}
		List<String> args = new ArrayList<String>(validTags.size());
		for(i = 0; i < validTags.size(); i++)
		{
			if(out.containsKey(validTags.get(i)))
				args.add(out.get(validTags.get(i)));
			else
				args.add(tagDefault.get(validTags.get(i)));
		}
		return args.toArray(new String[0]);
	}

	private static Object[] parseTag(String tag, String nextArg) throws Exception
	{
		tag = tag.toLowerCase();
		switch(tag)
		{
		case "-x":
			if(xLocSet)
				throw repeatArg("x Location");
			double tempX = Double.parseDouble(nextArg);
			if (Double.isInfinite(tempX) || Double.isNaN(tempX))
				throw new IllegalArgumentException("Invalid Argument for x-center: " + nextArg);
			xLocSet = true;
			return new Object[] {tag, String.valueOf(tempX), 2};
		case "-y":
			if(yLocSet)
				throw repeatArg("y Location");
			double tempY = Double.parseDouble(nextArg);
			if (Double.isInfinite(tempY) || Double.isNaN(tempY))
				throw new IllegalArgumentException("Invalid Argument for y-center: " + nextArg);
			yLocSet = true;
			return new Object[] {tag, String.valueOf(tempY), 2};
		case "-z":
			if(zoomSet)
				throw repeatArg("zoom magnitude");
			double tempZ = Double.parseDouble(nextArg);
			if (Double.isInfinite(tempZ) || Double.isNaN(tempZ))
				throw new IllegalArgumentException("Invalid Zoom Magnitude: " + nextArg);
			zoomSet = true;
			return new Object[] {tag, String.valueOf(tempZ), 2};
		case "-e":
			if(expSet)
				throw repeatArg("exponent");
			double tempE = Double.parseDouble(nextArg);
			if(Double.isInfinite(tempE) || Double.isNaN(tempE))
				throw new IllegalArgumentException("Invalid Exponent: " + nextArg);
			expSet = true;
			return new Object[] {tag, String.valueOf(tempE), 2};
		case "-b":
			if(bailSet)
				throw repeatArg("bailout value");
			double tempB = Double.parseDouble(nextArg);
			if(Double.isInfinite(tempB) || Double.isNaN(tempB) || tempB <= 1.0)
				throw new IllegalArgumentException("Invalid Bailout Value: " + nextArg);
			bailSet = true;
			return new Object[] {tag, String.valueOf(tempB), 2};
		case "-cm":
			if(colorMultSet)
				throw repeatArg("hue multiplier value");
			float tempCM = Float.parseFloat(nextArg);
			if (Float.isNaN(tempCM) || Float.isInfinite(tempCM) || tempCM <= 0.0f)
				throw new IllegalArgumentException("Invalid Color Multiplier Value: " + nextArg);
			colorMultSet = true;
			return new Object[] {tag, String.valueOf(tempCM), 2};
		case "-ca":
			if(colorAddSet)
				throw repeatArg("color addition value");
			float tempCA = Float.parseFloat(nextArg);
			if (Float.isNaN(tempCA) || Float.isInfinite(tempCA))
				throw new IllegalArgumentException("Invalid Color Addition Value: " + nextArg);
			else if (tempCA >= 1.0f || tempCA < 0.0f)
				throw new IllegalArgumentException("Color Addition Value must be between 0 and 1: " + tempCA);
			colorAddSet = true;
			return new Object[] {tag, String.valueOf(tempCA), 2};
		case "-i":
			if(maxItSet)
				throw repeatArg("maximum number of iterations");
			String tmp = nextArg.toLowerCase();
			int tempI;
			if (tmp.startsWith("-"))
				throw new IllegalArgumentException("Iterations cannot be set to a negative value: " + tmp);
			else if (tmp.startsWith("0x"))
				tempI = Integer.parseInt(tmp.substring(2), 16);
			else if (tmp.startsWith("0b"))
				tempI = Integer.parseInt(tmp.substring(2), 2);
			else if (tmp.startsWith("0"))
				tempI = Integer.parseInt(tmp.substring(1), 8);
			else
				tempI = Integer.parseInt(tmp, 10);
			maxItSet = true;
			return new Object[] {tag, String.valueOf(tempI), 2};
		case "-r":
			if(resMultSet)
				throw repeatArg("resolution scale factor");
			int tempR = Integer.parseInt(nextArg);
			if (tempR <= 0)
				throw new IllegalArgumentException("Invalid Resolution Multiplier: " + tempR);
			resMultSet = true;
			return new Object[] {tag, String.valueOf(tempR), 2};
		case "-t":
			if(threadedSet)
				throw repeatArg("multithreaded mode");
			threadedSet = true;
			return new Object[] {tag, String.valueOf(true), 1};
		case "-o":
			File tempO = new File(nextArg);
			if (!tempO.isDirectory())
				throw new IOException("Invalid Ouput Path - Must be a directory: " + tempO.getAbsolutePath());
			return new Object[] {tag, tempO.getAbsolutePath(), 2};
		}
		throw new IllegalArgumentException("Invalid Tag: " + tag);
	}

	private static boolean isValidTag(String arg)
	{
		for(String tag : validTags)
			if(arg.equalsIgnoreCase(tag))
				return true;
		return false;
	}

	private static void printHelp(String tag)
	{
		if (tag == null)
		{
			System.out.print("\tArgument\tDescription" + ls);
			System.out.print(ls);
			for(String t : validTags)
				System.out.print(tagDesc.get(t));
			System.out.print(ls);
			System.out.print("\tFor help with a specific argument, enter help <arg>" + ls);
			System.out.print("\tTo list detailed help for all parameters, enter help all" + ls);
			return;
		}

		if(tag.equalsIgnoreCase("all"))
			for(String t : validTags)
				printHelp(t);
		else if(isValidTag(tag.toLowerCase()))
			System.out.print(tagHelp.get(tag.toLowerCase()));
		else
			throw new IllegalArgumentException("Invalid Help Tag: " + tag);
	}


	private BufferedImage img;
	private JFrame frame;
	private JPanel panel;
	private File out;

	private final int height, width, resFactor, maxIteration;
	private final double xCenter, xMax, xMin;
	private final double yCenter, yMax, yMin;
	private final double zoom;
	private final double exp, bail, bail2;
	private final float colorConstant, colorFactor;

	private int pixels, percent;

	public Fractal(double xLocation, double yLocation, double zoomMag, double exponent, double bailout,
			float colorAddition, float colorMult, int resMult, File outputLocation)
	{
		this(xLocation, yLocation, zoomMag, exponent, bailout, colorAddition, colorMult, resMult, 0, outputLocation);
	}

	public Fractal(double xLocation, double yLocation, double zoomMag, double exponent, double bailout,
			float colorAddition, float colorMult, int resMult, int maxIterate, File outputLocation)
	{
		xCenter = xLocation;
		yCenter = yLocation;
		zoom = Math.pow(2, zoomMag);

		exp = exponent;
		bail = bailout;
		bail2 = Math.pow(bail, 2);

		xMax = xCenter + 16d/9d / zoom;
		xMin = xCenter - 16d/9d / zoom;
		yMax = yCenter - 1d / zoom;
		yMin = yCenter + 1d / zoom;

		if(maxIterate <= 0)
			maxIteration = 75 + (int)Math.round(5*Math.pow(1.85, Math.log1p(zoom*2)));
		else
			maxIteration = maxIterate;

		colorConstant = colorAddition;
		colorFactor = colorMult;

		resFactor = resMult;

		width = 1920*resFactor;
		height = 1080*resFactor;

		out = outputLocation.getAbsoluteFile();

		pixels = percent = 0;
	}

	public void generate()
	{
		System.out.println("Begin Generation");

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		int iterations;
		double mag;

		double xScale = (xMax-xMin)/width;
		double yScale = (yMax-yMin)/height;

		Complex p1 = new Complex(exp, 0.);
		Complex p2 = new Complex(1./exp, 0.);
		Complex z0, z;
		float hue;

		System.out.println("Evaluating and Coloring Pixels...");
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				z0 = new Complex(x*xScale + xMin, y*yScale + yMin);
				z = new Complex();

				for(iterations = 0; iterations < maxIteration && z.Re()*z.Re() + z.Im()*z.Im() < bail2; iterations++)
				{
					z = z.pow(p1).add(z0);
				}
				mag = z.subtract(z0).pow(p2).multiply(Complex.NEGATIVE_ONE).abs();

				hue = (float)( (iterations+(bail2 - mag)/(bail2 - bail))/maxIteration ) * colorFactor + colorConstant;
				img.setRGB(x, y, Color.HSBtoRGB(hue, 1, 1));

				incPixels();
			}
		}
		System.out.println("Complete");
	}

	public void generateMultithreaded() throws Exception
	{
		System.out.println("Begin Multithreaded Generation");

		int proc = Runtime.getRuntime().availableProcessors();

		while(width % proc != 0 && height % proc != 0)
			proc--;

		int numParts = proc*proc;

		if(proc == 1)
		{
			generate();
			return;
		}

		int partWidth = width/proc;
		int partHeight = height/proc;

		double[] xLower = new double[numParts];
		double[] xUpper = new double[numParts];
		double[] yLower = new double[numParts];
		double[] yUpper = new double[numParts];
		double xRange = xMax-xMin;
		double yRange = yMax-yMin;

		double xFactor = xRange / (1d*proc);
		double yFactor = yRange / (1d*proc);

		for(int x = 0; x < proc; x++)
		{
			for(int y = 0; y < proc; y++)
			{
				xLower[y*proc+x] = xMin + xFactor*(x+0);
				xUpper[y*proc+x] = xMin + xFactor*(x+1);
				yLower[y*proc+x] = yMin + yFactor*(y+0);
				yUpper[y*proc+x] = yMin + yFactor*(y+1);
			}
		}

		System.out.println("Setting up Threads");
		ExecutorService pool = Executors.newFixedThreadPool(proc);
		ArrayList<Callable<Object[]>> tasks = new ArrayList<Callable<Object[]>>(numParts);

		for(int x = 0; x < proc; x++)
			for(int y = 0; y < proc; y++)
				tasks.add(new ParallelFractal(
						xLower[y*proc+x], xUpper[y*proc+x],
						yLower[y*proc+x], yUpper[y*proc+x],
						x, y, partWidth, partHeight));
		tasks.trimToSize();
		xLower = xUpper = yLower = yUpper = null;

		List<Future<Object[]>> parts = null;

		try {
			System.out.println("Evaluating...");
			parts = pool.invokeAll(tasks);
		}catch(Exception e) {e.printStackTrace(); System.exit(1);}
		tasks = null;

		List<Object[]> output = new ArrayList<Object[]>(parts.size());

		for(Future<Object[]> i : parts)
			output.add(i.get());

		pool.shutdownNow();
		pool = null;
		parts = null;

		int[][] color = new int[width][height];
		int[][] rgb;
		int x0, y0;

		for(Object[] i : output)
		{
			x0 = (Integer)i[0]*partWidth;
			y0 = (Integer)i[1]*partHeight;
			rgb = (int[][])i[2];
			for(int x = 0; x < partWidth; x++)
				for(int y = 0; y < partHeight; y++)
					color[x0+x]
							[y0+y] =
							rgb[x]
									[y];
		}

		output = null;
		rgb = null;

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		System.out.println("Coloring Image...");
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				img.setRGB(x, y, color[x][y]);

		System.out.println("Complete");
	}

	private synchronized void incPixels()
	{
		pixels++;
		if((int)((double)pixels/(double)(width*height)*100.) > percent)
			System.out.println("Percent Complete: " + (++percent) + "%");
	}

	private final class ParallelFractal implements Callable<Object[]>
	{
		private final double xMin, xMax, yMin, yMax;
		private final double xScale, yScale;
		private final int width, height, xPart, yPart;
		private final Complex p1, p2;

		ParallelFractal(double xMin, double xMax, double yMin, double yMax, int xPart, int yPart, int width, int height)
		{
			this.xMin = xMin;
			this.xMax = xMax;
			this.yMin = yMin;
			this.yMax = yMax;
			this.xPart = xPart;
			this.yPart = yPart;
			this.width = width;
			this.height = height;

			xScale = (this.xMax-this.xMin)/this.width;
			yScale = (this.yMax-this.yMin)/this.height;

			p1 = new Complex(exp, 0.);
			p2 = new Complex(1./exp, 0.);
		}

		@Override
		public Object[] call() throws Exception
		{
			int[][] rgb = new int[width][height];
			int iterations;
			double mag;
			Complex z0, z;

			float hue;
			for(int x = 0; x < width; x++)
			{
				for(int y = 0; y < height; y++)
				{
					z0 = new Complex(x*xScale + xMin, y*yScale + yMin);
					z = new Complex();

					for(iterations = 0; iterations < maxIteration && z.Re()*z.Re() + z.Im()*z.Im() < bail2; iterations++)
					{
						z = z.pow(p1).add(z0);
					}
					mag = z.subtract(z0).pow(p2).multiply(Complex.NEGATIVE_ONE).abs();

					hue = (float)( (iterations+(bail2 - mag)/(bail2 - bail))/maxIteration );
					hue = hue * colorFactor + colorConstant;

					rgb[x][y] = Color.HSBtoRGB(hue, 1, 1);

					incPixels();
				}
			}

			Object[] out = new Object[3];
			out[0] = xPart;
			out[1] = yPart;
			out[2] = rgb;

			return out;
		}
	}

	public void display()
	{
		System.out.println("Display Image");
		frame = new JFrame();
		panel = new JPanel();
		panel.setSize(1280, 720);

		panel.add(new JLabel(new ImageIcon(img.getScaledInstance(panel.getWidth(),
				panel.getHeight(), Image.SCALE_SMOOTH))));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {}

			@Override
			public void windowClosed(WindowEvent e)
			{
				System.exit(0);
			}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowActivated(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}
		});

		frame.setVisible(true);
	}

	public File saveImage() throws IOException
	{
		System.out.println("Saving Image...");
		String loc = out.getAbsolutePath();
		if(!loc.endsWith(File.separator))
			loc += File.separator;
		String name = "Mandelbrot (" + xCenter + "," + yCenter + ") zoom=" + Math.log(zoom)/Math.log(2d)
				+ ", colorFactor=" + colorFactor + ", colorConstant=" + colorConstant
				+ ", iterations=" + maxIteration + ", exponent=" + exp
				+ ", bailout=" + bail;
		String extension = ".png";
		File output = new File(loc + name + extension);

		name += "_";
		int append = 1;

		while(output.exists())
		{
			output = new File(loc + name + append + extension);
			append++;
		}
		try {
			output.createNewFile();
		}catch(IOException e)
		{
			throw new IOException("Exception Creating Output File:\t" + e.getMessage() + "\t" + output.getAbsolutePath(), e);
		}

		try {
			ImageIO.write(img, "png", output);
		} catch (IOException e)
		{
			throw new IOException("Exception Writing Image:\t\t" + e.getMessage(), e);
		}

		return output;
	}
}
