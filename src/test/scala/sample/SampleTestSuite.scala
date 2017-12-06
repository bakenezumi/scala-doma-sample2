package sample

import org.scalatest.FunSuite
import SampleApp2._

class SampleTestSuite extends FunSuite with org.scalatest.BeforeAndAfterAll {
  private val tx = AppConfig.getTransactionManager
  private lazy val dao: EmpDao = new EmpDaoImpl(AppConfig)

  override def beforeAll(): Unit = {
    tx.required({ () =>
      dao.create()
    }: Runnable)
  }

  test("insert & select") {
    tx.required({ () =>
      tx.setRollbackOnly()
      val result = dao.insert(Emp(ID(-1), "foo", 10, -1))
      val selected = dao.selectById(result.getEntity.id)
      assert(selected.get == Emp(ID(1), "foo", 10, 1))
    }: Runnable)
  }
}
