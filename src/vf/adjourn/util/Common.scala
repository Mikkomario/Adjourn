package vf.adjourn.util

import utopia.flow.async.context.ThreadPool
import utopia.flow.util.logging.{Logger, SysErrLogger}
import utopia.vault.database.ConnectionPool

import scala.concurrent.ExecutionContext

/**
 * Contains commonly used environmental values
 * @author Mikko Hilpinen
 * @since 24.11.2023, v0.1
 */
object Common
{
	implicit val log: Logger = SysErrLogger
	implicit val exc: ExecutionContext = new ThreadPool("Adjourn")
	implicit val cPool: ConnectionPool = new ConnectionPool()
}
