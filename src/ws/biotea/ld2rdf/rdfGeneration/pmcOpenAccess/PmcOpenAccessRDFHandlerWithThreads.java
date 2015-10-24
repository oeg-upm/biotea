package ws.biotea.ld2rdf.rdfGeneration.pmcOpenAccess;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.ontoware.rdf2go.exception.ModelRuntimeException;

import ws.biotea.ld2rdf.rdfGeneration.Exception.ArticleTypeException;
import ws.biotea.ld2rdf.rdfGeneration.Exception.DTDException;
import ws.biotea.ld2rdf.rdfGeneration.Exception.PMCIdException;
import ws.biotea.ld2rdf.rdfGeneration.Exception.RDFModelIOException;
import ws.biotea.ld2rdf.util.Config;

/**
 * @author Leyla Garcia
 *
 */
public class PmcOpenAccessRDFHandlerWithThreads {	
	//Poolthread sutff
    protected int poolSize;
    protected int maxPoolSize;
    protected long keepAliveTime;
    protected ThreadPoolExecutor threadPool = null;
    protected final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    private Logger logger = Logger.getLogger(PmcOpenAccessRDFHandlerWithThreads.class);

    /**
     * Default constructor, it defines an initial pool with 5 threads, a maximum of 10 threads,
     * and a keepAlive time of 300 seconds.
     */
	public PmcOpenAccessRDFHandlerWithThreads() {
		this(10, 10, 300);
	}
	
	/**
     * Constructor with parameters, it enables to define the initial pool size, maximum pool size,
     * and keep alive time in seconds; it initializes the ThreadPoolExecutor.
     * @param poolSize Initial pool size
     * @param maxPoolSize Maximum pool size
     * @param keepAliveTime Keep alive time in seconds
     */
    protected PmcOpenAccessRDFHandlerWithThreads(int poolSize, int maxPoolSize, long keepAliveTime) {
    	this.poolSize = poolSize;
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);        
    }
	
	/**
     * Run a task with the thread pool and modifies the waiting queue list as needed.
     * @param task
     */
    protected void runTask(Runnable task) {
        threadPool.execute(task);
        logger.debug("Task count: " + queue.size());
    }

    /**
     * Shuts down the ThreadPoolExecutor.
     */
    public void shutDown() {
        threadPool.shutdown();
    }

    /**
     * Informs whether or not the threads have finished all pending executions.
     * @return
     */
    public boolean isTerminated() {
    	//this.handler.getLogger().debug("Task count: " + queue.size());
        return this.threadPool.isTerminated();
    }
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ModelRuntimeException 
	 */
	public static void main(String[] args) throws ModelRuntimeException, IOException {	
		long startTime = System.currentTimeMillis();		

		if (args == null) {
			System.out.println("Usage: -in <xml papers dir> -out <output dir> -sections " +
				"\n-all or -rdfAndAnnotations or any of -rdf -whatizit -ncboAnnotator -bio2rdf (optional, if none present -all)" + 
				"\n-pipeline <pipeline> (optional, whatizitEuropePmcAll by default, only used when -whatizit or all are present) Pipeline to be used by Whatizit AO annotator (values: whatizitEuropePmcGenesProteins, whatizitSwissprot, whatizitEuropePmcAll, etc)" + 
				"\n-sections (optional) If present, paper sections will be rdfized");
			System.exit(0);
		}
		PropertyConfigurator.configure("log4j.properties");					
		
		int initPool = 10, maxPool = 10, keepAlive = 300;
		String inputDir = null, outputDir = null, pipeline = "whatizitEuropePmcAll";
		boolean sections = false;
		Integer limit = null;
		boolean all = false, rdfAndAnnotations = false, rdf = false, whatizit = false, ncboAnnotator = false, bio2rdf = false;
		//boolean addIssuedDate = false;
		for (int i = 0; i < args.length; i++) {
			String str = args[i];
			if (str.equalsIgnoreCase("-in")) {
				inputDir = args[++i];
			} else if (str.equalsIgnoreCase("-out")) {
				outputDir = args[++i];
			} else if (str.equalsIgnoreCase("-sections")) {
				sections = true;
			} else if (str.equalsIgnoreCase("-all")) {
				all = true;
			} else if (str.equalsIgnoreCase("-rdfAndAnnotations")) {
				rdfAndAnnotations = true;
			} else if (str.equalsIgnoreCase("-whatizit")) {
				whatizit = true;
			} else if (str.equalsIgnoreCase("-ncboAnnotator")) {
				ncboAnnotator = true;
			} else if (str.equalsIgnoreCase("-bio2rdf")) {
				bio2rdf = true;
			} else if (str.equalsIgnoreCase("-rdf")) {
				rdf = true;
			} else if (str.equalsIgnoreCase("-pipeline")) {
				pipeline = args[++i];
			} else if (str.equalsIgnoreCase("-initPool")) {
				initPool = Integer.parseInt(args[++i]);
			} else if (str.equalsIgnoreCase("-maxPool")) {
				maxPool = Integer.parseInt(args[++i]);
			} else if (str.equalsIgnoreCase("-keepAlive")) {
				keepAlive = Integer.parseInt(args[++i]);
			}
		}
		
		//Init URI style, if -bio2rdf is present "Bio2RDF" will be used, otherwise "Biotea"
		if (bio2rdf) {
			Config.useBio2RDF = true;
		} else {
			Config.useBio2RDF = false;
		}
		
		//System.out.println("All: " + all + ", rdf: " + rdf + ", whatizit: " + whatizit + ", ncboAnnotator: " + ncboAnnotator + ", bio2rdf: " + bio2rdf + ", pipeline: " + pipeline + ", addIssuedDate: " + addIssuedDate);		
		if (!all && !whatizit && !ncboAnnotator && !bio2rdf && !rdf && !rdfAndAnnotations) { //&& !addIssuedDate
			all = true; 
		}
		if (all) {
			rdf = false; whatizit = false; ncboAnnotator = false; bio2rdf = false; rdfAndAnnotations = false; 
			//addIssuedDate = false;
		}
		
		if ((inputDir == null) || (outputDir == null)) {
			System.out.println("Usage: -in <xml papers dir> -out <output dir> -sections " +
					"\n-all or -rdfAndAnnotations or any of -rdf -whatizit -ncboAnnotator (optional, if none one present -all)" + 
					"\n-bio2rdf (optional, if present Bio2RDF URI style will be used" +
					"\n-pipeline <pipeline> (optional, whatizitEuropePmcAll by default, only used when -whatizit or all are present) Pipeline to be used by Whatizit AO annotator (values: whatizitEuropePmcGenesProteins, whatizitSwissprot, whatizitEuropePmcAll, etc)" + 
					"\n-sections (optional) If present, paper sections will be rdfized");			
			System.out.println("The request cannot be parsed, please check the usage");
			System.exit(0);
		}
		
		System.out.println("Execution variables: " +
			"\nInput " + inputDir + "\nOutput " + outputDir + 
			"\nSections " + sections + "\nPipeline " + pipeline + 
			"\nInitPool " + initPool + " MaxPool " + maxPool + " KeepAlive " + keepAlive);
		System.out.println("All: " + all + ", rdfAndAnnotations: " + rdfAndAnnotations 
				+ ", rdf: " + rdf + ", whatizit: " + whatizit + ", ncboAnnotator: " + ncboAnnotator 
				+ ", bio2rdf: " + bio2rdf
				);		
		
		PmcOpenAccessRDFHandlerWithThreads handler = new PmcOpenAccessRDFHandlerWithThreads(initPool, maxPool, keepAlive);
		if (all) {
			handler.processDirectoryAll(pipeline, inputDir, outputDir, sections, limit, 0);
		} else if (rdfAndAnnotations) {
			handler.processDirectoryAndAnnotate(pipeline, inputDir, outputDir, sections, limit, 0);
		} else {
			if (rdf) {
				handler.rdfizeDirectory(inputDir, outputDir, sections, limit);
			}
			if (whatizit) {
				handler.annotateDirectoryRDF(pipeline, outputDir, limit);
			}
			if (ncboAnnotator) {
				handler.annotateDirectoryRDF(outputDir, limit);
			} 
		}
		handler.shutDown();
		while (!handler.isTerminated()); //waiting
		long endTime = System.currentTimeMillis();
		System.out.println("\nTotal time: " + (endTime-startTime));
	}
	
	/**
	 * Process n number of xml files in a directory to the given limit, converts files to RDF, annotates them and generate the bio2rdf consolidated view.
	 * @param inputDir
	 * @param outputDir
	 * @param sections
	 * @param limit
	 */
	public void processDirectoryAll(final String pipeline, String inputDir, final String outputDir, final boolean sections, Integer limit, int i) {
		File dir = new File(inputDir); 
		int count = 1;
		for (final File subdir:dir.listFiles()) {			
			if (subdir.isDirectory()) { //only one level
				//this.processDirectory(pipeline, subdir.getAbsolutePath(), outputDir, sections, limit, i);
			} else {	
				if (subdir.getName().endsWith(".nxml")) {
					this.runTask(new Runnable() {
		                public void run() {
		                	try {
		                		PmcOpenAccessHelper helper = new PmcOpenAccessHelper();
								helper.processFileAll(pipeline, subdir, outputDir, sections);
								helper = null;
		                	} catch (Exception e) {
		                		logger.error(subdir.getName() + " could not be processed: " + e.getMessage());
								if ((e instanceof DTDException) || (e instanceof ArticleTypeException) || (e instanceof PMCIdException)) {
									
								} else {
									e.printStackTrace();
								}
							}
		                }
		            });	
				}
				count++;
			}
			if (count % 1000 == 0) {
				System.gc();
			}
		}
	}
	/**
	 * Process n number of xml files in a directory to the given limit, converts files to RDF and annotates them.
	 * @param inputDir
	 * @param outputDir
	 * @param sections
	 * @param limit
	 */
	public void processDirectoryAndAnnotate(final String pipeline, String inputDir, final String outputDir, final boolean sections, Integer limit, int i) {
		File dir = new File(inputDir); 
		int count = 1;
		for (final File subdir:dir.listFiles()) {			
			if (subdir.isDirectory()) { //only one level
				//this.processDirectory(pipeline, subdir.getAbsolutePath(), outputDir, sections, limit, i);
			} else {	
				if (subdir.getName().endsWith(".nxml")) {					
					this.runTask(new Runnable() {
		                public void run() {
		                	try {
		                		PmcOpenAccessHelper helper = new PmcOpenAccessHelper();
								helper.processFileRDFAndAnnotate(pipeline, subdir, outputDir, sections);
								helper = null;							
		                	} catch (Exception e) {
		                		logger.error(subdir.getName() + " could not be processed: " + e.getMessage());
								if ((e instanceof DTDException) || (e instanceof ArticleTypeException) || (e instanceof PMCIdException)) {
									
								} else {
									e.printStackTrace();
								}
							}
		                }
		            });	
				}
				count++;
			}
			if (count % 1000 == 0) {
				System.gc();
			}
		}
	}
	/**
	 * Process n number of xml files in a directory to the given limit, converts files to RDF.
	 * @param inputDir
	 * @param outputDir
	 * @param sections
	 * @param limit
	 */
	public void rdfizeDirectory(String inputDir, final String outputDir, final boolean sections, Integer limit) {
		File dir = new File(inputDir);
		int count = 1;
		for (final File subdir:dir.listFiles()) {			
			if (subdir.isDirectory()) { //only one level
				//this.processDirectory(pipeline, subdir.getAbsolutePath(), outputDir, sections, limit, i);
			} else {	
				if (subdir.getName().endsWith(".nxml")) {
					this.runTask(new Runnable() {
		                public void run() {
		                	try {
		                		PmcOpenAccessHelper helper = new PmcOpenAccessHelper();
								helper.rdfizeFile(subdir, outputDir, sections);
								helper = null;
							} catch (Exception e) {
								logger.error(subdir.getName() + " could not be processed: " + e.getMessage());
								if ((e instanceof DTDException) || (e instanceof ArticleTypeException) || (e instanceof PMCIdException)) {
									
								} else {
									e.printStackTrace();
								}																
							}
		                }
		            });						
				}
				count++;
			}
			if (count % 1000 == 0) {
				System.gc();
			}
		}
//		while (!queue.isEmpty() ) {
//			handler.logger.info("Waiting for " + queue.size() + " jobs");
//        }
	}
	/**
	 * Process n number of rdf files in a directory to the given limit, annotates files with whatizit pipeline.
	 * @param pipeline
	 * @param outputDir Where the rdf (rdfized xml papers are)
	 * @param limit
	 */
	public void annotateBothDirectoryRDF(final String pipeline, final String outputDir, Integer limit) {
		File dir = new File(outputDir);
		int count = 1;
		for (final File subdir:dir.listFiles()) {			
			if (subdir.isDirectory()) { //only one level
				
			} else {	
				if (subdir.getName().endsWith("_sections.rdf")) {
					this.runTask(new Runnable() {
		                public void run() {
							try {
								PmcOpenAccessHelper helper = new PmcOpenAccessHelper();
								helper.annotateFileWithWhatizit(subdir, outputDir, pipeline);
								helper = null;					
							} catch (Exception e) {
								logger.error(subdir.getName() + " could not be processed: " + e.getMessage());
								if (e instanceof RDFModelIOException) {
									
								} else {
									e.printStackTrace();
								}	
							}
							try {
								PmcOpenAccessHelper helper = new PmcOpenAccessHelper();
								helper.annotateFileWithNCBOAnnotator(subdir, outputDir);
								helper = null;
							} catch (Exception e) {
								logger.error(subdir.getName() + " could not be processed: " + e.getMessage());
								if (e instanceof DTDException) {
									
								} else if (e instanceof RDFModelIOException) {
									
								} else {
									e.printStackTrace();
								}																
							}
		                }
		            });	
				}
				count++;
			}
			if (count % 1000 == 0) {
				System.gc();
			}
		}
	}
	/**
	 * Process n number of rdf files in a directory to the given limit, annotates files with whatizit pipeline.
	 * @param pipeline
	 * @param outputDir Where the rdf (rdfized xml papers are)
	 * @param limit
	 */
	public void annotateDirectoryRDF(final String pipeline, final String outputDir, Integer limit) {
		File dir = new File(outputDir);
		int count = 1;
		for (final File subdir:dir.listFiles()) {			
			if (subdir.isDirectory()) { //only one level
				
			} else {	
				if (subdir.getName().endsWith("_sections.rdf")) {
					this.runTask(new Runnable() {
		                public void run() {
		                	try {
		                		PmcOpenAccessHelper helper = new PmcOpenAccessHelper();
								helper.annotateFileWithWhatizit(subdir, outputDir, pipeline);
								helper = null;	
							} catch (Exception e) {
								logger.error(subdir.getName() + " could not be processed: " + e.getMessage());
								if (e instanceof RDFModelIOException) {
									
								} else {
									e.printStackTrace();
								}	
							}
		                }
		            });	
				}
				count++;
			}
			if (count % 1000 == 0) {
				System.gc();
			}
		}
	}
	/**
	 * Process n number of rdf files in a directory to the given limit, annotates files with ncboAnnotator.
	 * @param outputDir Where the rdf (rdfized xml papers are)
	 * @param limit
	 */
	public void annotateDirectoryRDF(final String outputDir, Integer limit) {
		File dir = new File(outputDir);
		int count = 1;
		for (final File subdir:dir.listFiles()) {			
			if (subdir.isDirectory()) { //only one level
				
			} else {	
				if (subdir.getName().endsWith("_sections.rdf")) {
					this.runTask(new Runnable() {
		                public void run() {
		                	try {
		                		PmcOpenAccessHelper helper = new PmcOpenAccessHelper();
								helper.annotateFileWithNCBOAnnotator(subdir, outputDir);
								helper = null;
							} catch (Exception e) {
								logger.error(subdir.getName() + " could not be processed: " + e.getMessage());
								if (e instanceof DTDException) {
									
								} else if (e instanceof RDFModelIOException) {
									
								} else {
									e.printStackTrace();
								}
							}
		                }
		            });
				}
				count++;
			}
			if (count % 1000 == 0) {
				System.gc();
			}
		}
	}
}
