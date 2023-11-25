package vf.adjourn.controller.app

import utopia.flow.async.context.ThreadPool
import utopia.flow.collection.CollectionExtensions._
import utopia.flow.event.model.ChangeResponse
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.json.{JsonParser, JsonReader}
import utopia.flow.time.Now
import utopia.flow.time.TimeExtensions._
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.util.console.{ArgumentSchema, CommandArguments}
import utopia.flow.util.logging.{Logger, SysErrLogger}
import utopia.flow.view.mutable.eventful.{EventfulPointer, SettableOnce}
import utopia.flow.view.template.eventful.Changing

import java.nio.file.Paths
import java.time.Instant
import scala.collection.immutable.VectorBuilder
import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
 * An application for moving a large number of files to a new location
 * @author Mikko Hilpinen
 * @since 15.10.2023, v0.1
 */
object AdjournApp extends App
{
	// ATTRIBUTES   -----------------------
	
	private implicit val log: Logger = SysErrLogger
	private implicit val jsonParser: JsonParser = JsonReader
	private implicit val exc: ExecutionContext = new ThreadPool("Adjourn-App", 2, 50, 20.seconds)
	
	private val paramSchema = Vector(
		ArgumentSchema("input", "in"),
		ArgumentSchema("output", "out"),
		// ArgumentSchema.flag("noCopy", "NC"),
		ArgumentSchema.flag("onlyCopy", "C")
	)
	private val _args = CommandArguments(paramSchema, args.toVector)
	
	
	// APPLICATION CODE ------------------
	
	// Requests input and output directories
	_args("input").string
		.orElse { StdIn.readNonEmptyLine("Please specify the directory from which files are read") }
		.flatMap { p => Some(Paths.get(p)).filter { _.exists } } match
	{
		case Some(inputDir) =>
			_args("output").string
				.orElse { StdIn.readNonEmptyLine(
					"Please specify the directory where the files should be moved") }
				.flatMap { p => Paths.get(p).createDirectories().logToOption } match
			{
				case Some(outputDir) =>
					val startTime = Now.toInstant
					val copyCounter = EventfulPointer(0)
					val totalCountPointer = SettableOnce[Int]()
					var copyCompleted = false
					
					// Tracks and prints copy progress
					totalCountPointer.onceSet { totalCount =>
						reportProgress("Copy", copyCounter, totalCount, startTime)
					}
					
					println("Starting file transfer while counting the number of files to move...")
					// Counting the number of files to copy (asynchronously)
					Future {
						var totalCount = 0
						inputDir.allChildrenIterator.foreachWhile(!copyCompleted) {
							case Success(p) =>
								if (p.isRegularFile)
									totalCount += 1
							case Failure(error) => log(error, "Failed to count the number of files")
						}
						if (!copyCompleted) {
							println(s"Total number of files to copy is $totalCount")
							totalCountPointer.value = Some(totalCount)
						}
					}
					
					// Copying files over
					inputDir.allChildrenIterator
						.flatMapSuccesses { p =>
							// Only copies regular files
							if (p.isRegularFile) {
								// Preserves relative path
								val result = p.relativeTo(inputDir) match {
									case Left(_) => p.copyTo(outputDir)
									case Right(relative) => p.copyAs(outputDir/relative)
								}
								copyCounter.update { _ + 1 }
								result
							}
							else
								Success(p)
						}
						.findMap { _.failure } match
					{
						// Case: Copy-process failed
						case Some(error) =>
							log(error, "Copy process failed")
							
						// Case: Copy-process succeeded
						case None =>
							copyCompleted = true
							val totalCount = copyCounter.value
							println(s"Successfully copied all $totalCount files")
							outputDir.openInDesktop()
							if (!_args("onlyCopy").getBoolean &&
								StdIn.ask(s"Is it okay to now delete these files from ${inputDir.absolute}?",
									default = true))
							{
								// Deletes all files from the input directory
								val deleteCounter = EventfulPointer(0)
								
								// Tracks and prints progress
								reportProgress("Delete", deleteCounter, totalCount)
								
								val failuresBuilder = new VectorBuilder[Throwable]()
								inputDir.toTree.bottomToTopNodesIterator.map { _.nav }.foreach { p =>
									p.delete().failure.foreach { failuresBuilder += _ }
									deleteCounter.update { _ + 1 }
								}
								val failures = failuresBuilder.result()
								failures.headOption.foreach { e =>
									log(e, s"Encountered ${failures.size} failures while deleting files")
									inputDir.openInDesktop()
								}
							}
							println("Process completed!")
					}
					
				case None => println("Output directory not available. Terminates.")
			}
		case None => println("Specified input directory doesn't exists. Terminates.")
	}
	
	println("Closing. Bye!")
	
	
	// OTHER    -----------------------
	
	private def reportProgress(operation: String, counter: Changing[Int], totalCount: Int,
	                           startTime: Instant = Now.toInstant) =
	{
		val percentagePointer = counter.map { cc => cc * 100 / totalCount }
		var lastPrintTime = Instant.EPOCH
		val minReportInterval = 2.seconds
		percentagePointer.addListener { e =>
			val t = Now.toInstant
			val completionPercent = e.newValue
			if (t > lastPrintTime + minReportInterval) {
				lastPrintTime = t
				val timeUsed = t - startTime
				val timePerPercent = timeUsed / completionPercent
				val eta = timePerPercent * (100 - completionPercent)
				println(s"$operation $completionPercent% completed. ETA: ${eta.description}")
			}
			ChangeResponse.continueUnless(completionPercent >= 100)
		}
	}
}
