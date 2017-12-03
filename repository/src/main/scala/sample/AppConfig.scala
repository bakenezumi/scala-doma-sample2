package sample

import javax.sql.DataSource

import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.dialect._
import org.seasar.doma.jdbc.tx._

object AppConfig extends Config {
  val dataSource = new LocalTransactionDataSource(
    "jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1",
    "sa",
    null)
  val transactionManager = new LocalTransactionManager(
    dataSource.getLocalTransaction(getJdbcLogger))

  Class.forName("org.h2.Driver")

  override def getDialect: Dialect = new H2Dialect()

  override def getDataSource: DataSource = dataSource

  override def getTransactionManager: TransactionManager = transactionManager
}
